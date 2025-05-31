package com.example.investlearntfg.ui.screens.pantallaCompraAccion

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.investlearntfg.data.model.EmpresaPreview
import com.example.investlearntfg.data.model.PrecioCompania2
import com.example.investlearntfg.data.repository.FirestoreRepository
import com.example.investlearntfg.data.repository.PostRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PantallaCompraAccionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val postRepository: PostRepository
) : ViewModel() {

    private var userId = Firebase.auth.currentUser?.uid ?: ""

    // Datos de la empresa que vienen de la pantalla de la acción
    private val _empresa = MutableStateFlow<EmpresaPreview?>(null)
    val empresa: StateFlow<EmpresaPreview?> = _empresa

    // Precio y cambio de la accion actual
    private val _precioCompania2 = MutableStateFlow<PrecioCompania2?>(null)
    val precioCompania2: StateFlow<PrecioCompania2?> = _precioCompania2

    // Dinero que tiene el usuario en la cuenta
    private val _dineroCuenta = MutableStateFlow<Double?>(null)
    val dineroCuenta: StateFlow<Double?> = _dineroCuenta

    // Simbolo de la moneda utilizada por el usuario
    private val _simboloMonedaUsuario = MutableStateFlow<String?>(null)
    val simboloMonedaUsuario: StateFlow<String?> = _simboloMonedaUsuario

    // La tasa de cambio que hay entre la moneda del usuario y la de la acción
    private val _tipoCambio = MutableStateFlow<Double?>(null)
    val tipoCambio: StateFlow<Double?> = _tipoCambio

    // La tasa de cambio que hay entre la moneda de la acción y la del usuario
    private val _tipoCambioInverso = MutableStateFlow<Double?>(null)
    val tipoCambioInverso: StateFlow<Double?> = _tipoCambioInverso

    // La cantidad de acciones que el usuario selecciona para comprar
    private val _cantidadAcciones = MutableStateFlow(1)
    val cantidadAcciones: StateFlow<Int> = _cantidadAcciones

    // El precio total que va a pagar el usuario
    private val _precioCompra = MutableStateFlow(0.0)
    val precioCompra: StateFlow<Double> = _precioCompra

    init {
        cargarInformacionPreviaEmpresa(savedStateHandle)
        cargarDineroCuenta(userId)
        cargarSimboloMoneda(userId)
    }

    fun guardarCompraEnFirestore(onExito: () -> Unit, onFallo: () -> Unit) {

        FirestoreRepository.restarDineroCuenta(
            userId,
            _precioCompra.value
        )

        _empresa.value?.let {
            FirestoreRepository.guardarCompraEnFirestore(
                userId = userId,
                ticker = it.ticker,
                precioCompra = _empresa.value!!.precio,
                unidades = _cantidadAcciones.value,
                onExito = onExito,
                onFallo = onFallo
            )

            FirestoreRepository.subirTransaccion(
                userId = userId,
                ticker = it.ticker,
                tipoTransaccion = "Compra",
                precioTransaccion = _empresa.value!!.precio,
                unidades = _cantidadAcciones.value
            )
        }

        FirestoreRepository.incrementarComprasRealizadas(userId)
    }

    fun setPrecioCompra(nuevoPrecio: Double) {
        _precioCompra.value = nuevoPrecio
    }

    fun setCantidadAcciones(nuevaCantidad: Int) {
        _cantidadAcciones.value = nuevaCantidad
    }

    private suspend fun cargarTasaDeCambio(
        simboloMonedaUsuario: String?,
        simboloMonedaAccion: String
    ) {
        val codigoMonedaUsuario = simboloMonedaUsuario?.let { obtenerCodigoMoneda(it) }
        val codigoMonedaAccion = obtenerCodigoMoneda(simboloMonedaAccion)

        val respuesta = codigoMonedaUsuario?.let {
            postRepository.convertirMonedaPostRepository(
                it,
                codigoMonedaAccion
            )
        }
        Log.d("BBBBBBBBBBBBB", "$respuesta")
        if (respuesta != null) {
            _tipoCambio.value = respuesta.result
            _tipoCambioInverso.value = 1 / respuesta.result
        }
    }

    fun cargarSimboloMoneda(userId: String) {
        FirestoreRepository.getSimboloMoneda(userId) { simboloMoneda ->
            _simboloMonedaUsuario.value = when (simboloMoneda) {
                "Dólar estadounidense - $ - USD" -> "$"
                "Euro - € - EUR" -> "€"
                "Libra esterlina - £ - GBP" -> "£"
                "Yen japonés - ¥ - JPY" -> "¥"
                "Franco suizo - CHF - CHF" -> "CHF"
                else -> ""
            }
            viewModelScope.launch {
                _empresa.value?.let {
                    cargarTasaDeCambio(
                        _simboloMonedaUsuario.value,
                        it.simboloMoneda
                    )
                }
            }
        }
    }

    fun cargarDineroCuenta(userId: String) {
        FirestoreRepository.getDineroCuenta(userId) { dinero ->
            _dineroCuenta.value = dinero
        }
    }

    fun cargarPrecioAccion() {
        _empresa.value?.let { empresa ->
            viewModelScope.launch {
                _precioCompania2.value =
                    postRepository.getPrecioEmpresa2PostRepository(empresa.ticker)
            }
        }
    }

    fun cargarInformacionPreviaEmpresa(savedStateHandle: SavedStateHandle) {
        val empresaJson = savedStateHandle.get<String>("empresaJson")
        val empresaDeserializada = empresaJson?.let {
            Gson().fromJson(it, EmpresaPreview::class.java)
        }
        _empresa.value = empresaDeserializada
    }

    fun obtenerCodigoMoneda(simbolo: String): String {
        return when (simbolo) {
            "$" -> "USD"
            "€" -> "EUR"
            "£" -> "GBP"
            "¥" -> "JPY"
            "CHF" -> "CHF"
            "C$" -> "CAD"
            "A$" -> "AUD"
            "kr" -> "SEK"
            "₩" -> "KRW"
            "₹" -> "INR"
            "R$" -> "BRL"
            "₽" -> "RUB"
            "HK$" -> "HKD"
            "NZ$" -> "NZD"
            "₺" -> "TRY"
            "Rp" -> "IDR"
            "R" -> "ZAR"
            else -> "?"
        }
    }

}