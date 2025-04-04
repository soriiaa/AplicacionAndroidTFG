package com.example.investlearntfg.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.investlearntfg.ui.screens.pantallaLogin.LoginScreen

@Composable
fun NavGraph(navController: NavHostController){
    NavHost(
        navController = navController,
        startDestination = Destinations.LOGIN_SCREEN
    ) {
        composable(Destinations.LOGIN_SCREEN) {
            LoginScreen(navController)
        }
    }
}