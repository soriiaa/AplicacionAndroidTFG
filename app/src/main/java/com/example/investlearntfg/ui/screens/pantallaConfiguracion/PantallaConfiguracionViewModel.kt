package com.example.investlearntfg.ui.screens.pantallaConfiguracion

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
class PantallaConfiguracionViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {

    private var userId = Firebase.auth.currentUser?.uid ?: ""

    // Aqui almaceno la moneda que el usuario tiene guardada como suya en la base de datos
    private val _monedaInicialUsuario = MutableStateFlow("")
    val monedaInicialUsuario: StateFlow<String> = _monedaInicialUsuario

    // Aqui almaceno la moneda que el usuario tiene seleccionada o ha seleccionado
    private val _monedaSeleccionada = MutableStateFlow("")
    val monedaSeleccionada: StateFlow<String> = _monedaSeleccionada

    // Aqui controlo el estado del botón de guardado
    private val _botonActivado = MutableStateFlow(false)
    val botonActivado: StateFlow<Boolean> = _botonActivado

    fun setMonedaUsuario(moneda: String) {
        _monedaInicialUsuario.value = moneda
    }

    fun setMonedaSeleccionada(moneda: String) {
        _monedaSeleccionada.value = moneda
    }

    fun subirCambiosAFirebase() {
        //FirestoreRepository.
    }

    fun cargarMonedaActualUsuario() {
        FirestoreRepository.getSimboloMoneda(
            userId,
            onResultado = { moneda ->
                _monedaInicialUsuario.value = moneda ?: ""
                _monedaSeleccionada.value = _monedaInicialUsuario.value
            }
        )
    }

    fun comprobarDiferenciasParaBoton() {
        if (_monedaInicialUsuario.value != _monedaSeleccionada.value) {
            _botonActivado.value = true
        } else {
            _botonActivado.value = false
        }
    }

}