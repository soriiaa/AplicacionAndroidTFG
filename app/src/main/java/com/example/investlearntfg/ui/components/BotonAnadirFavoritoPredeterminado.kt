package com.example.investlearntfg.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.example.investlearntfg.R

@Composable
fun BotonAnadirFavoritoPredeterminado(esFavorita: Boolean, onClickFavorito: () -> Unit) {
    IconButton(onClick = onClickFavorito) {
        Icon(
            imageVector = if (esFavorita) Icons.Default.Star else Icons.Default.StarBorder,
            contentDescription = if (esFavorita) "Quitar de favoritos" else "Marcar como favorito",
            tint = if (esFavorita) colorResource(R.color.colorEstrellas) else Color.White
        )
    }
}