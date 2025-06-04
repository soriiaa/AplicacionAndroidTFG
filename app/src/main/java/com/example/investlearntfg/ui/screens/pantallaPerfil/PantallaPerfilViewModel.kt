package com.example.investlearntfg.ui.screens.pantallaPerfil

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.investlearntfg.data.model.AccionPropiedad
import com.example.investlearntfg.data.model.Transaccion
import com.example.investlearntfg.data.repository.FirestoreRepository
import com.example.investlearntfg.data.repository.PostRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class PantallaPerfilViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {

    private var userId = Firebase.auth.currentUser?.uid ?: ""

    private val _nickname = MutableStateFlow<String>("")
    val nickname: StateFlow<String> = _nickname

    // Aqui guardo la lista de las acciones en propiedad del usuario
    private val _accionesEnPropiedad = MutableStateFlow<List<AccionPropiedad>>(emptyList())
    val accionesEnPropiedad: StateFlow<List<AccionPropiedad>> = _accionesEnPropiedad

    // Mediante este boleeano controlo el estado de la carga de las acciones
    private val _cargandoAccionesEnPropiedad = MutableStateFlow(false)
    val cargandoAccionesEnPropiedad: StateFlow<Boolean> = _cargandoAccionesEnPropiedad

    // Aqui almaceno todas las transacciones que devuelve el endpoint de la base de datos de firebase
    private val _historialTransacciones = MutableStateFlow<List<Transaccion>>(emptyList())
    val historialTransacciones: StateFlow<List<Transaccion>> = _historialTransacciones

    // Aqui almaceno el estado de la carga del histórico de las acciones del usuario
    private val _cargandoHistorialAcciones = MutableStateFlow(false)
    val cargandoHistorialAcciones: StateFlow<Boolean> = _cargandoHistorialAcciones

    init {
        cargarNickname()
        cargarAccionesEnPropiedad()
        cargarHistorialTransacciones()
    }

    fun cargarHistorialTransacciones() {
        viewModelScope.launch {
            _cargandoHistorialAcciones.value = true
            try {
                val historial = FirestoreRepository.cargarHistorialTransacciones(usuarioId = userId)
                _historialTransacciones.value = historial
                historial.forEachIndexed { index, transaccion ->
                    Log.d("HistorialTransacciones", "Transacción #$index: $transaccion")
                }
            } catch (e: Exception) {
                Log.e("HistorialTransacciones", "Error al cargar historial: ${e.message}", e)
                _historialTransacciones.value = emptyList()
            } finally {
                _cargandoHistorialAcciones.value = false
            }
            _cargandoHistorialAcciones.value = false
        }
    }

    fun cargarAccionesEnPropiedad() {
        viewModelScope.launch {
            _cargandoAccionesEnPropiedad.value = true
            val documentos = FirestoreRepository.getDocumentosAccionesPropiedad(userId)
            val accionesEnPropiedad = documentos.map { doc ->

                val ticker = doc.getString("ticker") ?: ""

                var perfilAccionName = ""
                var perfilAccionLogo = ""
                var perfilAccionMoneda = ""
                var precioActual = 0.0

                try {
                    val perfilAccion = postRepository.getPerfilEmpresaPostRepository(ticker)
                    perfilAccionName = perfilAccion.name
                    perfilAccionLogo = perfilAccion.logo
                    perfilAccionMoneda = perfilAccion.currency

                    val precioAccion = postRepository.getPrecioEmpresaPostRepository(ticker)
                    precioActual = precioAccion.c
                } catch (e: retrofit2.HttpException) {
                    _cargandoAccionesEnPropiedad.value = false
                    if (e.code() == 429) {
                        Log.e("API_ERROR", "Demasiadas solicitudes (429). Intenta más tarde.")
                    } else {
                        Log.e("API_ERROR", "Error HTTP: ${e.code()}")
                    }
                } catch (e: Exception) {
                    _cargandoAccionesEnPropiedad.value = false
                    Log.e("API_ERROR", "Error inesperado: ${e.localizedMessage}")
                }

                val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val fechaTimestamp = doc.getTimestamp("fecha")
                val fecha = fechaTimestamp?.toDate()?.let { formatter.format(it) } ?: ""

                AccionPropiedad(
                    nombre = perfilAccionName,
                    precioActual = precioActual,
                    fotoUrl = perfilAccionLogo,
                    fecha = fecha,
                    id = doc.getString("id") ?: "",
                    precioCompra = doc.getDouble("precioCompra") ?: 0.0,
                    ticker = ticker,
                    unidades = doc.getLong("unidades")?.toInt() ?: 0,
                    moneda = perfilAccionMoneda
                )
            }

            _accionesEnPropiedad.value = accionesEnPropiedad
            _cargandoAccionesEnPropiedad.value = true
        }
    }

    private fun cargarNickname() {
        viewModelScope.launch {
            _nickname.value = FirestoreRepository.obtenerNickname(userId)
        }
    }

    fun obtenerSimboloMoneda(codigo: String): String {
        return when (codigo) {
            "USD" -> "$"
            "EUR" -> "€"
            "GBP" -> "£"
            "JPY" -> "¥"
            "CHF" -> "CHF"
            "CAD" -> "C$"
            "AUD" -> "A$"
            "SEK" -> "kr"
            "KRW" -> "₩"
            "INR" -> "₹"
            "BRL" -> "R$"
            "RUB" -> "₽"
            "HKD" -> "HK$"
            "NZD" -> "NZ$"
            "TRY" -> "₺"
            "IDR" -> "Rp"
            "ZAR" -> "R"
            else -> "?"
        }
    }

}