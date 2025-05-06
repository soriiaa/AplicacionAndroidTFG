package com.example.investlearntfg.ui.screens.pantallaInicio

import androidx.lifecycle.ViewModel
import com.example.investlearntfg.data.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PantallaInicialViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {





    fun recargarPantalla() {
        // Aquí recargas los datos o estado necesario
    }

}