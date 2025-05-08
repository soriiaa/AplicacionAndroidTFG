package com.example.investlearntfg.ui.screens.pantallaBuscar

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

    init {
        cargarEmpresasDestacadas()
    }

    private fun cargarEmpresasDestacadas() {

        viewModelScope.launch {
            try {

                val empresasDestacadas = listOf("AAPL", "GOOGL", "MSFT", "AMZN", "TSLA")

                val empresasPreview = empresasDestacadas.mapNotNull { simbolo ->
                    try {
                        val perfil = postRepository.getPerfilEmpresaPostRepository(simbolo)
                        val precio = postRepository.getPrecioEmpresaPostRepository(simbolo)

                        EmpresasPreview(
                            nombre = perfil.name,
                            logo = perfil.logo,
                            precio = precio.c
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
            }
        }
    }

}