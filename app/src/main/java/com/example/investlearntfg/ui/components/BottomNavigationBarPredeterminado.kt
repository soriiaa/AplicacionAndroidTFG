package com.example.investlearntfg.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.investlearntfg.ui.navigation.Destinations

@Composable
fun BottomNavigationBarPredeterminado(navController: NavController) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
            label = { Text("Inicio") },
            selected = false,
            onClick = { navController.navigate(Destinations.PANTALLA_INICIAL_SCREEN) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Search, contentDescription = "Búsqueda") },
            label = { Text("Buscar") },
            selected = false,
            onClick = { navController.navigate(Destinations.PANTALLA_BUSCAR_SCREEN) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
            label = { Text("Perfil") },
            selected = false,
            onClick = { navController.navigate(Destinations.PANTALLA_PERFIL_SCREEN) }
        )
    }
}