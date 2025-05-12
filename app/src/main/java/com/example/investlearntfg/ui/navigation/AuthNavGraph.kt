package com.example.investlearntfg.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.investlearntfg.ui.screens.pantallaLogin.LoginScreen
import com.example.investlearntfg.ui.screens.pantallaRecuperarContrasena.RecuperarContrasenaScreen
import com.example.investlearntfg.ui.screens.pantallaRegistro.SignUpScreen

@Composable
fun AuthNavGraph(
    navController: NavHostController,
    onLoginSuccess: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Destinations.LOGIN_SCREEN
    ) {
        composable(Destinations.LOGIN_SCREEN) {
            LoginScreen(navController, onLoginSuccess = onLoginSuccess)
        }

        composable(Destinations.SINGUP_SCREEN) {
            SignUpScreen(navController)
        }

        composable(Destinations.RECUPERAR_CONTRASENA_SCREEN) {
            RecuperarContrasenaScreen(navController)
        }
    }
}