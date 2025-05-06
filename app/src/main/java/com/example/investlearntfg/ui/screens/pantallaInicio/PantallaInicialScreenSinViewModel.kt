package com.example.investlearntfg.ui.screens.pantallaInicio

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.investlearntfg.R
import com.example.investlearntfg.ui.components.BottomNavigationBarPredeterminado
import com.example.investlearntfg.ui.components.LogoAplicacionPulsable
import com.example.investlearntfg.ui.theme.InvestLearnTFGTheme

@Composable
fun PantallaInicialScreenSinVM(
    navController: NavHostController
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.TopCenter
        ) {
            LogoAplicacionPulsable {
                // Aquí va el onClick
            }
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {}

        BottomNavigationBarPredeterminado(navController)
    }
}


@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    InvestLearnTFGTheme {
        PantallaInicialScreenSinVM(navController = rememberNavController())
    }
}