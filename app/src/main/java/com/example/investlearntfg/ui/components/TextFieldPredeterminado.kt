package com.example.investlearntfg.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.investlearntfg.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldPredeterminado(textoInicial: String, textoEscrito: MutableState<TextFieldValue>) {
    OutlinedTextField (
        value = textoEscrito.value,
        onValueChange = { textoIntroducido ->
            textoEscrito.value = textoIntroducido
        },
        placeholder = { Text(textoInicial, color = Color.Black) },
        textStyle = TextStyle(color = Color.Black, fontSize = 17.sp),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = colorResource(id = R.color.color1),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
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