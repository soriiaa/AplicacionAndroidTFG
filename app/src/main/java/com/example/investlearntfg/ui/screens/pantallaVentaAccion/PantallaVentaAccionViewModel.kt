package com.example.investlearntfg.ui.screens.pantallaVentaAccion

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.investlearntfg.data.model.EmpresaPreview
import com.example.investlearntfg.data.model.PaqueteAcciones
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
open class PantallaVentaAccionViewModel @Inject constructor(
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

    // Lista paquetes en posesion por el usuario
    private val _listaPaquetesEnPosesion = MutableStateFlow<List<PaqueteAcciones>>(emptyList())
    val listaPaquetesEnPosesion: StateFlow<List<PaqueteAcciones>> = _listaPaquetesEnPosesion

    // Aqui almaceno los id de los paquetes que han sido seleccionados
    private val _paquetesSeleccionados = MutableStateFlow<Set<String>>(emptySet())
    val paquetesSeleccionados: StateFlow<Set<String>> = _paquetesSeleccionados

    init {
        cargarInformacionPreviaEmpresa(savedStateHandle)
        cargarDineroCuenta(userId)
        cargarSimboloMoneda(userId)
        obtenerPaquetesPropiedadPorTicker()
    }

    fun venderPaquetesSeleccionados(onResult: (exito: Boolean, mensaje: String?) -> Unit) {
        try {
            var ganancia = 0.0

            _paquetesSeleccionados.value.forEach { paqueteId ->

                val paquete = _listaPaquetesEnPosesion.value.find { it.id == paqueteId }
                _empresa.value?.let {
                    _precioCompania2.value?.let { it1 ->
                        if (paquete != null) {
                            FirestoreRepository.subirTransaccion(
                                userId = userId,
                                ticker = it.ticker,
                                tipoTransaccion = "Venta",
                                precioTransaccion = it1.c,
                                unidades = paquete.unidades,
                                fotoAccion = _empresa.value!!.logo
                            )
                        }
                    }
                }
                FirestoreRepository.eliminarAccionEnPropiedad(userId, paqueteId)
                ganancia += (((_precioCompania2.value?.c ?: 0.0) - paquete?.precioCompra!!) * paquete.unidades)
            }

            FirestoreRepository.incrementarVentasRealizadas(userId)

            if ((_empresa.value?.simboloMoneda ?: 0.0) != simboloMonedaUsuario.value) {
                ganancia *= _tipoCambioInverso.value!!
            }

            FirestoreRepository.incrementarGananciasTotales(userId, ganancia)

            onResult(true, null)

        } catch (e: Exception) {
            println("Error al vender la acción ${e.message}")
            onResult(false, "Error al vender la acción")
        }
    }

    fun obtenerPaquetesPropiedadPorTicker() {
        viewModelScope.launch {
            _empresa.value?.let {
                _listaPaquetesEnPosesion.value =
                    FirestoreRepository.obtenerPaquetesPropiedadPorTicker(userId, it.ticker)
            }
        }
    }

    fun alternarSeleccion(id: String) {
        _paquetesSeleccionados.value = _paquetesSeleccionados.value.toMutableSet().apply {
            if (contains(id)) remove(id) else add(id)
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

    fun cargarDineroCuenta(userId: String) {
        FirestoreRepository.getDineroCuenta(userId) { dinero ->
            _dineroCuenta.value = dinero
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