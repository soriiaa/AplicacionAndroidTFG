package com.example.investlearntfg.ui.screens.pantallaAccion

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
import com.example.investlearntfg.ui.components.BotonAnadirFavoritoPredeterminado
import com.example.investlearntfg.ui.components.BotonVolverAtrasPredeterminado
import com.example.investlearntfg.ui.components.LogoAplicacionPulsable

@Composable
fun PantallaAccionScreen(navController: NavController) {

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
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 5.dp)
            ) {
                BotonVolverAtrasPredeterminado(navController)
            }
            Box(
                modifier = Modifier.align(Alignment.Center)
            ) {
                LogoAplicacionPulsable {
                    // Aquí va el onClick
                }
            }
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(start = 5.dp)
            ) {

                // TODO ESTO
                BotonAnadirFavoritoPredeterminado(false) { }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun PantallaAccionScreenPreview() {
    PantallaAccionScreen(navController = rememberNavController())
}