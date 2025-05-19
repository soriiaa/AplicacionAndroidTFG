package com.example.investlearntfg.data.model

data class CandleResponse (
    val c: List<Float>, // precios de cierre
    val h: List<Float>, // precios máximos
    val l: List<Float>, // precios mínimos
    val o: List<Float>, // precios de apertura
    val t: List<Long>,  // marcas de tiempo (formato Unix)
    val s: String       // estado (puede ser "ok" o "no_data")
)