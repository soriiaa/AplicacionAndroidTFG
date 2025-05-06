package com.example.investlearntfg.utils

import com.google.firebase.auth.FirebaseAuth

fun hayUsuarioLogeado(): Boolean {
    val usuarioActual = FirebaseAuth.getInstance().currentUser
    return usuarioActual != null
}