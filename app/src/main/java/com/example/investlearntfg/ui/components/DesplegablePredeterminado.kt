package com.example.investlearntfg.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.investlearntfg.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DesplegableMonedaPredeterminado(
    opciones: List<String>,
    monedaSeleccionada: String,
    onSeleccionChanged: (String) -> Unit
) {

    var seleccionado by remember { mutableStateOf(monedaSeleccionada) }
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .widthIn(max = 280.dp)
            .clip(RoundedCornerShape(5.dp))
            .background(colorResource(id = R.color.color1))
            .border(2.dp, colorResource(id = R.color.color1), RoundedCornerShape(5.dp))
    ) {

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier
                .background(
                    colorResource(id = R.color.color1),
                    shape = RoundedCornerShape(5.dp)
                )
                .border(2.dp, colorResource(id = R.color.color1), RoundedCornerShape(5.dp)),
        ) {
            TextField(
                value = seleccionado,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorResource(id = R.color.color1),
                    unfocusedContainerColor = colorResource(id = R.color.color1),
                    disabledContainerColor = colorResource(id = R.color.color1),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                textStyle = TextStyle(color = Color.Black, fontSize = 17.sp),
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier
                    .menuAnchor()
                    .background(colorResource(id = R.color.color1))
                    .border(2.dp, colorResource(id = R.color.color1), RoundedCornerShape(5.dp))
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                opciones.forEach { opcion ->
                    DropdownMenuItem(
                        text = { Text(opcion) },
                        onClick = {
                            seleccionado = opcion
                            onSeleccionChanged(opcion)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}