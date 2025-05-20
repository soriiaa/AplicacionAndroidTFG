package com.example.investlearntfg.data.repository

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

object UsuarioRepository {

    private val db = Firebase.firestore

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

}