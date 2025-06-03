package com.example.investlearntfg.data.repository

import android.util.Log
import com.example.investlearntfg.data.model.EmpresaPreview
import com.example.investlearntfg.data.model.PaqueteAcciones
import com.example.investlearntfg.data.model.Transaccion
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import java.util.UUID

object FirestoreRepository {

    private val db = Firebase.firestore
    private val auth = FirebaseAuth.getInstance()

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

    suspend fun verificarCorreoExistente(correo: String): Boolean {
        return try {
            val resultado = db.collection("usuarios")
                .whereEqualTo("email", correo)
                .get()
                .await()
            !resultado.isEmpty
        } catch (e: Exception) {
            false
        }
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

    suspend fun enviarCorreoVerificacion(): Boolean {
        return try {
            auth.currentUser?.sendEmailVerification()?.await()
            true
        } catch (e: Exception) {
            false
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
        onExito: () -> Unit,
        onFallo: () -> Unit
    ) {
        val compraId = UUID.randomUUID().toString()
        val compra = hashMapOf(
            "id" to compraId,
            "ticker" to ticker,
            "precioCompra" to precioCompra,
            "unidades" to unidades,
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

    fun incrementarComprasRealizadas(usuarioId: String) {
        val usuarioRef = db.collection("usuarios").document(usuarioId)

        usuarioRef.update("compras_realizadas", FieldValue.increment(1))
            .addOnSuccessListener {
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error actualizando compras_realizadas", e)
            }
    }

    fun incrementarVentasRealizadas(usuarioId: String) {
        val usuarioRef = db.collection("usuarios").document(usuarioId)

        usuarioRef.update("ventas_realizadas", FieldValue.increment(1))
            .addOnSuccessListener {
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error actualizando ventas_realizadas", e)
            }
    }

    fun incrementarGananciasTotales(usuarioId: String, ganancias: Double) {
        val usuarioRef = db.collection("usuarios").document(usuarioId)

        usuarioRef.update("ganancias_totales", FieldValue.increment(ganancias))
            .addOnSuccessListener {
                Log.d("Firestore", "Ganancias totales incrementadas en $ganancias")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error actualizando ganancias_totales", e)
            }
    }


    suspend fun obtenerNickname(userId: String): String {
        return try {
            val doc = db.collection("usuarios").document(userId).get().await()
            if (doc.exists()) {
                doc.getString("nickname") as String
            } else {
                throw Exception("Documento no existe")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    suspend fun getDocumentosAccionesPropiedad(userId: String): List<DocumentSnapshot> {
        return try {
            val snapshot = db
                .collection("usuarios")
                .document(userId)
                .collection("acciones_en_propiedad")
                .get()
                .await()

            snapshot.documents
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getAccionCompradaBoolean(userId: String, simbolo: String): Boolean {
        return try {
            val querySnapshot = db.collection("usuarios")
                .document(userId)
                .collection("acciones_en_propiedad")
                .whereEqualTo("ticker", simbolo)
                .limit(1)
                .get()
                .await()

            !querySnapshot.isEmpty
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun obtenerPaquetesPropiedadPorTicker(userId: String, ticker: String): List<PaqueteAcciones> {

        val resultado = mutableListOf<PaqueteAcciones>()

        try {
            val snapshot = db
                .collection("usuarios")
                .document(userId)
                .collection("acciones_en_propiedad")
                .whereEqualTo("ticker", ticker)
                .get()
                .await()

            for (document in snapshot.documents) {
                document.toObject(PaqueteAcciones::class.java)?.let { resultado.add(it) }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return resultado
    }

    fun subirTransaccion(
        userId: String,
        ticker: String,
        tipoTransaccion: String,
        precioTransaccion: Double,
        unidades: Int,
        fotoAccion: String,
        onSuccess: () -> Unit = {},
        onFailure: (Exception) -> Unit = {}
    ) {

        val transaccion = hashMapOf(
            "ticker" to ticker,
            "tipo_transaccion" to tipoTransaccion,
            "precio_transaccion" to precioTransaccion,
            "unidades" to unidades,
            "foto_accion" to fotoAccion,
            "fecha" to Timestamp.now()
        )

        db.collection("usuarios")
            .document(userId)
            .collection("historial_transacciones")
            .add(transaccion)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun eliminarAccionEnPropiedad(usuarioId: String, paqueteId: String) {
        val accionRef = db.collection("usuarios")
            .document(usuarioId)
            .collection("acciones_en_propiedad")
            .document(paqueteId)

        accionRef.delete()
            .addOnSuccessListener {
                Log.d("Firestore", "Acción con ID $paqueteId eliminada correctamente.")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error al eliminar la acción con ID $paqueteId", e)
            }
    }

    suspend fun cargarHistorialTransacciones(usuarioId: String): List<Transaccion> {
        return try {
            val snapshot = db.collection("usuarios")
                .document(usuarioId)
                .collection("historial_transacciones")
                .get()
                .await()

            snapshot.documents.mapNotNull { it.toObject(Transaccion::class.java) }
        } catch (e: Exception) {
            Log.e("Firestore", "Error al obtener historial de transacciones", e)
            emptyList()
        }
    }

    fun getNumeroAccionesEnPropiedad(userId: String, onResultado: (Int) -> Unit) {
        db.collection("usuarios")
            .document(userId)
            .collection("acciones_en_propiedad")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val numeroAcciones = querySnapshot.size()
                onResultado(numeroAcciones)
            }
            .addOnFailureListener {
                onResultado(0)
            }
    }

    fun getNumeroTransacciones(userId: String, onResultado: (Int?) -> Unit) {
        db.collection("usuarios")
            .document(userId)
            .collection("historial_transacciones")
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot != null) {
                    val numeroTransacciones = querySnapshot.size()
                    onResultado(numeroTransacciones)
                } else {
                    onResultado(null)
                }
            }
            .addOnFailureListener {
                onResultado(null)
            }
    }

    fun getNicknameNombreApellidoUsuario(
        userId: String,
        onResultado: (nickname: String?, nombre: String?, apellidos: String?) -> Unit,
        onError: (Exception) -> Unit
    ) {
        db.collection("usuarios")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val nickname = document.getString("nickname")
                    val nombre = document.getString("nombre")
                    val apellidos = document.getString("apellidos")
                    onResultado(nickname, nombre, apellidos)
                } else {
                    onResultado(null, null, null)
                }
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }

    fun setDatosNuevosUsuario(
        userId: String,
        nickname: String,
        nombre: String,
        apellidos: String,
        onResultado: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        val datosActualizados = mapOf(
            "nickname" to nickname,
            "nombre" to nombre,
            "apellidos" to apellidos
        )

        db.collection("usuarios")
            .document(userId)
            .update(datosActualizados)
            .addOnSuccessListener { onResultado() }
            .addOnFailureListener { exception -> onError(exception) }
    }


}