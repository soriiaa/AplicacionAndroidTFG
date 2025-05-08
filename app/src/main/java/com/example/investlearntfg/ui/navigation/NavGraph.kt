package com.example.investlearntfg.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.investlearntfg.ui.screens.pantallaAccion.PantallaAccionScreen
import com.example.investlearntfg.ui.screens.pantallaBuscar.PantallaBuscarScreen
import com.example.investlearntfg.ui.screens.pantallaInicio.PantallaInicialScreen
import com.example.investlearntfg.ui.screens.pantallaLogin.LoginScreen
import com.example.investlearntfg.ui.screens.pantallaPerfil.PantallaPerfilScreen
import com.example.investlearntfg.ui.screens.pantallaRecuperarContrasena.RecuperarContrasenaScreen
import com.example.investlearntfg.ui.screens.pantallaRegistro.SingUpScreen

@Composable
fun NavGraph(navController: NavHostController, startDestination: String) {

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Destinations.LOGIN_SCREEN) {
            LoginScreen(navController)
        }

        composable(Destinations.SINGUP_SCREEN) {
            SingUpScreen(navController)
        }

        composable(Destinations.RECUPERAR_CONTRASENA_SCREEN) {
            RecuperarContrasenaScreen(navController)
        }

        composable(Destinations.PANTALLA_INICIAL_SCREEN) {
            PantallaInicialScreen(navController)
        }

        composable(Destinations.PANTALLA_BUSCAR_SCREEN) {
            PantallaBuscarScreen(navController)
        }

        composable(Destinations.PANTALLA_PERFIL_SCREEN) {
            PantallaPerfilScreen(navController)
        }

        composable(Destinations.PANTALLA_ACCION_SCREEN) {
            PantallaAccionScreen(navController)
        }

    }

}