package com.example.investlearntfg.ui.screens.pantallaPerfil

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.investlearntfg.R
import com.example.investlearntfg.ui.components.BottomNavigationBarPredeterminado
import com.example.investlearntfg.ui.components.LogoAplicacionPulsable
import com.example.investlearntfg.ui.navigation.Destinations
import com.example.investlearntfg.ui.screens.pantallaLogin.LoginViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PantallaPerfilScreen(navController: NavHostController) {

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
                IconButton(
                    onClick = { /* TODO ir a pantalla configuracion */ }
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Ir a configuración",
                        tint = Color.White
                    )
                }
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

@Composable
fun BotonEditarPerfil(navController: NavController) {

    val buttonWidth = with(LocalDensity.current) { 150.dp.toPx() }
    val textSize = buttonWidth / 32f

    Button(
        onClick = { /* TODO navegar a la pestaña de editar perfil */ },
        colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.color4)),
        modifier = Modifier
            .fillMaxWidth(0.35f)
            .height(70.dp)
            .padding(start = 24.dp, end = 16.dp, top = 16.dp, bottom = 16.dp)
    ) {
        Text(
            stringResource(R.string.texto_editar_perfil),
            color = Color.White,
            fontSize = textSize.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PantallaPerfilScreenPreview() {
    PantallaPerfilScreen(navController = rememberNavController())
}