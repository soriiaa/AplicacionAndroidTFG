package com.example.investlearntfg.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.investlearntfg.R

@Composable
fun LogoAplicacionPulsable(onClick: () -> Unit) {
    androidx.compose.material3.Surface(
        modifier = Modifier.size(100.dp),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.background,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        onClick = onClick
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_modo_oscuro),
                contentDescription = "Logo",
            )
        }
    }
}