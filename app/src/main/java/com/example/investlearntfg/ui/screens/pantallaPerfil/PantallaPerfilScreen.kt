package com.example.investlearntfg.ui.screens.pantallaPerfil

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
                    .padding(start = 5.dp)
            ) {
                BotonEditarPerfil(navController)
            }

            // Boton app
            Box(
                modifier = Modifier.align(Alignment.Center)
            ) {
                LogoAplicacionPulsable { /* TODO onClick */ }
            }

            // Boton derecha
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 5.dp)
            ) {
                IconButton(
                    onClick = { navController.navigate(Destinations.PANTALLA_CONFIGURACION_SCREEN) }
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
                .weight(0.35f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Box {
                Image(
                    painter = painterResource(id = R.drawable.foto_perfil_modo_oscuro),
                    contentDescription = "Foto de perfil",
                    modifier = Modifier
                        .size(130.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                IconButton(
                    onClick = { /* TODO: acción para cambiar foto */ },
                    modifier = Modifier
                        .size(36.dp)
                        .align(Alignment.BottomEnd)
                        .offset(x = (-10).dp, y = 1.dp)
                        .background(color = colorResource(id = R.color.color4), shape = CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Cambiar foto de perfil",
                        tint = Color.DarkGray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .weight(0.2f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "soriiaa",
                fontSize = 18.sp
            )
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {

        }
        BottomNavigationBarPredeterminado(navController)
    }
}

@Composable
fun BotonEditarPerfil(navController: NavController) {
    IconButton(
        onClick = { navController.navigate(Destinations.PANTALLA_EDITAR_PERFIL_SCREEN) },
        modifier = Modifier
            .size(48.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = stringResource(R.string.texto_editar_perfil),
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PantallaPerfilScreenPreview() {
    PantallaPerfilScreen(navController = rememberNavController())
}