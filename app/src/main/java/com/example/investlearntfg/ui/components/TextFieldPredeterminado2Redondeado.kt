package com.example.investlearntfg.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.investlearntfg.R

@Composable
fun TextFieldPredeterminado2Redondeado(
    textoInicial: String,
    textoEscrito: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier
) {
    OutlinedTextField(
        value = textoEscrito,
        onValueChange = { textoIntroducido -> onValueChange(textoIntroducido) },
        placeholder = { Text(textoInicial, color = Color.Black) },
        textStyle = TextStyle(color = Color.Black, fontSize = 17.sp),
        shape = RoundedCornerShape(45.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = colorResource(id = R.color.color1),
            unfocusedContainerColor = colorResource(id = R.color.color1),
            disabledContainerColor = colorResource(id = R.color.color1),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 28.dp),
        singleLine = true
    )
}