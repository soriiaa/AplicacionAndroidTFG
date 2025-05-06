package com.example.investlearntfg.ui.screens.pantallaRecuperarContrasena

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.investlearntfg.data.repository.PostRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class RecuperarContrasenaViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {

    private val _correo = MutableStateFlow(TextFieldValue())
    val correo: StateFlow<TextFieldValue> = _correo

    private val _estado = MutableStateFlow<EstadoRecuperacion>(EstadoRecuperacion.Inactivo)
    val estado: StateFlow<EstadoRecuperacion> = _estado

    private val _botonHabilitado = MutableStateFlow(true)
    val botonHabilitado: StateFlow<Boolean> = _botonHabilitado

    val puedeEnviarEmail = combine(_correo, _botonHabilitado) { correo, botonActivo ->
        val emailEscrito = correo.text.trim().isNotEmpty()
        val emailValido = correo.text.trim().formatoEmailCorrecto()
        return@combine emailEscrito && emailValido && botonActivo
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    fun String.formatoEmailCorrecto(): Boolean =
        this.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()

    fun onCorreoChange(value: TextFieldValue) {
        _correo.value = value
    }

    fun recuperarContrasena() {

        _botonHabilitado.value = false

        val correoTexto = _correo.value.text
        _estado.value = EstadoRecuperacion.Cargando

        FirebaseAuth.getInstance().sendPasswordResetEmail(correoTexto)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _estado.value = EstadoRecuperacion.Exito("Correo de recuperación enviado.")
                } else {
                    _estado.value = EstadoRecuperacion.Error("Error al enviar correo.")
                }
                _botonHabilitado.value = true
            }
    }


}

sealed class EstadoRecuperacion {
    object Inactivo : EstadoRecuperacion()
    object Cargando : EstadoRecuperacion()
    data class Exito(val mensaje: String) : EstadoRecuperacion()
    data class Error(val mensaje: String) : EstadoRecuperacion()
}