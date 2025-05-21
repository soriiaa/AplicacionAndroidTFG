package com.example.investlearntfg.data.repository

import com.example.investlearntfg.data.model.EmpresaPreview
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

object FavoritosRepository {

    private val db = Firebase.firestore

    fun marcarComoFavorita(userId: String, empresa: EmpresaPreview) {
        db.collection("usuarios")
            .document(userId)
            .collection("favoritos")
            .document(empresa.ticker)
            .set(empresa)
    }

    fun eliminarFavorita(userId: String, ticker: String) {
        db.collection("usuarios")
            .document(userId)
            .collection("favoritos")
            .document(ticker)
            .delete()
    }

    fun obtenerFavoritas(userId: String, onResultado: (List<String>) -> Unit) {
        db.collection("usuarios")
            .document(userId)
            .collection("favoritos")
            .get()
            .addOnSuccessListener { result ->
                val tickersFavoritos = result.map { it.id }
                onResultado(tickersFavoritos)
            }
    }

    fun esFavorito(userId: String, ticker: String, onResultado: (Boolean) -> Unit) {
        db.collection("usuarios")
            .document(userId)
            .collection("favoritos")
            .document(ticker)
            .get()
            .addOnSuccessListener { doc ->
                onResultado(doc.exists())
            }
            .addOnFailureListener {
                onResultado(false)
            }
    }
}