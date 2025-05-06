package com.example.investlearntfg.ui.screens.pantallaLogin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.investlearntfg.R
import com.example.investlearntfg.ui.components.TitulosInvestLearn
import com.example.investlearntfg.ui.navigation.Destinations
import com.example.investlearntfg.ui.theme.InvestLearnTFGTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {

    val focusManager = LocalFocusManager.current
    val textoCorreoIntroducido by viewModel.correo.collectAsState()
    val textoContrasenaIntroducido by viewModel.contrasena.collectAsState()

    val botonHabilitado =
        textoCorreoIntroducido.text.isNotBlank() && textoContrasenaIntroducido.text.isNotBlank()

    InvestLearnTFGTheme {

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = { focusManager.clearFocus() })
                }
        ) {
            val screenHeight = maxHeight
            val screenWidth = maxWidth

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
                Spacer(modifier = Modifier.height(83.dp))
                TitulosInvestLearn()
                Spacer(modifier = Modifier.height(47.dp))
                TextFieldCorreo(textoCorreoIntroducido, viewModel)
                Spacer(modifier = Modifier.height(50.dp))
                TextFieldContrasenaLogin("Contraseña", textoContrasenaIntroducido, viewModel)
                Spacer(modifier = Modifier.height(50.dp))
                BotonInicioSesionLogin(
                    textoCorreoIntroducido,
                    textoContrasenaIntroducido,
                    botonHabilitado,
                    viewModel,
                    navController
                )
                Spacer(modifier = Modifier.height((15.dp)))
                OlvidoContrasena(navController)
                Spacer(modifier = Modifier.height(50.dp))
                Registro(navController)

            }
        }
    }
}

@Composable
fun TextFieldCorreo(textoCorreoIntroducido: TextFieldValue, viewModel: LoginViewModel) {
    OutlinedTextField(
        value = textoCorreoIntroducido,
        onValueChange = {
            viewModel.onCorreoChange(it)
        },
        placeholder = { Text(stringResource(R.string.texto_correo), color = Color.Black) },
        textStyle = TextStyle(color = Color.Black, fontSize = 17.sp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = colorResource(id = R.color.color1),
            unfocusedContainerColor = colorResource(id = R.color.color1),
            disabledContainerColor = colorResource(id = R.color.color1),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        modifier = Modifier
            .background(
                colorResource(id = R.color.color1),
                shape = RoundedCornerShape(5.dp)
            )
            .border(2.dp, colorResource(id = R.color.color1), RoundedCornerShape(5.dp)),
        singleLine = true

    )
}

@Composable
fun TextFieldContrasenaLogin(
    textoInicial: String,
    textoContrasenaIntroducido: TextFieldValue,
    viewModel: LoginViewModel
) {

    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = textoContrasenaIntroducido,
        onValueChange = { viewModel.onContrasenaChange(it) },
        placeholder = { Text(textoInicial, color = Color.Black) },
        textStyle = TextStyle(color = Color.Black, fontSize = 17.sp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = colorResource(id = R.color.color1),
            unfocusedContainerColor = colorResource(id = R.color.color1),
            disabledContainerColor = colorResource(id = R.color.color1),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        modifier = Modifier
            .background(
                colorResource(id = R.color.color1),
                shape = RoundedCornerShape(5.dp)
            )
            .border(2.dp, colorResource(id = R.color.color1), RoundedCornerShape(5.dp)),
        singleLine = true,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(), // Ocultar/Mostrar contraseña
        trailingIcon = {
            val image =
                if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
            val description = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"

            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = image, contentDescription = description, tint = Color.Black)
            }
        }
    )
}

@Composable
fun BotonInicioSesionLogin(
    textoUsuarioIntroducido: TextFieldValue,
    textoContrasenaIntroducido: TextFieldValue,
    botonHabilitado: Boolean,
    viewModel: LoginViewModel,
    navController: NavController
) {

    val mostrarDialogo = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Button(
        onClick = {
            coroutineScope.launch {
                if (textoContrasenaIntroducido.text.length <= 6) {
                    delay(300)
                    mostrarDialogo.value = true
                } else {
                    viewModel.iniciarSesion(
                        onExito = {
                            navController.popBackStack()
                            navController.navigate(Destinations.PANTALLA_INICIAL_SCREEN)
                        },
                        onError = {
                            mostrarDialogo.value = true
                        }
                    )
                }
            }
        },
        colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.color4)),
        enabled = botonHabilitado,
        modifier = Modifier
            .height(50.dp)
            .width(150.dp)
    ) {
        Text(
            stringResource(R.string.texto_iniciar_sesion),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )
    }

    if (mostrarDialogo.value) {
        AlertDialog(
            onDismissRequest = {
                mostrarDialogo.value = false
            },
            title = { Text("Inicio de Sesión fallido") },
            text = { Text("Correo o Contraseña Incorrectos") },
            confirmButton = {
                Button(onClick = {
                    mostrarDialogo.value = false
                }) {
                    Text("Aceptar")
                }
            }
        )
    }

}

@Composable
fun Registro(navController: NavController) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = stringResource(R.string.texto_registro))
            TextButton(
                onClick = { navController.navigate(Destinations.SINGUP_SCREEN) },
                colors = ButtonDefaults.textButtonColors(contentColor = colorResource(id = R.color.color3)),
                modifier = Modifier
                    .padding(0.dp)
                    .wrapContentSize()
            ) {
                Text(
                    stringResource(R.string.texto_registro2),
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
fun OlvidoContrasena(navController: NavController) {

    TextButton(
        onClick = { navController.navigate(Destinations.RECUPERAR_CONTRASENA_SCREEN) },
        colors = ButtonDefaults.textButtonColors(contentColor = colorResource(id = R.color.color3)),
        modifier = Modifier
            .padding(0.dp)
            .wrapContentSize()
    ) {
        Text(
            stringResource(R.string.olvido_contrasena),
            fontSize = 16.sp
        )
    }

}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    InvestLearnTFGTheme {
        LoginScreen(navController = rememberNavController())
    }
}