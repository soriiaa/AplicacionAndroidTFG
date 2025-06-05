package com.example.investlearntfg.ui.screens.pantallaConfiguracion

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.investlearntfg.R
import com.example.investlearntfg.ui.components.BotonVolverAtrasPredeterminado
import com.example.investlearntfg.ui.components.BottomNavigationBarPredeterminado

@Composable
fun PantallaConfiguracionScreen(
    navController: NavController,
    viewModel: PantallaConfiguracionViewModel = hiltViewModel()
) {

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
                .weight(1f)
                .fillMaxWidth()
        ) {
            BotonVolverAtrasPredeterminado(
                navController,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 5.dp)
            )
            Text(
                text = "Editar Perfil",
                modifier = Modifier.align(Alignment.Center),
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Button(
                onClick = { viewModel.subirCambiosAFirebase() },
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.color4)),
                enabled = true/*botonHabilitado*/,
                modifier = Modifier
                    .height(40.dp)
                    .width(90.dp)
                    .align(Alignment.CenterEnd)
                    .padding(end = 15.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Done,
                    contentDescription = "Guardar",
                    tint = Color.White
                )
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