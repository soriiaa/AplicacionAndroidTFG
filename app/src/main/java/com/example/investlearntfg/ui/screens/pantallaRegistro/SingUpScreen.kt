package com.example.investlearntfg.ui.screens.pantallaRegistro

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.investlearntfg.R
import com.example.investlearntfg.ui.components.SelectorDesplegable
import com.example.investlearntfg.ui.components.TextFieldContrasena
import com.example.investlearntfg.ui.components.TextFieldPredeterminado2
import com.example.investlearntfg.ui.components.TitulosInvestLearn
import com.example.investlearntfg.ui.navigation.Destinations

@Composable
fun SignUpScreen(
    navController: NavController,
    viewModel: SignUpViewModel = hiltViewModel()
) {

    val focusManager = LocalFocusManager.current

    val textoNombre by viewModel.textoNombre.collectAsState()
    val textoApellido by viewModel.textoApellido.collectAsState()
    val textoNickname by viewModel.textoNickname.collectAsState()
    val textoCorreo by viewModel.textoCorreo.collectAsState()
    val textoContrasena by viewModel.textoContrasena.collectAsState()
    val textoConfirmarContrasena by viewModel.textoConfirmarContrasena.collectAsState()
    val monedaSeleccionada by viewModel.monedaSeleccionada.collectAsState()

    val botonHabilitado by viewModel.botonHabilitado.collectAsState()
    val formatoCorreoCorrecto by viewModel.formatoCorreoCorrecto.collectAsState()

    val mostrarDialogoCorreo by viewModel.mostrarDialogoCorreoYaExistente.collectAsState()
    val mostrarDialogoRegistroExitoso by viewModel.mostrarDialogoRegistroExitoso.collectAsState()
    val mostrarDialogoRegistroErroneo by viewModel.mostrarDialogoRegistroErroneo.collectAsState()


    val opcionesMoneda = listOf(
        "Dólar estadounidense - $ - USD",
        "Euro - € - EUR",
        "Libra esterlina - £ - GBP",
        "Yen japonés - ¥ - JPY",
        "Franco suizo - CHF - CHF"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(15.dp))
        BotonInicioSesionSingIn(navController)

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 10.dp)
                .padding(bottom = 26.dp)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                    })
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                TitulosInvestLearn()
                TextoRegistro()
                Spacer(modifier = Modifier.height(45.dp))
                TextFieldPredeterminado2(
                    textoInicial = "Nombre",
                    textoEscrito = textoNombre,
                    onValueChange = { viewModel.onNombreChange(it) }
                )
                Spacer1()
                TextFieldPredeterminado2(
                    textoInicial = "Apellidos",
                    textoEscrito = textoApellido,
                    onValueChange = { viewModel.onApellidoChange(it) }
                )
                Spacer1()
                TextFieldPredeterminado2(
                    textoInicial = "Nickname",
                    textoEscrito = textoNickname,
                    onValueChange = { viewModel.onNicknameChange(it) }
                )
                Spacer1()
                TextFieldPredeterminado2(
                    textoInicial = "Correo",
                    textoEscrito = textoCorreo,
                    onValueChange = { viewModel.onCorreoChange(it) }
                )
                if (!formatoCorreoCorrecto) {
                    Text(
                        text = "Formato de correo inválido",
                        color = Color.Red,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                    )
                }
                Spacer1()
                TextFieldContrasena(
                    textoEscrito = textoContrasena,
                    onValueChange = { viewModel.onContrasenaChange(it) },
                    placeholder = "Contraseña - (min 6)"
                )
                Spacer1()
                TextFieldContrasena(
                    textoEscrito = textoConfirmarContrasena,
                    onValueChange = { viewModel.onConfirmarContrasenaChange(it) },
                    placeholder = "Confirmar Contraseña"
                )
                Spacer1()
                SelectorDesplegable(
                    opcionesMoneda,
                    monedaSeleccionada,
                    onSeleccionChanged = { nuevaSeleccion ->
                        viewModel.onMonedaSeleccionadaChange(nuevaSeleccion)
                    }
                )
                Spacer1()
                BotonRegistroSingIn(
                    viewModel,
                    botonHabilitado
                )
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
    }

    if (mostrarDialogoCorreo) {
        AlertDialog(
            onDismissRequest = {
                viewModel.ocultarDialogoCorreo()
            },
            title = { Text("Error en el registro") },
            text = { Text("El correo introducido ya está registrado.") },
            confirmButton = {
                Button(onClick = {
                    viewModel.ocultarDialogoCorreo()
                }) {
                    Text("Aceptar")
                }
            },
            containerColor = colorResource(id = R.color.color7)
        )
    }

    if (mostrarDialogoRegistroExitoso) {
        AlertDialog(
            onDismissRequest = {
                viewModel.ocultarDialogoExitoso()
                navController.popBackStack()
                navController.navigate(Destinations.LOGIN_SCREEN)
                navController.popBackStack()
            },
            title = { Text("Registro exitoso") },
            text = { Text("Tu cuenta ha sido creada correctamente.") },
            confirmButton = {
                Button(onClick = {
                    viewModel.ocultarDialogoExitoso()
                    navController.popBackStack()
                    navController.navigate(Destinations.LOGIN_SCREEN)
                    navController.popBackStack()
                }) {
                    Text("Continuar")
                }
            },
            containerColor = colorResource(id = R.color.color7)
        )
    }

    if (mostrarDialogoRegistroErroneo) {
        AlertDialog(
            onDismissRequest = {
                viewModel.ocultarDialogoErroneo()
            },
            title = { Text("Registro erroneo") },
            text = { Text("Lo sentimos, se ha producido un error inesperado.") },
            confirmButton = {
                Button(onClick = {
                    viewModel.ocultarDialogoErroneo()
                }) {
                    Text("Aceptar")
                }
            },
            containerColor = colorResource(id = R.color.color7)
        )
    }
}

@Composable
fun Spacer1() {
    Spacer(modifier = Modifier.height(30.dp))
}

@Composable
fun TextoRegistro() {
    Text(
        text = stringResource(R.string.texto_registro3),
        style = TextStyle(
            fontSize = 20.sp
        )
    )
}

@Composable
fun BotonInicioSesionSingIn(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 16.dp),
        horizontalArrangement = Arrangement.End
    ) {
        TextButton(
            onClick = {
                navController.popBackStack()
                navController.navigate(Destinations.LOGIN_SCREEN)
                navController.popBackStack()
            },
            colors = ButtonDefaults.textButtonColors(contentColor = colorResource(id = R.color.color3))
        ) {
            Text(
                stringResource(R.string.texto_iniciar_sesion),
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun BotonRegistroSingIn(
    viewModel: SignUpViewModel,
    botonHabilitado: Boolean
) {
    Button(
        onClick = {
            viewModel.verificarCorreoExistente()
            viewModel.registrarUsuario()
        },
        colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.color4)),
        modifier = Modifier
            .height(50.dp)
            .width(150.dp),
        enabled = botonHabilitado
    ) {
        Text(
            stringResource(R.string.texto_registro4),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )
    }
}