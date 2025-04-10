package com.example.investlearntfg.data.model

data class Usuario(
    val id: String,
    val nombre: String,
    val apellidos: String,
    val nickname: String,
    val correoElectronico: String,
    val contrasena: String,
    val monedaPrincipal: String,
    val fechaCreacion: Long,
    val dineroEnCuenta: Double,
    val gananciasTotales: Double,
    val valoresEnPropiedad: Int,
    val comprasRealizadas: Int,
    val ventasRealizadas: Int
)
