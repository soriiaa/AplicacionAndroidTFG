package com.example.investlearntfg.ui.screens.pantallaLogin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.investlearntfg.R
import com.example.investlearntfg.ui.theme.InvestLearnTFGTheme

@Composable
fun LoginScreen(navController: NavController) {

    val textoUsuarioIntroducido = remember { mutableStateOf(TextFieldValue()) }

    InvestLearnTFGTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TitulosLogin()
            TextFieldUsuario(textoUsuarioIntroducido)
        }
    }
}

@Composable
fun TitulosLogin() {
    Spacer(modifier = Modifier.height(90.dp))
    Text(
        text = stringResource(R.string.nombreApp),
        style = TextStyle(
            fontSize = 55.sp
        )
    )
    Spacer(modifier = Modifier.height(80.dp))
    Text(
        text = stringResource(R.string.apartado_login),
        style = TextStyle(
            fontSize = 35.sp
        )
    )
    Spacer(modifier = Modifier.height(90.dp))
}


@Composable
fun TextFieldUsuario(textoUsuarioIntroducido: MutableState<TextFieldValue>) {
    TextField(
        value = textoUsuarioIntroducido.value,
        onValueChange = { newText ->
            textoUsuarioIntroducido.value = newText
        },
        label = { Text("Usuario") }
    )
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    InvestLearnTFGTheme {
        LoginScreen(navController = rememberNavController())
    }
}