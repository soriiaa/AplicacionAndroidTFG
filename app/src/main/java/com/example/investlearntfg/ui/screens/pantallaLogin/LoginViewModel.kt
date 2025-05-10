package com.example.investlearntfg.ui.screens.pantallaLogin

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.investlearntfg.data.repository.PostRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val _correo = MutableStateFlow(TextFieldValue())
    val correo: StateFlow<TextFieldValue> = _correo

    private val _contrasena = MutableStateFlow(TextFieldValue())
    val contrasena: StateFlow<TextFieldValue> = _contrasena

    fun onCorreoChange(value: TextFieldValue) {
        _correo.value = value
    }

    fun onContrasenaChange(value: TextFieldValue) {
        _contrasena.value = value
    }

    fun iniciarSesion(
        onExito: () -> Unit,
        onError: (String) -> Unit
    ) {
        val email = _correo.value.text.trim()
        val password = _contrasena.value.text

        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password).await()
                onExito()
            } catch (e: Exception) {
                onError(e.message ?: "Error desconocido al iniciar sesión.")
            }
        }
    }
}