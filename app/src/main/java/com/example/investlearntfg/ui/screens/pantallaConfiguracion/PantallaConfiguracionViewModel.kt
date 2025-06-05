package com.example.investlearntfg.ui.screens.pantallaConfiguracion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.investlearntfg.data.repository.FirestoreRepository
import com.example.investlearntfg.data.repository.PostRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
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

    // Aqui almaceno el estado del dialogo que muestra como ha salido el guardado de la moneda
    private val _mostrarDialogoMoneda = MutableStateFlow("")
    val mostrarDialogoMoneda: StateFlow<String> = _mostrarDialogoMoneda

    // Aqui almaceno el estado de si muestro el dialogo del correo o no
    private val _mostrarDialogoCambioContrasena = MutableStateFlow(false)
    val mostrarDialogoCambioContrasena: StateFlow<Boolean> = _mostrarDialogoCambioContrasena

    // Aquí almaceno el resultado que devuelve la función que cambia la contraseña
    private val _mostrarDialogoCambioContrasenaResultado = MutableStateFlow("")
    val mostrarDialogoCambioContrasenaResultado: StateFlow<String> = _mostrarDialogoCambioContrasenaResultado

    fun ocultarDialogoResultadoCambioContrasena() {
        _mostrarDialogoCambioContrasenaResultado.value = ""
    }

    fun mostrarDialogoCambioContrasena() {
        _mostrarDialogoCambioContrasena.value = true
    }

    fun ocultarDialogoCambioContrasena() {
        _mostrarDialogoCambioContrasena.value = false
    }

    fun mostrarDialogoMoneda(tipo: String) {
        // tipo: "exito", "error" o ""
        _mostrarDialogoMoneda.value = tipo
    }

    fun ocultarDialogoMoneda() {
        _mostrarDialogoMoneda.value = ""
    }

    fun setMonedaSeleccionada(moneda: String) {
        _monedaSeleccionada.value = moneda
    }

    fun actualizarMonedaPrincipal() {
        FirestoreRepository.actualizarMonedaPrincipal(
            userId,
            _monedaSeleccionada.value,
            onExito = {
                viewModelScope.launch {
                    var dineroEnCuenta = 0.00
                    FirestoreRepository.getDineroCuenta(
                        userId = userId,
                        onResultado = { resultado ->
                            dineroEnCuenta = resultado ?: 0.00
                        }
                    )

                    val codigoMonedaAntiguo = obtenerCodigoMoneda(_monedaInicialUsuario.value)
                    val codigoMonedaNuevo = obtenerCodigoMoneda(_monedaSeleccionada.value)

                    val tipoCambio = (postRepository.convertirMonedaPostRepository(
                        codigoMonedaAntiguo,
                        codigoMonedaNuevo
                    )).result

                    val nuevoDineroEnCuenta = dineroEnCuenta * tipoCambio

                    FirestoreRepository.modificarDineroCuenta(
                        userId = userId,
                        nuevoValor = nuevoDineroEnCuenta,
                        onExito = {
                            mostrarDialogoMoneda("exito")
                            cargarMonedaActualUsuario()
                            comprobarDiferenciasParaBoton()
                            _botonActivado.value = false
                        },
                        onError = {
                            mostrarDialogoMoneda("error")
                        }
                    )
                }
            },
            onError = {
                mostrarDialogoMoneda("error")
            }
        )
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

    fun obtenerCodigoMoneda(opcionMoneda: String): String {
        val codigoMoneda = when (opcionMoneda) {
            "Dólar estadounidense - $ - USD" -> "USD"
            "Euro - € - EUR" -> "EUR"
            "Libra esterlina - £ - GBP" -> "GBP"
            "Yen japonés - ¥ - JPY" -> "JPY"
            "Franco suizo - CHF - CHF" -> "CHF"
            else -> ""
        }
        return codigoMoneda
    }

    fun cambiarContrasena() {
        _mostrarDialogoCambioContrasena.value = false

        FirestoreRepository.getEmailUsuario(
            userId = userId,
            onExito = { email ->
                FirestoreRepository.enviarCorreoRestablecerContrasena(
                    email = email,
                    onExito = {
                        _mostrarDialogoCambioContrasenaResultado.value = "exito"
                    },
                    onError = {
                        _mostrarDialogoCambioContrasenaResultado.value = "error"
                    }
                )
            },
            onError = {
                _mostrarDialogoCambioContrasenaResultado.value = "error"
            }
        )
    }

}