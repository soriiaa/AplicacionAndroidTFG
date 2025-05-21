package com.example.investlearntfg.data.remote

import com.example.investlearntfg.data.model.ExchangeRateResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ExchangeRateApiService {

    @GET("convert")
    suspend fun convertirMoneda(
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("amount") amount: Double,
        @Query("access_key") accessKey: String
    ): ExchangeRateResponse

}