package com.example.investlearntfg.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.investlearntfg.ui.screens.pantallaAccion.PantallaAccionScreen
import com.example.investlearntfg.ui.screens.pantallaBuscar.PantallaBuscarScreen
import com.example.investlearntfg.ui.screens.pantallaCompraAccion.PantallaCompraAccionScreen
import com.example.investlearntfg.ui.screens.pantallaConfiguracion.PantallaConfiguracionScreen
import com.example.investlearntfg.ui.screens.pantallaEditarPerfil.PantallaEditarPerfilScreen
import com.example.investlearntfg.ui.screens.pantallaInicio.PantallaInicialScreen
import com.example.investlearntfg.ui.screens.pantallaNoticias.PantallaNoticiasScreen
import com.example.investlearntfg.ui.screens.pantallaPerfil.PantallaPerfilScreen
import com.example.investlearntfg.ui.screens.pantallaVentaAccion.PantallaVentaAccionScreen

@Composable
fun MainAppNavGraph(
    navController: NavHostController,
    onLogout: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Destinations.PANTALLA_INICIAL_SCREEN
    ) {
        composable(Destinations.PANTALLA_INICIAL_SCREEN) {
            PantallaInicialScreen(navController, onLogout = onLogout)
        }

        composable(Destinations.PANTALLA_BUSCAR_SCREEN) {
            PantallaBuscarScreen(navController)
        }

        composable(Destinations.PANTALLA_NOTICIAS_SCREEN) {
            PantallaNoticiasScreen(navController)
        }

        composable(Destinations.PANTALLA_PERFIL_SCREEN) {
            PantallaPerfilScreen(navController)
        }

        composable("${Destinations.PANTALLA_ACCION_SCREEN}/{empresaJson}") {
            PantallaAccionScreen(navController)
        }

        composable("${Destinations.PANTALLA_COMPRA_ACCION_SCREEN}/{empresaJson}") {
            PantallaCompraAccionScreen(navController)
        }

        composable("${Destinations.PANTALLA_VENTA_ACCION_SCREEN}/{empresaJson}") {
            PantallaVentaAccionScreen(navController)
        }

        composable(Destinations.PANTALLA_CONFIGURACION_SCREEN) {
            PantallaConfiguracionScreen(navController, onLogout = onLogout)
        }

        composable(Destinations.PANTALLA_EDITAR_PERFIL_SCREEN) {
            PantallaEditarPerfilScreen(navController)
        }
    }
}