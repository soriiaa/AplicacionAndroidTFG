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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.investlearntfg.R
import com.example.investlearntfg.ui.components.SelectorDesplegable
import com.example.investlearntfg.ui.components.TextFieldContrasena
import com.example.investlearntfg.ui.components.TextFieldPredeterminado
import com.example.investlearntfg.ui.components.TitulosInvestLearn
import com.example.investlearntfg.ui.navigation.Destinations
import com.example.investlearntfg.ui.theme.InvestLearnTFGTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun SingUpScreen(navController: NavController) {

    val viewModel: SignUpViewModel = hiltViewModel()
    val scope = rememberCoroutineScope()
    val textoError = remember { mutableStateOf(TextFieldValue()) }
    val habilitarTextoError = remember { mutableStateOf(false) }
    val mostrarDialogo = remember { mutableStateOf(false) }

    val opcionesMoneda = listOf(
        "Dólar estadounidense - $ - USD",
        "Euro - € - EUR",
        "Libra esterlina - £ - GBP",
        "Yen japonés - ¥ - JPY",
        "Franco suizo - CHF - CHF"
    )

    val focusManager = LocalFocusManager.current

    val textoNombre = remember { mutableStateOf(TextFieldValue()) }
    val textoApellido = remember { mutableStateOf(TextFieldValue()) }
    val textoNickname = remember { mutableStateOf(TextFieldValue()) }
    val textoCorreo = remember { mutableStateOf(TextFieldValue()) }
    val textoContrasena = remember { mutableStateOf(TextFieldValue()) }
    val textoConfirmarContrasena = remember { mutableStateOf(TextFieldValue()) }
    var monedaSeleccionada by remember { mutableStateOf("Elegir moneda") }

    InvestLearnTFGTheme {
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
                    TextFieldPredeterminado("Nombre", textoNombre)
                    Spacer(modifier = Modifier.height(30.dp))
                    TextFieldPredeterminado("Apellidos", textoApellido)
                    Spacer(modifier = Modifier.height(30.dp))
                    TextFieldPredeterminado("Nickname", textoNickname)
                    Spacer(modifier = Modifier.height(30.dp))
                    TextFieldPredeterminado("Correo", textoCorreo)
                    Spacer(modifier = Modifier.height(30.dp))
                    TextFieldContrasena(stringResource(R.string.contrasena_longitud), textoContrasena)
                    Spacer(modifier = Modifier.height(30.dp))
                    TextFieldContrasena("Confirmar Contraseña", textoConfirmarContrasena)
                    Spacer(modifier = Modifier.height(30.dp))
                    SelectorDesplegable(
                        opcionesMoneda,
                        monedaSeleccionada,
                        onSeleccionChanged = { nuevaSeleccion ->
                            monedaSeleccionada = nuevaSeleccion
                        }
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                    MensajesErrores(textoError, habilitarTextoError)
                    BotonRegistroSingIn(
                        navController,
                        mostrarDialogo,
                        viewModel,
                        scope,
                        textoError,
                        habilitarTextoError,
                        textoNombre,
                        textoApellido,
                        textoNickname,
                        textoCorreo,
                        textoContrasena,
                        textoConfirmarContrasena,
                        monedaSeleccionada
                    )

                }
            }

            Spacer(modifier = Modifier.height(30.dp))

        }
    }
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
fun MensajesErrores(
    textoError: MutableState<TextFieldValue>,
    habilitarTextoError: MutableState<Boolean>
) {

    if (habilitarTextoError.value) {
        Text(
            text = textoError.value.text,
            color = Color.Red,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(25.dp))
    }

}

@Composable
fun BotonRegistroSingIn(

    navController: NavController,
    mostrarDialogo: MutableState<Boolean>,
    viewModel: SignUpViewModel,
    scope: CoroutineScope,
    textoError: MutableState<TextFieldValue>,
    habilitarTextoError: MutableState<Boolean>,
    textoNombreIntroducido: MutableState<TextFieldValue>,
    textoApellidoIntroducido: MutableState<TextFieldValue>,
    textoNicknameIntroducido: MutableState<TextFieldValue>,
    textoCorreoIntroducido: MutableState<TextFieldValue>,
    textoContrasenaIntroducido: MutableState<TextFieldValue>,
    textoConfirmarContrasenaIntroducido: MutableState<TextFieldValue>,
    textoMonedaIntroducido: String

) {

    val todosLosCamposValidos = textoNombreIntroducido.value.text.isNotBlank() &&
            textoApellidoIntroducido.value.text.isNotBlank() &&
            textoNicknameIntroducido.value.text.isNotBlank() &&
            textoCorreoIntroducido.value.text.isNotBlank() &&
            textoContrasenaIntroducido.value.text.isNotBlank() &&
            textoConfirmarContrasenaIntroducido.value.text.isNotBlank() &&
            textoMonedaIntroducido != "Elegir moneda" &&
            textoContrasenaIntroducido.value.text == textoConfirmarContrasenaIntroducido.value.text &&
            esCorreoValido(textoCorreoIntroducido.value.text) &&
            textoContrasenaIntroducido.value.text.length >= 6 &&
            textoConfirmarContrasenaIntroducido.value.text.length >= 6

    Button(
        onClick = {
            scope.launch {

                val emailExiste =
                    viewModel.verificarCorreoExistente(textoCorreoIntroducido.value.text)

                if (emailExiste) {
                    textoError.value = TextFieldValue("El email introducido ya está registrado")
                    habilitarTextoError.value = true
                } else {
                    habilitarTextoError.value = false
                    mostrarDialogo.value = viewModel.registrarUsuario(
                        textoNombreIntroducido.value.text,
                        textoApellidoIntroducido.value.text,
                        textoNicknameIntroducido.value.text,
                        textoCorreoIntroducido.value.text,
                        textoContrasenaIntroducido.value.text,
                        textoMonedaIntroducido
                    )
                }
            }
        },
        colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.color4)),
        modifier = Modifier
            .height(50.dp)
            .width(150.dp),
        enabled = todosLosCamposValidos
    ) {
        Text(
            stringResource(R.string.texto_registro4),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )
    }

    if (mostrarDialogo.value) {
        AlertDialog(
            onDismissRequest = {
                mostrarDialogo.value = false
                navController.popBackStack()
                navController.navigate(Destinations.LOGIN_SCREEN)
                navController.popBackStack()
            },
            title = { Text("Registro exitoso") },
            text = { Text("Tu cuenta ha sido creada correctamente.") },
            confirmButton = {
                Button(onClick = {
                    mostrarDialogo.value = false
                    navController.popBackStack()
                    navController.navigate(Destinations.LOGIN_SCREEN)
                    navController.popBackStack()
                }) {
                    Text("Continuar")
                }
            }
        )
    }
}

fun esCorreoValido(correo: String): Boolean {
    val regex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}\$")
    return correo.matches(regex)
}