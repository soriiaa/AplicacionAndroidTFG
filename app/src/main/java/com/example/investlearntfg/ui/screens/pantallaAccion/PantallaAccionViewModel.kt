package com.example.investlearntfg.ui.screens.pantallaAccion

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.investlearntfg.data.model.CandleResponse
import com.example.investlearntfg.data.model.CompanyProfile2Response
import com.example.investlearntfg.data.model.EmpresaPreview
import com.example.investlearntfg.data.model.PrecioCompania2
import com.example.investlearntfg.data.repository.FirestoreRepository
import com.example.investlearntfg.data.repository.PostRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class PantallaAccionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val postRepository: PostRepository
) : ViewModel() {

    private var userId = Firebase.auth.currentUser?.uid ?: ""

    private val _empresa = MutableStateFlow<EmpresaPreview?>(null)
    val empresa: StateFlow<EmpresaPreview?> = _empresa

    private val _perfilCompletoEmpresa = MutableStateFlow<CompanyProfile2Response?>(null)
    val perfilCompletoEmpresa: StateFlow<CompanyProfile2Response?> = _perfilCompletoEmpresa

    private val _esEmpresaFavorita = MutableStateFlow(false)
    val esEmpresaFavorita: StateFlow<Boolean> = _esEmpresaFavorita

    private val _velas = MutableStateFlow<CandleResponse?>(null)
    val velas: StateFlow<CandleResponse?> = _velas

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _dineroCuenta = MutableStateFlow<Double?>(null)
    val dineroCuenta: StateFlow<Double?> = _dineroCuenta

    // Precio y cambio de la accion actual
    private val _precioCompania2 = MutableStateFlow<PrecioCompania2?>(null)
    val precioCompania2: StateFlow<PrecioCompania2?> = _precioCompania2

    private val _simboloMonedaUsuario = MutableStateFlow<String?>(null)
    val simboloMonedaUsuario: StateFlow<String?> = _simboloMonedaUsuario

    init {
        cargarInformacionPreviaEmpresa(savedStateHandle)
        cargarDineroCuenta(userId)
        cargarSimboloMoneda(userId)
        cargarDatosEmpresaCompletos()
        cargarEsFavorito()
        traerVelasAccion("D")
    }

    suspend fun puedeVender(): Boolean {
        _empresa.value?.let {
            try {
                return FirestoreRepository.getAccionCompradaBoolean(userId, it.ticker)
            } catch (e: Exception) {
                Log.d("Error", "")
            }
        }
        return false
    }

    fun cargarPrecioAccion() {
        _empresa.value?.let { empresa ->
            viewModelScope.launch {
                _precioCompania2.value = postRepository.getPrecioEmpresa2PostRepository(empresa.ticker)
            }
        }
    }

    private fun cargarInformacionPreviaEmpresa(savedStateHandle: SavedStateHandle) {
        try {
            val empresaJson = savedStateHandle.get<String>("empresaJson")
            val empresaDeserializada = empresaJson?.let {
                Gson().fromJson(it, EmpresaPreview::class.java)
            }
            _empresa.value = empresaDeserializada
        } catch (e: Exception) { }
    }

    fun alternarFavorito() {
        if (_esEmpresaFavorita.value) {
            _empresa.value?.let { FirestoreRepository.eliminarFavorita(userId, it.ticker) }
            _esEmpresaFavorita.value = false
        } else {
            _empresa.value?.let { FirestoreRepository.marcarComoFavorita(userId, it) }
            _esEmpresaFavorita.value = true
        }
    }

    fun cargarEsFavorito() {
        _empresa.value?.let { empresa ->
            FirestoreRepository.esFavorito(userId, empresa.ticker) { esFavorito ->
                _esEmpresaFavorita.value = esFavorito
            }
        }
    }

    fun cargarDatosEmpresaCompletos() {
        viewModelScope.launch {
            try {
                val respuestaApi = _empresa.value?.let {
                    postRepository.getPerfilEmpresa2PostRepository(it.ticker)
                }
                _perfilCompletoEmpresa.value = respuestaApi
            } catch (e: Exception) {
                Log.e("PerfilEmpresa", "Error al obtener perfil: ${e.message}")
            }
        }
    }

    fun cargarSimboloMoneda(userId: String) {
        FirestoreRepository.getSimboloMoneda(userId) { simboloMoneda ->
            _simboloMonedaUsuario.value = when (simboloMoneda) {
                "Dólar estadounidense - $ - USD" -> "$"
                "Euro - € - EUR" -> "€"
                "Libra esterlina - £ - GBP" -> "£"
                "Yen japonés - ¥ - JPY" -> "¥"
                "Franco suizo - CHF - CHF" -> "CHF"
                else -> ""
            }
        }
    }

    fun cargarDineroCuenta(userId: String) {
        FirestoreRepository.getDineroCuenta(userId) { dinero ->
            _dineroCuenta.value = dinero
        }
    }

    fun traerVelasAccion(periodo: String) {
        viewModelScope.launch {

            _isLoading.value = true
            try {
                val calendar = Calendar.getInstance()
                val to = calendar.timeInMillis / 1000  // tiempo actual en segundos

                val resolution: String
                var multiplier: Int = 1

                when (periodo) {
                    "D" -> {
                        resolution = "minute"
                        multiplier = 30
                        calendar.add(Calendar.DAY_OF_YEAR, -1)
                    }

                    "S" -> {
                        resolution = "hour"
                        multiplier = 4
                        calendar.add(Calendar.DAY_OF_YEAR, -7)
                    }

                    "M" -> {
                        resolution = "day"
                        calendar.add(Calendar.MONTH, -1)
                    }

                    else -> {
                        resolution = "day"
                        calendar.add(Calendar.DAY_OF_YEAR, -7)
                    }
                }

                val from = calendar.timeInMillis / 1000

                val tickerEmpresa =
                    _empresa.value?.ticker ?: throw IllegalStateException("Ticker no disponible")

                Log.d(
                    "PARAMETROS_API",
                    "ticker: $tickerEmpresa, multiplier: $multiplier, resolution: $resolution, from: $from, to: $to"
                )
                val valoresGrafico: CandleResponse = postRepository.getVelasAccionPostRepository(
                    tickerEmpresa,
                    multiplier,
                    resolution,
                    from.toString(),
                    to.toString()
                )

                _velas.value = valoresGrafico

                Log.d("VELAS_RESPUESTA_API", "Respuesta: ${_velas.value.toString()}")
            } catch (e: Exception) {
                _velas.value = null
            } finally {
                _isLoading.value = false
            }

        }
    }


}