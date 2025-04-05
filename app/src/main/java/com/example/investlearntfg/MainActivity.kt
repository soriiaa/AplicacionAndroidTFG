package com.example.investlearntfg

import android.os.Build
import android.os.Bundle
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.example.investlearntfg.ui.navigation.Destinations
import com.example.investlearntfg.ui.navigation.NavGraph
import com.example.investlearntfg.ui.theme.InvestLearnTFGTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InvestLearnTFGTheme {

                window.statusBarColor = ContextCompat.getColor(this, R.color.backgroundColor)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    window.insetsController?.setSystemBarsAppearance(
                        0,
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                    )
                } else {
                    @Suppress("DEPRECATION")
                    window.decorView.systemUiVisibility = 0
                }

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
