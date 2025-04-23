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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.investlearntfg.R
import com.example.investlearntfg.ui.components.SelectorDesplegable
import com.example.investlearntfg.ui.components.TextFieldPredeterminado
import com.example.investlearntfg.ui.components.TitulosInvestLearn
import com.example.investlearntfg.ui.navigation.Destinations
import com.example.investlearntfg.ui.theme.InvestLearnTFGTheme

@Composable
fun SingUpScreen(navController: NavController) {

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
                    TextFieldPredeterminado("Contraseña", textoContrasena)
                    Spacer(modifier = Modifier.height(30.dp))
                    TextFieldPredeterminado("Confirmar Contraseña", textoConfirmarContrasena)
                    Spacer(modifier = Modifier.height(30.dp))
                    SelectorDesplegable(
                        opcionesMoneda,
                        monedaSeleccionada,
                        onSeleccionChanged = { nuevaSeleccion ->
                            monedaSeleccionada = nuevaSeleccion
                        }
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                    BotonRegistroSingIn(
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
fun BotonRegistroSingIn(

    textoNombreIntroducido: MutableState<TextFieldValue>,
    textoApellidoIntroducido: MutableState<TextFieldValue>,
    textoNicknameIntroducido: MutableState<TextFieldValue>,
    textoCorreoIntroducido: MutableState<TextFieldValue>,
    textoContrasenaIntroducido: MutableState<TextFieldValue>,
    textoConfirmarContrasenaIntroducido: MutableState<TextFieldValue>,
    textoMonedaIntroducido: String

) {

    Button(
        onClick = { /* TODO Comprobar que los campos estén rellenos y hacer el registro */ },
        colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.color4)),
        modifier = Modifier
            .height(50.dp)
            .width(150.dp)
    ) {
        Text(
            stringResource(R.string.texto_registro4),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SingUpScreenPreview() {
    SingUpScreen(navController = rememberNavController())
}