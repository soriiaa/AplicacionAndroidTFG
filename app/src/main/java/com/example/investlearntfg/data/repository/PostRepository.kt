package com.example.investlearntfg.data.repository

import android.util.Log
import com.example.investlearntfg.data.model.ApiKey
import com.example.investlearntfg.data.model.CandleResponse
import com.example.investlearntfg.data.model.DatosPerfilCompania
import com.example.investlearntfg.data.model.ListaEmpresasBusqueda
import com.example.investlearntfg.data.model.PrecioCompania
import com.example.investlearntfg.data.remote.FinnHubApiService
import com.example.investlearntfg.data.remote.PolygonApiService
import retrofit2.Call
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Named

class PostRepository @Inject constructor(
    @Named("finnhub") private val finnhubApiKey: ApiKey,
    @Named("polygon") private val polygonApiKey: ApiKey,
    private val finnHubApiService: FinnHubApiService,
    private val polygonApiService: PolygonApiService
) {

    suspend fun getPerfilEmpresaPostRepository(simbolo: String): DatosPerfilCompania {
        return finnHubApiService.getPerfilEmpresa(simbolo, finnhubApiKey.value)
    }

    suspend fun getPrecioEmpresaPostRepository(simbolo: String): PrecioCompania {
        return finnHubApiService.getPrecioEmpresa(simbolo, finnhubApiKey.value)
    }

    suspend fun buscarEmpresasPorNombre(nombre: String): ListaEmpresasBusqueda {
        Log.d("POST_REPOSITORY", "Buscando empresas con: $nombre")
        return finnHubApiService.getListaEmpresasBusquedaNombre(nombre, finnhubApiKey.value)
    }

    suspend fun getVelasAccion(symbol: String, multiplier: Int, timespan: String, fromDate: String, toDate: String): CandleResponse {
        return polygonApiService.getVelasAccion(symbol, multiplier, timespan, fromDate, toDate, polygonApiKey.value)
    }

}