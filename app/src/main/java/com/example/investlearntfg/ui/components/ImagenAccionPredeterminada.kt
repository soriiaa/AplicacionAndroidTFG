package com.example.investlearntfg.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.investlearntfg.data.model.EmpresaPreview

@Composable
fun ImagenAccionPredeterminada(logo: String) {
    Image(
        painter = rememberAsyncImagePainter(logo),
        contentDescription = "logo",
        modifier = Modifier
            .size(100.dp)
            .padding(8.dp)
            .clip(CircleShape),
        contentScale = ContentScale.Crop
    )
}
