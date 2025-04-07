package com.example.investlearntfg.ui.screens.pantallaRegistro

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.investlearntfg.R
import com.example.investlearntfg.ui.components.TextFieldPredeterminado
import com.example.investlearntfg.ui.components.TitulosInvestLearn
import com.example.investlearntfg.ui.navigation.Destinations
import com.example.investlearntfg.ui.theme.InvestLearnTFGTheme

@Composable
fun SingUpScreen(navController: NavController) {

    val focusManager = LocalFocusManager.current
    val textoNombre = remember { mutableStateOf(TextFieldValue()) }
    val textoApellido = remember { mutableStateOf(TextFieldValue()) }

    InvestLearnTFGTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            BotonInicioSesion(navController)
            TitulosInvestLearn()

            LazyColumn(
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
                item {
                    Spacer(modifier = Modifier.height(30.dp))
                    TextFieldPredeterminado("Nombre", textoNombre)
                    Spacer(modifier = Modifier.height(30.dp))
                    TextFieldPredeterminado("Apellidos", textoApellido)
                }
            }

        }
    }
}

@Composable
fun BotonInicioSesion(navController: NavController) {
    TextButton(
        onClick = {
            navController.popBackStack()
            navController.navigate(Destinations.LOGIN_SCREEN)
            navController.popBackStack()
        },
        colors = ButtonDefaults.textButtonColors(contentColor = colorResource(id = R.color.color3)),
        modifier = Modifier
            .padding(0.dp)
            .wrapContentSize()
    ) {
        Text(
            stringResource(R.string.texto_iniciar_sesion),
            fontSize = 16.sp
        )
    }

}

@Preview(showBackground = true)
@Composable
fun SingUpScreenPreview() {
    SingUpScreen(navController = rememberNavController())
}