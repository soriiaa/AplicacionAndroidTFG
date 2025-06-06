package com.example.investlearntfg.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.investlearntfg.R
import com.example.investlearntfg.ui.navigation.Destinations
import com.example.investlearntfg.ui.screens.pantallaBuscar.PantallaBuscarViewModel

@Composable
fun BottomNavigationBarPredeterminado(navController: NavController) {

    val currentRoute = navController.currentDestination?.route
    val colorIndicador = colorResource(R.color.color6)
    val colorNoSeleccionado = Color.White
    val viewModelPantallaBuscar: PantallaBuscarViewModel = hiltViewModel()

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
            label = { Text("Inicio") },
            selected = currentRoute == Destinations.PANTALLA_INICIAL_SCREEN,
            onClick = {
                navController.navigate(Destinations.PANTALLA_INICIAL_SCREEN) {
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
                viewModelPantallaBuscar.limpiarEmpresasBusqueda()
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                unselectedIconColor = colorNoSeleccionado,
                selectedTextColor = Color.White,
                unselectedTextColor = colorNoSeleccionado,
                indicatorColor = colorIndicador
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
            label = { Text("Buscar") },
            selected = currentRoute == Destinations.PANTALLA_BUSCAR_SCREEN,
            onClick = {
                navController.navigate(Destinations.PANTALLA_BUSCAR_SCREEN) {
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                unselectedIconColor = colorNoSeleccionado,
                selectedTextColor = Color.White,
                unselectedTextColor = colorNoSeleccionado,
                indicatorColor = colorIndicador
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Newspaper, contentDescription = "Noticias") },
            label = { Text("Noticias") },
            selected = currentRoute == Destinations.PANTALLA_NOTICIAS_SCREEN,
            onClick = {
                navController.navigate(Destinations.PANTALLA_NOTICIAS_SCREEN) {
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                unselectedIconColor = colorNoSeleccionado,
                selectedTextColor = Color.White,
                unselectedTextColor = colorNoSeleccionado,
                indicatorColor = colorIndicador
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
            label = { Text("Perfil") },
            selected = currentRoute == Destinations.PANTALLA_PERFIL_SCREEN,
            onClick = {
                navController.navigate(Destinations.PANTALLA_PERFIL_SCREEN) {
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
                viewModelPantallaBuscar.limpiarEmpresasBusqueda()
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                unselectedIconColor = colorNoSeleccionado,
                selectedTextColor = Color.White,
                unselectedTextColor = colorNoSeleccionado,
                indicatorColor = colorIndicador
            )
        )
    }
}