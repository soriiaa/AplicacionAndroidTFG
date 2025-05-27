package com.example.investlearntfg.ui.screens.pantallaRegistro

import androidx.lifecycle.ViewModel
import com.example.investlearntfg.data.repository.PostRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    suspend fun verificarCorreoExistente(correo: String): Boolean {

        val usuarios = db.collection("usuarios")

        val resultado = usuarios
            .whereEqualTo("email", correo)
            .get()
            .await()

        return !resultado.isEmpty
    }

    suspend fun registrarUsuario(

        nombre: String,
        apellidos: String,
        nickname: String,
        correo: String,
        contrasena: String,
        moneda: String

    ): Boolean {

        return try {

            val resultado = auth.createUserWithEmailAndPassword(correo, contrasena).await()
            val uid = resultado.user?.uid

            if (uid != null) {
                val nuevoUsuario = hashMapOf(
                    "uid" to uid,
                    "nombre" to nombre,
                    "apellidos" to apellidos,
                    "nickname" to nickname,
                    "email" to correo,
                    "moneda_principal" to moneda,
                    "compras_realizadas" to 0,
                    "ventas_realizadas" to 0,
                    "dinero_cuenta" to 5000,
                    "ganancias_totales" to 0,
                    "fecha_creacion" to System.currentTimeMillis()
                )

                db.collection("usuarios").document(uid).set(nuevoUsuario).await()
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

}
