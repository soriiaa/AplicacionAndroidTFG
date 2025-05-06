package com.example.investlearntfg

import android.os.Build
import android.os.Bundle
import android.view.View
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
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.colorResource
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.example.investlearntfg.ui.navigation.Destinations
import com.example.investlearntfg.ui.navigation.NavGraph
import com.example.investlearntfg.ui.theme.InvestLearnTFGTheme
import com.example.investlearntfg.utils.hayUsuarioLogeado
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // INICIAR FIREBASE
        FirebaseApp.initializeApp(this)
        val db = FirebaseFirestore.getInstance()

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            InvestLearnTFGTheme {

                SetSystemNavBarColor(colorResource(id = R.color.backgroundColor), darkIcons = false)

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

    val startDestination = remember {
        if (hayUsuarioLogeado()) {
            Destinations.PANTALLA_INICIAL_SCREEN
        } else {
            Destinations.LOGIN_SCREEN
        }
    }

    LaunchedEffect(navController) {

        navController.addOnDestinationChangedListener{ _, destination, _ ->
            currentRoute.value = destination.route ?: Destinations.LOGIN_SCREEN
        }
    }

    Scaffold(
        content = { padding ->
            Box(modifier = Modifier.padding(padding)) {
                NavGraph(navController, startDestination = startDestination)
            }

        },
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun SetSystemNavBarColor(color: Color, darkIcons: Boolean) {
    val view = LocalView.current
    SideEffect {
        val window = (view.context as ComponentActivity).window
        window.navigationBarColor = color.toArgb()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.setSystemBarsAppearance(
                if (darkIcons) WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS else 0,
                WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
            )
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = if (darkIcons)
                View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            else 0
        }
    }
}