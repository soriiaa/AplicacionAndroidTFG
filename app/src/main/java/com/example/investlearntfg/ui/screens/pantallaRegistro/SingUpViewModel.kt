package com.example.investlearntfg.ui.screens.pantallaRegistro

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

/*


class SignUpViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    // Función para crear un nuevo usuario
    fun registrarUsuario(
        name: String,
        surname: String,
        nickname: String,
        email: String,
        password: String,
        currency: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Crear el usuario en Firestore
                    val user = User(
                        name = name,
                        surname = surname,
                        nickname = nickname,
                        email = email,
                        password = password,
                        mainCurrency = currency,
                        accountBalance = 5000.0, // Dinero inicial
                        creationDate = System.currentTimeMillis(),
                        totalGains = 0.0,
                        propertiesValue = 0,
                        purchasesMade = 0,
                        salesMade = 0,
                        userId = task.result?.user?.uid ?: ""
                    )

                    db.collection("users").document(user.userId)
                        .set(user)
                        .addOnSuccessListener {
                            onSuccess()
                        }
                        .addOnFailureListener { exception ->
                            onFailure(exception.message ?: "Error al guardar el usuario")
                        }
                } else {
                    onFailure(task.exception?.message ?: "Error al registrar el usuario")
                }
            }
    }
}

 */
