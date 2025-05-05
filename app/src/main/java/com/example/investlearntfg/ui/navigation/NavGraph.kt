package com.example.investlearntfg.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.investlearntfg.ui.screens.pantallaInicio.PantallaInicialScreen
import com.example.investlearntfg.ui.screens.pantallaLogin.LoginScreen
import com.example.investlearntfg.ui.screens.pantallaRegistro.SingUpScreen

@Composable
fun NavGraph(navController: NavHostController){

    NavHost(
        navController = navController,
        startDestination = Destinations.LOGIN_SCREEN
    ) {
        composable(Destinations.LOGIN_SCREEN) {
            LoginScreen(navController)
        }

        composable(Destinations.SINGUP_SCREEN) {
            SingUpScreen(navController)
        }

        composable(Destinations.PANTALLA_INICIAL_SCREEN) {
            PantallaInicialScreen(navController)
        }
    }

}