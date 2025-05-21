package com.example.investlearntfg.data.model

data class PrecioCompania2(
    val c: Double,  // Precio actual
    val d: Double,  // Cambio neto (diferencia con el cierre anterior)
    val dp: Double  // Cambio porcentual
)
