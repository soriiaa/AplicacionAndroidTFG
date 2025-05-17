package com.example.investlearntfg.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.investlearntfg.data.model.EmpresaPreview
import com.example.investlearntfg.ui.screens.pantallaAccion.PantallaAccionScreen
import com.example.investlearntfg.ui.screens.pantallaBuscar.PantallaBuscarScreen
import com.example.investlearntfg.ui.screens.pantallaConfiguracion.PantallaConfiguracionScreen
import com.example.investlearntfg.ui.screens.pantallaEditarPerfil.PantallaEditarPerfilScreen
import com.example.investlearntfg.ui.screens.pantallaInicio.PantallaInicialScreen
import com.example.investlearntfg.ui.screens.pantallaPerfil.PantallaPerfilScreen
import com.example.investlearntfg.ui.theme.InvestLearnTFGTheme
import com.google.gson.Gson

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

        composable(Destinations.PANTALLA_PERFIL_SCREEN) {
            PantallaPerfilScreen(navController)
        }

        composable("${Destinations.PANTALLA_ACCION_SCREEN}/{empresaJson}") { backStackEntry ->
            val empresaJson = backStackEntry.arguments?.getString("empresaJson")
            val empresa = Gson().fromJson(empresaJson, EmpresaPreview::class.java)
            PantallaAccionScreen(navController, empresa)
        }

        composable(Destinations.PANTALLA_CONFIGURACION_SCREEN) {
            PantallaConfiguracionScreen(navController)
        }

        composable(Destinations.PANTALLA_EDITAR_PERFIL_SCREEN) {
            PantallaEditarPerfilScreen(navController)
        }
    }
}