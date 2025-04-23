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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.investlearntfg.R
import com.example.investlearntfg.ui.components.TextFieldContrasena
import com.example.investlearntfg.ui.components.TitulosInvestLearn
import com.example.investlearntfg.ui.navigation.Destinations
import com.example.investlearntfg.ui.theme.InvestLearnTFGTheme

@Composable
fun LoginScreen(navController: NavController) {

    val focusManager = LocalFocusManager.current
    val textoUsuarioIntroducido = remember { mutableStateOf(TextFieldValue()) }
    val textoContrasenaIntroducido = remember { mutableStateOf(TextFieldValue()) }

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
                TextFieldUsuario(textoUsuarioIntroducido)
                Spacer(modifier = Modifier.height(50.dp))
                TextFieldContrasena("Contraseña", textoContrasenaIntroducido)
                Spacer(modifier = Modifier.height(50.dp))
                BotonInicioSesionLogin(textoUsuarioIntroducido, textoContrasenaIntroducido)
                Spacer(modifier = Modifier.height(50.dp))
                Registro(navController)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldUsuario(textoUsuarioIntroducido: MutableState<TextFieldValue>) {
    OutlinedTextField(
        value = textoUsuarioIntroducido.value,
        onValueChange = { textoIntroducido ->
            textoUsuarioIntroducido.value = textoIntroducido
        },
        placeholder = { Text("Usuario", color = Color.Black) },
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
fun BotonInicioSesionLogin(
    textoUsuarioIntroducido: MutableState<TextFieldValue>,
    textoContrasenaIntroducido: MutableState<TextFieldValue>
) {

    Button(
        onClick = { /* TODO Comprobar que los dos campos contengan texto y hacer el inicio de sesión */ },
        colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.color4)),
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

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    InvestLearnTFGTheme {
        LoginScreen(navController = rememberNavController())
    }
}