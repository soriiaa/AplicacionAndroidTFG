package com.example.investlearntfg.data.model


data class CandleResponse(
    val results: List<CandleData>?,
    val resultsCount: Int
)

data class CandleData(
    val t: Long,   // Marca de tiempo
    val o: Float,  // Apertura
    val h: Float,  // Máximo
    val l: Float,  // Mínimo
    val c: Float,  // Cierre
    val v: Long    // Volumen
)
