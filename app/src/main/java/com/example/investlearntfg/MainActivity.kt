package com.example.investlearntfg

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.colorResource
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.example.investlearntfg.ui.navigation.AuthNavGraph
import com.example.investlearntfg.ui.navigation.MainAppNavGraph
import com.example.investlearntfg.ui.theme.InvestLearnTFGTheme
import com.example.investlearntfg.utils.hayUsuarioLogeado
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db = FirebaseFirestore.getInstance()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            InvestLearnTFGTheme(dynamicColor = true, darkTheme = true) {

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
    val isLoggedIn = remember { mutableStateOf(hayUsuarioLogeado()) }
    val navController = rememberNavController()

    if (isLoggedIn.value) {
        MainAppNavGraph(
            navController = navController,
            onLogout = {
                isLoggedIn.value = false
            }
        )
    } else {
        AuthNavGraph(
            navController = navController,
            onLoginSuccess = {
                isLoggedIn.value = true
            }
        )
    }
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