package com.example.investlearntfg.ui.screens.pantallaLogin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.investlearntfg.R
import com.example.investlearntfg.ui.theme.InvestLearnTFGTheme

@Composable
fun LoginScreen(navController: NavController) {
    InvestLearnTFGTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(46.dp))
            Text(
                text = stringResource(R.string.nombreApp),
                style = TextStyle(
                    fontSize = 44.sp
                )
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = stringResource(R.string.apartado_login),
                style = TextStyle(
                    fontSize = 20.sp
                )
            )
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