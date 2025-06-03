package com.example.investlearntfg.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.investlearntfg.R

@Composable
fun TextFieldContrasena(
    textoEscrito: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    placeholder: String
) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = textoEscrito,
        onValueChange = { nuevaContrasena -> onValueChange(nuevaContrasena) },
        placeholder = { Text(placeholder, color = Color.Black) },
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
        singleLine = true,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val image =
                if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
            val description = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"

            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = image, contentDescription = description, tint = Color.Black)
            }
        }
    )
}
