package com.example.investlearntfg.ui.screens.pantallaCompraAccion

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.investlearntfg.data.model.EmpresaPreview
import com.example.investlearntfg.data.model.PrecioCompania2
import com.example.investlearntfg.data.repository.PostRepository
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

    // Datos de la empresa que vienen de la pantalla de la acción
    private val _empresa = MutableStateFlow<EmpresaPreview?>(null)
    val empresa: StateFlow<EmpresaPreview?> = _empresa

    // Precio y cambio de la accion actual
    private val _precioCompania2 = MutableStateFlow<PrecioCompania2?>(null)
    val precioCompania2: StateFlow<PrecioCompania2?> = _precioCompania2

    init {
        cargarInformacionPreviaEmpresa(savedStateHandle)
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