package com.example.investlearntfg.ui.screens.pantallaBuscar

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.investlearntfg.data.model.EmpresaPreview
import com.example.investlearntfg.data.repository.FavoritosRepository
import com.example.investlearntfg.data.repository.PostRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PantallaBuscarViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {

    private val _empresas = MutableStateFlow<List<EmpresaPreview>>(emptyList())
    val empresas: StateFlow<List<EmpresaPreview>> = _empresas

    private val _textoBuscador = MutableStateFlow(TextFieldValue(""))
    val textoBuscador: StateFlow<TextFieldValue> = _textoBuscador

    val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando

    private val _empresasFavoritas = MutableStateFlow<Set<String>>(emptySet())
    val empresasFavoritas: StateFlow<Set<String>> = _empresasFavoritas

    fun onTextoBuscadorChange(nuevoTexto: TextFieldValue) {
        _textoBuscador.value = nuevoTexto
    }

    private val userId = Firebase.auth.currentUser?.uid ?: ""

    init {
        cargarEmpresasDestacadas()
        cargarFavoritos()
    }

    fun cargarEmpresasDestacadas() {

        viewModelScope.launch {

            _cargando.value = true

            try {

                val empresasDestacadas = listOf("AAPL", "GOOGL", "MSFT", "AMZN", "TSLA", "BLK", "NVDA", "BBVA")

                val empresasPreview = empresasDestacadas.mapNotNull { simbolo ->
                    try {
                        val perfil = postRepository.getPerfilEmpresaPostRepository(simbolo)
                        val precio = postRepository.getPrecioEmpresaPostRepository(simbolo)

                        val moneda = perfil.currency
                        val simboloMonetario = when (moneda) {
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

                        EmpresaPreview(
                            ticker = simbolo,
                            nombre = perfil.name,
                            logo = perfil.logo,
                            precio = precio.c,
                            simboloMoneda = simboloMonetario
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

    fun cargarFavoritos() {
        FavoritosRepository.obtenerFavoritas(userId) { favoritos ->
            _empresasFavoritas.value = favoritos.toSet()
        }
    }

    fun alternarFavorito(empresa: EmpresaPreview) {
        val esFavorita = _empresasFavoritas.value.contains(empresa.ticker)

        if (esFavorita) {
            FavoritosRepository.eliminarFavorita(userId, empresa.ticker)
            _empresasFavoritas.value -= empresa.ticker
        } else {
            FavoritosRepository.marcarComoFavorita(userId, empresa)
            _empresasFavoritas.value += empresa.ticker
        }
    }

}