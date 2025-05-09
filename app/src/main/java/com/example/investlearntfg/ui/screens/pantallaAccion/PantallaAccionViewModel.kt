package com.example.investlearntfg.ui.screens.pantallaAccion

import androidx.lifecycle.ViewModel
import com.example.investlearntfg.data.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PantallaAccionViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {

    private val _esEmpresaFavorita = MutableStateFlow(false)
    val esEmpresaFavorita: StateFlow<Boolean> = _esEmpresaFavorita

    fun alternarEsFavorito() {
        _esEmpresaFavorita.value = !_esEmpresaFavorita.value
    }

}