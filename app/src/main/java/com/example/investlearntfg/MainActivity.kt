package com.example.investlearntfg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph
import androidx.navigation.compose.rememberNavController
import com.example.investlearntfg.ui.navigation.Destinations
import com.example.investlearntfg.ui.navigation.NavGraph
import com.example.investlearntfg.ui.theme.InvestLearnTFGTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InvestLearnTFGTheme {
                App()
            }
        }
    }
}

@Composable
fun App() {

    val navController = rememberNavController()
    val currentRoute = remember { mutableStateOf(Destinations.LOGIN_SCREEN) }

    LaunchedEffect(navController) {
        navController.addOnDestinationChangedListener{ _, destination, _ ->
            currentRoute.value = destination.route ?: Destinations.LOGIN_SCREEN
        }
    }

    Scaffold(
        content = { padding ->
            Box(modifier = Modifier.padding(padding)) {
                NavGraph(navController)
            }

        },
        modifier = Modifier.fillMaxSize()
    )
}
