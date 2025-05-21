package com.example.investlearntfg.ui.screens.pantallaCompraAccion

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

    init {
        cargarInformacionPreviaEmpresa(savedStateHandle)
        cargarDineroCuenta(userId)
        cargarSimboloMoneda(userId)
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
                _precioCompania2.value = postRepository.getPrecioEmpresa2PostRepository(empresa.ticker)
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