package com.example.investlearntfg.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.investlearntfg.R

@Composable
fun SelectorCantidadAcciones(
    cantidadInicial: Int = 1,
    cantidadMinima: Int = 1,
    cantidadMaxima: Int = 999,
    onCantidadCambio: (Int) -> Unit = {}
) {
    var cantidad by remember { mutableStateOf(cantidadInicial.coerceAtLeast(cantidadMinima)) }

    Row(
        modifier = Modifier
            .border(1.dp, Color.White, RoundedCornerShape(7.dp))
            .background(colorResource(R.color.backgroundColor))
            .padding(horizontal = 7.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "-",
            fontSize = 20.sp,
            color = if (cantidad > cantidadMinima) Color.White else Color.Gray,
            modifier = Modifier
                .clickable(enabled = cantidad > cantidadMinima) {
                    if (cantidad > cantidadMinima) {
                        cantidad--
                        onCantidadCambio(cantidad)
                    }
                }
                .padding(horizontal = 10.dp)
        )
        Text(
            text = cantidad.toString(),
            fontSize = 18.sp,
            modifier = Modifier
                .padding(horizontal = 10.dp)
        )
        Text(
            text = "+",
            fontSize = 20.sp,
            color = if (cantidad < cantidadMaxima) Color.White else Color.Gray,
            modifier = Modifier
                .clickable(enabled = cantidad < cantidadMaxima) {
                    if (cantidad < cantidadMaxima) {
                        cantidad++
                        onCantidadCambio(cantidad)
                    }
                }
                .padding(horizontal = 10.dp)
        )
    }
}