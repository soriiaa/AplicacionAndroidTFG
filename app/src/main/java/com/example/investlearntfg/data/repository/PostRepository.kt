package com.example.investlearntfg.data.repository

import com.example.investlearntfg.data.model.ApiKey
import com.example.investlearntfg.data.model.DatosPerfilCompania
import com.example.investlearntfg.data.model.PrecioCompania
import com.example.investlearntfg.data.remote.FinnHubApiService
import javax.inject.Inject

class PostRepository @Inject constructor(
    private val apiKey: ApiKey,
    private val finnHubApiService: FinnHubApiService
) {

    suspend fun getPerfilEmpresaPostRepository(simbolo: String): DatosPerfilCompania {
        return finnHubApiService.getPerfilEmpresa(simbolo, apiKey.value)
    }

    suspend fun getPrecioEmpresaPostRepository(simbolo: String): PrecioCompania {
        return finnHubApiService.getPrecioEmpresa(simbolo, apiKey.value)
    }

}