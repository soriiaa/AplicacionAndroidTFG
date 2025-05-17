package com.example.investlearntfg.ui.screens.pantallaConfiguracion

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.investlearntfg.ui.components.BottomNavigationBarPredeterminado
import com.example.investlearntfg.ui.components.LogoAplicacionPulsable

@Composable
fun PantallaConfiguracionScreen(navController: NavController) {

    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.TopCenter
        ) {

            // Boton Izquierda
            Box(
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                // BotonEditarPerfil(navController)
            }

            // Boton app
            Box(
                modifier = Modifier.align(Alignment.Center)
            ) {
                LogoAplicacionPulsable { /* TODO onClick */}
            }

            // Boton derecha
            Box(
                modifier = Modifier.align(Alignment.CenterEnd)
                    .padding(end = 5.dp)
            ) {
                
            }
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {

        }

        BottomNavigationBarPredeterminado(navController)
    }

}

@Preview(showBackground = true)
@Composable
fun PantallaConfiguracionScreenPreview() {
    PantallaConfiguracionScreen(navController = rememberNavController())
}