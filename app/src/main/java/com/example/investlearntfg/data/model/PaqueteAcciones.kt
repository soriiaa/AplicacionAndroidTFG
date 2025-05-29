package com.example.investlearntfg.data.model

import com.google.firebase.Timestamp

data class PaqueteAcciones(
    val fecha: Timestamp? = null,
    val id: String = "",
    val precioCompra: Double = 0.0,
    val ticker: String = "",
    val unidades: Int = 0
)
