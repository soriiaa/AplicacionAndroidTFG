package com.example.investlearntfg.data.model

data class AccionPropiedad(
    val id: String = "",
    val ticker: String = "",
    val nombre: String = "",
    val fecha: String = "",
    val precioCompra: Double = 0.0,
    val precioActual: Double = 0.0,
    val fotoUrl: String = "",
    val unidades: Int = 0,
    val moneda: String = ""
)