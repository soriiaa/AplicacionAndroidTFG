package com.example.investlearntfg.ui.screens.pantallaEditarPerfil

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.investlearntfg.data.repository.FirestoreRepository
import com.example.investlearntfg.data.repository.PostRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PantallaEditarPerfilScreenViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {

    private var userId = Firebase.auth.currentUser?.uid ?: ""

    // En estas 3 variables tengo guardado lo que estaba en la base de datos
    private val _nicknameOriginal = MutableStateFlow("")
    val nicknameOriginal: StateFlow<String> = _nicknameOriginal

    private val _nombreOriginal = MutableStateFlow("")
    val nombreOriginal: StateFlow<String> = _nombreOriginal

    private val _apellidosOriginal = MutableStateFlow("")
    val apellidosOriginal: StateFlow<String> = _apellidosOriginal

    // Y en estas 3 tengo guardado lo que va a variar
    private val _nicknameIntroducido = MutableStateFlow("")
    val nicknameIntroducido: StateFlow<String> = _nicknameIntroducido

    private val _nombreIntroducido = MutableStateFlow("")
    val nombreIntroducido: StateFlow<String> = _nombreIntroducido

    private val _apellidosIntroducido = MutableStateFlow("")
    val apellidosIntroducido: StateFlow<String> = _apellidosIntroducido

    // Aqui almaceno el estado del botón de guardar
    private val _botonGuardarHabilitado = MutableStateFlow(false)
    val botonGuardarHabilitado: StateFlow<Boolean> = _botonGuardarHabilitado

    // Uso estas variables para almacenar sus respectivos dialogos
    private val _mostrarDialogoExito = MutableStateFlow(false)
    val mostrarDialogoExito: StateFlow<Boolean> = _mostrarDialogoExito

    private val _mostrarDialogoError = MutableStateFlow(false)
    val mostrarDialogoError: StateFlow<Boolean> = _mostrarDialogoError

    fun subirCambiosAFirebase() {
        FirestoreRepository.setDatosNuevosUsuario(
            userId = userId,
            nickname = _nicknameIntroducido.value,
            nombre = _nombreIntroducido.value,
            apellidos = _apellidosIntroducido.value,
            onResultado = {
                _mostrarDialogoExito.value = true
            },
            onError = {
                _mostrarDialogoError.value = true
            }
        )
    }

    fun comprobarDiferenciaEntreDatos() {
        if (
            _nicknameIntroducido.value.isNotBlank() &&
            _nombreIntroducido.value.isNotBlank() &&
            _apellidosIntroducido.value.isNotBlank() &&
            (
                    _nicknameIntroducido.value != _nicknameOriginal.value ||
                            _nombreIntroducido.value != _nombreOriginal.value ||
                            _apellidosIntroducido.value != _apellidosOriginal.value
                    )
        ) {
            _botonGuardarHabilitado.value = true
        } else {
            _botonGuardarHabilitado.value = false
        }
    }

    fun asignarDatosUsuarioVariables() {
        _nicknameIntroducido.value = _nicknameOriginal.value
        _nombreIntroducido.value = _nombreOriginal.value
        _apellidosIntroducido.value = _apellidosOriginal.value
    }

    fun cargarDatosUsuario() {
        FirestoreRepository.getNicknameNombreApellidoUsuario(
            userId = userId,
            onResultado = { nickname, nombre, apellidos ->
                _nicknameOriginal.value = nickname ?: ""
                _nombreOriginal.value = nombre ?: ""
                _apellidosOriginal.value = apellidos ?: ""
                asignarDatosUsuarioVariables()
            },
            onError = { error ->
                Log.e("Firestore", "Error al obtener datos del usuario: ${error.message}")
            }
        )
    }

    fun setNicknameIntroducido(nuevo: String) {
        _nicknameIntroducido.value = nuevo
    }

    fun setNombreIntroducido(nuevo: String) {
        _nombreIntroducido.value = nuevo
    }

    fun setApellidosIntroducido(nuevo: String) {
        _apellidosIntroducido.value = nuevo
    }

}

