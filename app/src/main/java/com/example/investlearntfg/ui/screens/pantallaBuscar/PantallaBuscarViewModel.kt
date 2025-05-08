package com.example.investlearntfg.ui.screens.pantallaBuscar

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.investlearntfg.data.model.EmpresasPreview
import com.example.investlearntfg.data.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PantallaBuscarViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {

    private val _empresas = MutableStateFlow<List<EmpresasPreview>>(emptyList())
    val empresas: StateFlow<List<EmpresasPreview>> = _empresas

    private val _textoBuscador = MutableStateFlow(TextFieldValue(""))
    val textoBuscador: StateFlow<TextFieldValue> = _textoBuscador

    val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando

    fun onTextoBuscadorChange(nuevoTexto: TextFieldValue) {
        _textoBuscador.value = nuevoTexto
    }

    init {
        cargarEmpresasDestacadas()
    }

    fun cargarEmpresasDestacadas() {

        viewModelScope.launch {

            _cargando.value = true

            try {

                val empresasDestacadas = listOf("AAPL", "GOOGL", "MSFT", "AMZN", "TSLA", "BLK", "NVDA", "REP.MC", "BBVA")

                val empresasPreview = empresasDestacadas.mapNotNull { simbolo ->
                    try {
                        val perfil = postRepository.getPerfilEmpresaPostRepository(simbolo)
                        val precio = postRepository.getPrecioEmpresaPostRepository(simbolo)

                        val moneda = perfil.currency
                        val simbolo = when (moneda) {
                            "USD" -> "$"
                            "EUR" -> "€"
                            "GBP" -> "£"
                            "JPY" -> "¥"
                            "CHF" -> "CHF"
                            "CAD" -> "C$"
                            "AUD" -> "A$"
                            "CNY" -> "¥"
                            "SEK" -> "kr"
                            "NOK" -> "kr"
                            "KRW" -> "₩"
                            "INR" -> "₹"
                            "BRL" -> "R$"
                            "MXN" -> "$"
                            "RUB" -> "₽"
                            "HKD" -> "HK$"
                            "NZD" -> "NZ$"
                            "TRY" -> "₺"
                            "IDR" -> "Rp"
                            "ZAR" -> "R"
                            else -> "?"
                        }

                        EmpresasPreview(
                            nombre = perfil.name,
                            logo = perfil.logo,
                            precio = precio.c,
                            simboloMoneda = simbolo
                        )

                    } catch (e: Exception) {
                        println("Error al cargar datos de $simbolo: ${e.message}")
                        null
                    }
                }

                _empresas.value = empresasPreview

            } catch (e: Exception) {
                println("Error general al cargar empresas destacadas: ${e.message}")
                _empresas.value = emptyList()
            } finally {
                _cargando.value = false
            }
        }
    }

}