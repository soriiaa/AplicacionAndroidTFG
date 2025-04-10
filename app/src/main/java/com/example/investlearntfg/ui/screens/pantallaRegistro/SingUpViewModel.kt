package com.example.investlearntfg.ui.screens.pantallaRegistro

import androidx.lifecycle.ViewModel
import com.example.investlearntfg.data.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignUpViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    fun registrarUsuario(

        nombreIntroducido: String,
        apellidosIntroducidos: String,
        nicknameIntroducido: String,
        emailIntroducido: String,
        contrasenaIntroducida: String,
        confirmarContrasenaIntroducida: String,
        monedaIntroducida: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit

    ) {



        auth.createUserWithEmailAndPassword(emailIntroducido, contrasenaIntroducida)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Crear el usuario en Firestore
                    val usuario = Usuario(
                        nombre = nombreIntroducido,
                        apellidos = apellidosIntroducidos,
                        nickname = nicknameIntroducido,
                        correoElectronico = emailIntroducido,
                        contrasena = contrasenaIntroducida,
                        monedaPrincipal = monedaIntroducida,
                        fechaCreacion = System.currentTimeMillis(),
                        dineroEnCuenta = 5000.0, // Dinero inicial
                        totalGains = 0.0,
                        propertiesValue = 0,
                        purchasesMade = 0,
                        salesMade = 0,
                        userId = task.result?.user?.uid ?: ""
                    )

                    db.collection("users").document(usuario.userId)
                        .set(usuario)
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

    fun validarCampos() {

    }

}
