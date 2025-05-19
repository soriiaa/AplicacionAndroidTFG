package com.example.investlearntfg.ui.screens.pantallaAccion

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
            val tiempoActual = System.currentTimeMillis() / 1000
            val (resolution, from) = when (periodo) {
                "H" -> Pair("1", tiempoActual - TimeUnit.HOURS.toSeconds(1))
                "D" -> Pair("D", tiempoActual - TimeUnit.DAYS.toSeconds(1))
                "M" -> Pair("D", tiempoActual - TimeUnit.DAYS.toSeconds(30))
                else -> Pair("D", tiempoActual - TimeUnit.DAYS.toSeconds(7))
            }
            val to = tiempoActual

            val tickerEmpresa = _empresa.value?.ticker ?: throw IllegalStateException("Ticker no disponible")

            val valoresGrafico = postRepository.getVelasAccion(tickerEmpresa, resolution, from, to)
            _velas.value = valoresGrafico

        }
    }

}