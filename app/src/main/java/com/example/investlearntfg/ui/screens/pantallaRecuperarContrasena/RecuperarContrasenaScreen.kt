package com.example.investlearntfg.ui.screens.pantallaRecuperarContrasena

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.investlearntfg.R
import com.example.investlearntfg.ui.components.BotonVolverAtrasPredeterminado
import com.example.investlearntfg.ui.components.LogoAplicacionPulsable
import com.example.investlearntfg.ui.components.TextFieldPredeterminado2
import com.example.investlearntfg.ui.navigation.Destinations
import kotlin.math.roundToInt

@Composable
fun RecuperarContrasenaScreen(
    navController: NavHostController,
    viewModel: RecuperarContrasenaViewModel = hiltViewModel()
) {

    val focusManager = LocalFocusManager.current
    val estadoRecuperacion = viewModel.estado.collectAsState()

    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp
    val tamanoFuenteTitulo = (screenWidthDp * 0.08).roundToInt().sp
    val tamanoFuenteTexto = (screenWidthDp * 0.042).roundToInt().sp

    val textoCampoCorreo = viewModel.correo.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            },
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
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
        }

        Box(
            modifier = Modifier
                .weight(1.5f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.recuperacion_contrasena),
                fontSize = tamanoFuenteTitulo
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(start = 30.dp, end = 30.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.texto_instrucciones_recuperacion_contrasena),
                fontSize = tamanoFuenteTexto
            )
        }

        Box(
            modifier = Modifier
                .weight(2f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {

            TextFieldPredeterminado2(
                textoInicial = stringResource(R.string.texto_correo2),
                textoEscrito = textoCampoCorreo.value,
                onValueChange = { viewModel.onCorreoChange(it) }
            )

            var mostrarDialogo: Boolean = false

            when (val estado = estadoRecuperacion.value) {
                is EstadoRecuperacion.Cargando -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is EstadoRecuperacion.Exito -> {
                    AlertDialog(
                        onDismissRequest = {
                            mostrarDialogo = false
                            navController.popBackStack()
                            navController.navigate(Destinations.LOGIN_SCREEN)
                            navController.popBackStack()
                        },
                        title = { Text("Correo enviado correctamente") },
                        text = { Text("Siga los pasos disponibles en el correo de su bandeja de entrada.") },
                        confirmButton = {
                            Button(onClick = {
                                mostrarDialogo = false
                                navController.popBackStack()
                                navController.navigate(Destinations.LOGIN_SCREEN)
                                navController.popBackStack()
                            }) {
                                Text("Aceptar")
                            }
                        }
                    )
                }

                is EstadoRecuperacion.Error -> {
                    Text(
                        text = estado.mensaje,
                        color = Color.Red,
                        fontSize = tamanoFuenteTexto
                    )
                }

                else -> {}
            }

        }

        Box(
            modifier = Modifier
                .weight(2.5f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            BotonEnviarEmailRecuperarContrasenaScreen(
                viewModel,
                screenWidthDp
            )
        }

    }
}

@Composable
fun BotonEnviarEmailRecuperarContrasenaScreen(
    viewModel: RecuperarContrasenaViewModel,
    screenWidthDp: Int
) {

    val tamanoBoton = (screenWidthDp * 0.14).roundToInt().dp
    val tamanoFuenteBoton = (screenWidthDp * 0.045).roundToInt().sp
    val puedeEnviar = viewModel.puedeEnviarEmail.collectAsState().value

    Button(
        onClick = {
            viewModel.recuperarContrasena()
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 70.dp, vertical = 1.dp)
            .height(tamanoBoton),
        shape = RoundedCornerShape(12.dp),
        enabled = puedeEnviar
    ) {
        Text(
            text = stringResource(R.string.recuperacion_contrasena),
            fontSize = tamanoFuenteBoton
        )
    }

}