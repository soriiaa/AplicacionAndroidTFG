package com.example.investlearntfg.data.repository

import android.util.Log
import com.example.investlearntfg.data.model.ApiKey
import com.example.investlearntfg.data.model.DatosPerfilCompania
import com.example.investlearntfg.data.model.ListaEmpresasBusqueda
import com.example.investlearntfg.data.model.PrecioCompania
import com.example.investlearntfg.data.remote.FinnHubApiService
import javax.inject.Inject

class PostRepository @Inject constructor(
    private val apiKey: ApiKey,
    private val apiService: FinnHubApiService
) {

    suspend fun getPerfilEmpresaPostRepository(simbolo: String): DatosPerfilCompania {
        return apiService.getPerfilEmpresa(simbolo, apiKey.value)
    }

    suspend fun getPrecioEmpresaPostRepository(simbolo: String): PrecioCompania {
        return apiService.getPrecioEmpresa(simbolo, apiKey.value)
    }

    suspend fun buscarEmpresasPorNombre(nombre: String): ListaEmpresasBusqueda {
        Log.d("POST_REPOSITORY", "Buscando empresas con: $nombre")
        return apiService.getListaEmpresasBusquedaNombre(nombre, apiKey.value)
    }

}