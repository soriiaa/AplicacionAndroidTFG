package com.example.investlearntfg.data.remote

import retrofit2.Call
import com.example.investlearntfg.data.model.CandleResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PolygonApiService {

    // https://api.polygon.io/v2/aggs/ticker/AAPL/range/1/day/2024-01-01/2024-05-01?apiKey=sWG_SGucG1vSuLM8pGvpUpQJOd953z45

    @GET("v2/aggs/ticker/{symbol}/range/{multiplier}/{timespan}/{from}/{to}")
    suspend fun getVelasAccion(
        @Path("symbol") symbol: String,
        @Path("multiplier") multiplier: Int,
        @Path("timespan") timespan: String, // "day", "week", "month"
        @Path("from") from: String,         // "YYYY-MM-DD"
        @Path("to") to: String,             // "YYYY-MM-DD"
        @Query("apiKey") apiKey: String
    ): CandleResponse

}