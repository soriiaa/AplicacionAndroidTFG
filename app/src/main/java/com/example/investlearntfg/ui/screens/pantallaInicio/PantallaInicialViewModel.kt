package com.example.investlearntfg.ui.screens.pantallaInicio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.investlearntfg.data.model.EmpresaPreview
import com.example.investlearntfg.data.repository.FirestoreRepository
import com.example.investlearntfg.data.repository.PostRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PantallaInicialViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {

    private var userId = Firebase.auth.currentUser?.uid ?: ""

    // Aqui guardo el dinero que tiene el usuario en la cuenta
    private val _dineroEnCuenta = MutableStateFlow(0.0)
    val dineroEnCuenta: StateFlow<Double> = _dineroEnCuenta

    // Aqui guardo el simbolo de la moneda que usa el usuario
    private val _simboloMoneda = MutableStateFlow("")
    val simboloMoneda: StateFlow<String> = _simboloMoneda

    // Aqui guardo la ganancia que el usuario ha conseguido
    private val _gananciaUsuario = MutableStateFlow(0.00)
    val gananciaUsuario: StateFlow<Double> = _gananciaUsuario

    // Aqui guardo el total de transacciones que ha hecho el usuario en su cuenta
    private val _totalTransacciones = MutableStateFlow(0)
    val totalTransacciones: StateFlow<Int> = _totalTransacciones

    // Aqui guardo los tickers de las acciones
    private val _empresasFavoritas = MutableStateFlow<Set<String>>(emptySet())
    val empresasFavoritas: StateFlow<Set<String>> = _empresasFavoritas

    // Aqui guardo la lista de objetos de las acciones
    private val _listaEmpresasFavoritasPreview = MutableStateFlow<List<EmpresaPreview>>(emptyList())
    val listaEmpresasFavoritasPreview: StateFlow<List<EmpresaPreview>> = _listaEmpresasFavoritasPreview

    // Aqui guardo el estado de la carga de las acciones favoritas para luego mostrarlo por pantalla
    private val _cargandoAccionesFavoritas = MutableStateFlow(false)
    val cargandoAccionesFavoritas: StateFlow<Boolean> = _cargandoAccionesFavoritas

    // Con esta variable manejo el estado del dialogo de cierre de sesión
    private val _mostrarDialogoCerrarSesion = MutableStateFlow(false)
    val mostrarDialogoCerrarSesion: StateFlow<Boolean> = _mostrarDialogoCerrarSesion

    fun recargarPantalla() {
        cargarEstadisticas()
        cargarAccionesFavoritas()
    }

    fun cerrarSesion() {
        FirebaseAuth.getInstance().signOut()
    }

    fun cargarEstadisticas() {
        cargarDineroCuenta()
        cargarSimboloMonedaUsuario()
        cargarGananciaUsuario()
        cargarNumeroTransacciones()
    }

    fun cargarAccionesFavoritas() {
        FirestoreRepository.obtenerFavoritas(
            userId,
            onResultado = { listaTickers ->
                _empresasFavoritas.value = listaTickers.toSet()
                viewModelScope.launch {
                    _cargandoAccionesFavoritas.value = true
                    try {
                        val empresasPreview = listaTickers.mapNotNull { simbolo ->
                            try {
                                val perfil = postRepository.getPerfilEmpresaPostRepository(simbolo)
                                val precio = postRepository.getPrecioEmpresaPostRepository(simbolo)

                                val moneda = perfil.currency
                                val simboloMonetario = establecerSimboloMoneda(moneda)

                                EmpresaPreview(
                                    ticker = simbolo,
                                    nombre = perfil.name,
                                    logo = perfil.logo,
                                    precio = precio.c,
                                    simboloMoneda = simboloMonetario
                                )
                            } catch (e: Exception) {
                                println("Error al cargar datos de $simbolo: ${e.message}")
                                null
                            }
                        }
                        _listaEmpresasFavoritasPreview.value = empresasPreview
                    } catch (e: Exception) {
                        println("Error general al cargar empresas destacadas: ${e.message}")
                        _listaEmpresasFavoritasPreview.value = emptyList()
                    } finally {
                        _cargandoAccionesFavoritas.value = false
                    }
                }
            }
        )
    }

    fun alternarFavorito(empresa: EmpresaPreview) {
        val esFavorita = _empresasFavoritas.value.contains(empresa.ticker)

        if (esFavorita) {
            FirestoreRepository.eliminarFavorita(userId, empresa.ticker)
            _empresasFavoritas.value -= empresa.ticker
            cargarAccionesFavoritas()
        } else {
            FirestoreRepository.marcarComoFavorita(userId, empresa)
            _empresasFavoritas.value += empresa.ticker
            cargarAccionesFavoritas()
        }
    }

    private fun cargarNumeroTransacciones() {
        FirestoreRepository.getNumeroTransacciones(
            userId,
            onResultado = { numeroTransacciones ->
                _totalTransacciones.value = numeroTransacciones ?: 0
            }
        )
    }

    private fun cargarGananciaUsuario() {
        FirestoreRepository.getGananciasTotales(
            userId,
            onResultado = { ganancias ->
                _gananciaUsuario.value = ganancias ?: 0.00
            }
        )
    }

    private fun cargarDineroCuenta() {
        FirestoreRepository.getDineroCuenta(
            userId,
            onResultado = { dinero ->
                _dineroEnCuenta.value = dinero ?: 0.0
            }
        )
    }

    private fun cargarSimboloMonedaUsuario() {
        FirestoreRepository.getSimboloMoneda(
            userId,
            onResultado = { moneda ->
                _simboloMoneda.value = when (moneda) {
                    "Dólar estadounidense - $ - USD" -> "$"
                    "Euro - € - EUR" -> "€"
                    "Libra esterlina - £ - GBP" -> "£"
                    "Yen japonés - ¥ - JPY" -> "¥"
                    "Franco suizo - CHF - CHF" -> "CHF"
                    else -> ""
                }
            }
        )
    }

    private fun establecerSimboloMoneda(moneda: String): String {
        val simboloMonetario = when (moneda) {
            "USD" -> "$"
            "EUR" -> "€"
            "GBP" -> "£"
            "JPY" -> "¥"
            "CHF" -> "CHF"
            "CAD" -> "C$"
            "AUD" -> "A$"
            "CNY" -> "¥"
            "SEK" -> "kr"
            "NOK" -> "kr"
            "KRW" -> "₩"
            "INR" -> "₹"
            "BRL" -> "R$"
            "MXN" -> "$"
            "RUB" -> "₽"
            "HKD" -> "HK$"
            "NZD" -> "NZ$"
            "TRY" -> "₺"
            "IDR" -> "Rp"
            "ZAR" -> "R"
            else -> "?"
        }
        return simboloMonetario
    }

    fun setMostrarDialogoCerrarSesion(mostrar: Boolean) {
        _mostrarDialogoCerrarSesion.value = mostrar
    }

}