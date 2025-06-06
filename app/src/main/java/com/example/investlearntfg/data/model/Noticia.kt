package com.example.investlearntfg.data.model

data class Noticia(
    val category: String,
    val datetime: Long,
    val headline: String,
    val id: Int,
    val image: String,
    val related: String,
    val source: String,
    val summary: String,
    val url: String
)