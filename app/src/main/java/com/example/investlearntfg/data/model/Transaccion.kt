package com.example.investlearntfg.data.model

import com.google.firebase.Timestamp

data class Transaccion(
    val ticker: String = "",
    val tipo_transaccion: String = "",
    val unidades: Int = 0,
    val foto_accion: String = "",
    val fecha: Timestamp? = null,
    val precio_transaccion: Double = 0.0
)
