package com.example.investlearntfg.ui.screens.pantallaInicio

import androidx.lifecycle.ViewModel
import com.example.investlearntfg.data.repository.PostRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PantallaInicialViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {

    fun recargarPantalla() {
        // Aquí recargas los datos o estado necesario
    }

    fun cerrarSesion() {
        FirebaseAuth.getInstance().signOut()
    }

}