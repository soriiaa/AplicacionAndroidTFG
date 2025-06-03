package com.example.investlearntfg.ui.screens.pantallaRegistro

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.investlearntfg.data.repository.FirestoreRepository
import com.example.investlearntfg.data.repository.PostRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {

    private val _textoNombre = MutableStateFlow(TextFieldValue())
    val textoNombre: StateFlow<TextFieldValue> = _textoNombre

    private val _textoApellido = MutableStateFlow(TextFieldValue())
    val textoApellido: StateFlow<TextFieldValue> = _textoApellido

    private val _textoNickname = MutableStateFlow(TextFieldValue())
    val textoNickname: StateFlow<TextFieldValue> = _textoNickname

    private val _textoCorreo = MutableStateFlow(TextFieldValue())
    val textoCorreo: StateFlow<TextFieldValue> = _textoCorreo

    private val _textoContrasena = MutableStateFlow(TextFieldValue())
    val textoContrasena: StateFlow<TextFieldValue> = _textoContrasena

    private val _textoConfirmarContrasena = MutableStateFlow(TextFieldValue())
    val textoConfirmarContrasena: StateFlow<TextFieldValue> = _textoConfirmarContrasena

    private val _monedaSeleccionada = MutableStateFlow("Elegir moneda")
    val monedaSeleccionada: StateFlow<String> = _monedaSeleccionada

    // En esta variable almaceno el estado del botón registro
    private val _botonHabilitado = MutableStateFlow(false)
    val botonHabilitado: StateFlow<Boolean> = _botonHabilitado

    // Aqui almaceno el estado acerca de si el correo electrónico tiene un formato correcto o no
    private val _formatoCorreoCorrecto = MutableStateFlow(true)
    val formatoCorreoCorrecto: StateFlow<Boolean> = _formatoCorreoCorrecto

    // En esta variable almaceno si el correo ya existia en la base de datos o no
    private val _correoYaExistente = MutableStateFlow(false)
    val correoYaExistente: StateFlow<Boolean> = _correoYaExistente

    // Aqui gestiono el estado del alert dialog cuando el correo ya está en uso
    private val _mostrarDialogoCorreoYaExistente = MutableStateFlow(false)
    val mostrarDialogoCorreoYaExistente: StateFlow<Boolean> = _mostrarDialogoCorreoYaExistente

    // Diálogo de registro exitoso
    private val _mostrarDialogoRegistroExitoso = MutableStateFlow(false)
    val mostrarDialogoRegistroExitoso: StateFlow<Boolean> = _mostrarDialogoRegistroExitoso

    // Diálogo de registro erróneo
    private val _mostrarDialogoRegistroErroneo = MutableStateFlow(false)
    val mostrarDialogoRegistroErroneo: StateFlow<Boolean> = _mostrarDialogoRegistroErroneo

    // Aqui almaceno si se ha enviado o no correctamente el correo de verificación
    private val _correoVerificacionEnviado = MutableStateFlow(false)
    val correoVerificacionEnviado: StateFlow<Boolean> = _correoVerificacionEnviado

    fun actualizarEstadoCorreoVerificacion(enviado: Boolean) {
        _correoVerificacionEnviado.value = enviado
    }

    // Funciones para mostrar u ocultar los dialogos de exito o error del registro
    fun mostrarDialogoExitoso() {
        _mostrarDialogoRegistroExitoso.value = true
    }

    fun ocultarDialogoExitoso() {
        _mostrarDialogoRegistroExitoso.value = false
    }

    fun mostrarDialogoErroneo() {
        _mostrarDialogoRegistroErroneo.value = true
    }

    fun ocultarDialogoErroneo() {
        _mostrarDialogoRegistroErroneo.value = false
    }

    // Funciones para mostrar u ocultar el dialogo de que el correo ya está en uso
    fun mostrarDialogoCorreo() {
        _mostrarDialogoCorreoYaExistente.value = true
    }

    fun ocultarDialogoCorreo() {
        _mostrarDialogoCorreoYaExistente.value = false
    }

    // Funciones para actualizar cada estado
    fun onNombreChange(newValue: TextFieldValue) {
        _textoNombre.value = newValue
        comprobarCampos()
    }

    fun onApellidoChange(newValue: TextFieldValue) {
        _textoApellido.value = newValue
        comprobarCampos()
    }

    fun onNicknameChange(newValue: TextFieldValue) {
        _textoNickname.value = newValue
        comprobarCampos()
    }

    fun onCorreoChange(newValue: TextFieldValue) {
        _textoCorreo.value = newValue
        comprobarCampos()
    }

    fun onContrasenaChange(newValue: TextFieldValue) {
        _textoContrasena.value = newValue
        comprobarCampos()
    }

    fun onConfirmarContrasenaChange(newValue: TextFieldValue) {
        _textoConfirmarContrasena.value = newValue
        comprobarCampos()
    }

    fun onMonedaSeleccionadaChange(newValue: String) {
        _monedaSeleccionada.value = newValue
        comprobarCampos()
    }

    fun comprobarCampos() {
        val camposValidos = _textoNombre.value.text.isNotBlank() &&
                _textoApellido.value.text.isNotBlank() &&
                _textoNickname.value.text.isNotBlank() &&
                _textoCorreo.value.text.isNotBlank() &&
                _textoContrasena.value.text.isNotBlank() &&
                _textoConfirmarContrasena.value.text.isNotBlank() &&
                _monedaSeleccionada.value != "Elegir moneda" &&
                _textoContrasena.value.text.length >= 6 &&
                _textoContrasena.value.text == _textoConfirmarContrasena.value.text &&
                esCorreoValido()

        _botonHabilitado.value = camposValidos
    }

    fun verificarCorreoExistente() {
        viewModelScope.launch {
            _correoYaExistente.value = FirestoreRepository.verificarCorreoExistente(_textoCorreo.value.text)
        }
    }

    fun esCorreoValido(): Boolean {
        val regex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}\$")
        _formatoCorreoCorrecto.value = _textoCorreo.value.text.matches(regex)
        return _formatoCorreoCorrecto.value
    }

    fun registrarUsuario() {
        viewModelScope.launch {

            if (_correoYaExistente.value) {
                _mostrarDialogoCorreoYaExistente.value = true
            } else {
                val respuesta = FirestoreRepository.registrarUsuario(
                    _textoNombre.value.text,
                    _textoApellido.value.text,
                    _textoNickname.value.text,
                    _textoCorreo.value.text,
                    _textoContrasena.value.text,
                    _monedaSeleccionada.value
                )

                if (respuesta) {

                    val respuestaCorreo = enviarVerificacionPorCorreo()
                    _correoVerificacionEnviado.value = respuestaCorreo

                    _mostrarDialogoRegistroExitoso.value = true
                    cerrarSesion()
                } else {
                    _mostrarDialogoRegistroErroneo.value = true
                }
            }
        }
    }

    fun enviarVerificacionPorCorreo(): Boolean {
        var respuesta = false
        viewModelScope.launch {
            respuesta = FirestoreRepository.enviarCorreoVerificacion()
        }
        return respuesta
    }

    fun cerrarSesion() {
        FirebaseAuth.getInstance().signOut()
    }

}
