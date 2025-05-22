package com.example.investlearntfg.data.repository

import com.example.investlearntfg.data.model.EmpresaPreview
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import java.util.UUID

object FirestoreRepository {

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

    fun getDineroCuenta(userId: String, onResultado: (Double?) -> Unit) {
        db.collection("usuarios")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val dinero = document.getDouble("dinero_cuenta")
                    onResultado(dinero)
                } else {
                    onResultado(null)
                }
            }
            .addOnFailureListener {
                onResultado(null)
            }
    }

    fun getSimboloMoneda(userId: String, onResultado: (String?) -> Unit) {
        db.collection("usuarios")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val moneda = document.getString("moneda_principal")
                    onResultado(moneda)
                } else {
                    onResultado(null)
                }
            }
            .addOnFailureListener {
                onResultado(null)
            }
    }

    fun guardarCompraEnFirestore(
        userId: String,
        ticker: String,
        precioCompra: Double,
        unidades: Int,
        monedaPagada: String,
        onExito: () -> Unit,
        onFallo: () -> Unit
    ) {
        val compraId = UUID.randomUUID().toString()
        val compra = hashMapOf(
            "id" to compraId,
            "ticker" to ticker,
            "precioCompra" to precioCompra,
            "unidades" to unidades,
            "moneda_pagada" to monedaPagada,
            "fecha" to FieldValue.serverTimestamp()
        )
        db.collection("usuarios")
            .document(userId)
            .collection("acciones_en_propiedad")
            .document(compraId)
            .set(compra)
            .addOnSuccessListener {
                onExito()
            }
            .addOnFailureListener {
                onFallo()
            }
    }

    fun restarDineroCuenta(
        userId: String,
        cantidadARestar: Double
    ) {
        val userDocRef = db.collection("usuarios").document(userId)

        userDocRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val dineroCuentaActual = documentSnapshot.getDouble("dinero_cuenta") ?: 0.0
                    val nuevoDineroCuenta = dineroCuentaActual - cantidadARestar

                    userDocRef.update("dinero_cuenta", nuevoDineroCuenta)
                        .addOnSuccessListener {

                        }
                        .addOnFailureListener {

                        }
                } else {

                }
            }
            .addOnFailureListener {

            }
    }


}