package com.example.investlearntfg.data.remote

import com.example.investlearntfg.data.model.DatosPerfilCompania
import com.example.investlearntfg.data.model.ListaEmpresasBusqueda
import com.example.investlearntfg.data.model.PrecioCompania
import retrofit2.http.GET
import retrofit2.http.Query

interface FinnHubApiService {

    // Endpoint: https://finnhub.io/api/v1/stock/profile2?symbol=AAPL&token=TU_API_KEY
    // https://finnhub.io/api/v1/stock/profile2?symbol=MSFT&token=d0e6uv1r01qv1dmld640d0e6uv1r01qv1dmld64g

    @GET("stock/profile2?")
    suspend fun getPerfilEmpresa(
        @Query("symbol") simbolo: String,
        @Query("token") apiKey: String
    ): DatosPerfilCompania

    // Endpoint: https://finnhub.io/api/v1/quote?symbol=AAPL&token=TU_API_KEY
    @GET("quote?")
    suspend fun getPrecioEmpresa(
        @Query("symbol") simbolo: String,
        @Query("token") apiKey: String
    ): PrecioCompania

    // https://finnhub.io/api/v1/search?q=Tesla&token=d0e6uv1r01qv1dmld640d0e6uv1r01qv1dmld64g
    @GET("search")
    suspend fun getListaEmpresasBusquedaNombre(
        @Query("q") query: String,
        @Query("token") apiKey: String
    ): ListaEmpresasBusqueda

}
