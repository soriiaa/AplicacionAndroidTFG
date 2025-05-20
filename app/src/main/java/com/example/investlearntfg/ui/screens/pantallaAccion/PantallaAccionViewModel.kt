package com.example.investlearntfg.ui.screens.pantallaAccion

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.investlearntfg.data.model.CandleResponse
import com.example.investlearntfg.data.model.EmpresaPreview
import com.example.investlearntfg.data.repository.PostRepository
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class PantallaAccionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val postRepository: PostRepository
) : ViewModel() {

    private val _empresa = MutableStateFlow<EmpresaPreview?>(null)
    val empresa: StateFlow<EmpresaPreview?> = _empresa

    private val _esEmpresaFavorita = MutableStateFlow(false)
    val esEmpresaFavorita: StateFlow<Boolean> = _esEmpresaFavorita

    private val _velas = MutableStateFlow<CandleResponse?>(null)
    val velas: StateFlow<CandleResponse?> = _velas

    fun alternarEsFavorito() {
        _esEmpresaFavorita.value = !_esEmpresaFavorita.value
    }

    init {
        val empresaJson = savedStateHandle.get<String>("empresaJson")
        val empresaDeserializada = empresaJson?.let {
            Gson().fromJson(it, EmpresaPreview::class.java)
        }
        _empresa.value = empresaDeserializada

        traerVelasAccion("D")
    }

    fun traerVelasAccion(periodo: String) {
        viewModelScope.launch {
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

                val tickerEmpresa = _empresa.value?.ticker ?: throw IllegalStateException("Ticker no disponible")

                Log.d("PARAMETROS_API", "ticker: $tickerEmpresa, multiplier: $multiplier, resolution: $resolution, from: $from, to: $to")
                val valoresGrafico: CandleResponse = postRepository.getVelasAccion(tickerEmpresa, multiplier, resolution, from.toString(), to.toString())

                _velas.value = valoresGrafico

                Log.d("VELAS_RESPUESTA_API", "Respuesta: ${_velas.value.toString()}")

            } catch (e: Exception) {
                e.printStackTrace()
                _velas.value = null
            }
        }
    }



}