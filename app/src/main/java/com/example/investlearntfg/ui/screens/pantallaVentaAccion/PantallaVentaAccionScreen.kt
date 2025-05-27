package com.example.investlearntfg.ui.screens.pantallaVentaAccion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.investlearntfg.data.model.AccionPropiedad

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaVenderAccion(
    accion: AccionPropiedad,
    onVolver: () -> Unit
) {
    var cantidadAVender by remember { mutableIntStateOf(1) }

    val maxUnidades = accion.unidades
    val gananciaActual = (accion.precioActual - accion.precioCompra) * cantidadAVender
    val colorGanancia = when {
        gananciaActual > 0 -> Color(0xFF4CAF50)
        gananciaActual < 0 -> Color(0xFFF44336)
        else -> Color.Gray
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Vender acción") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = accion.fotoUrl,
                    contentDescription = "Logo ${accion.ticker}",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.width(16.dp))
                Column {
                    Text("${accion.ticker} - ${accion.nombre.orEmpty()}", style = MaterialTheme.typography.titleLarge)
                    Text("Disponibles: ${accion.unidades}", color = Color.Gray)
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Selecciona cuántas acciones quieres vender", style = MaterialTheme.typography.bodyMedium)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    IconButton(
                        onClick = { if (cantidadAVender > 1) cantidadAVender-- },
                        enabled = cantidadAVender > 1
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "Disminuir")
                    }
                    Text(cantidadAVender.toString(), style = MaterialTheme.typography.titleLarge)
                    IconButton(
                        onClick = { if (cantidadAVender < maxUnidades) cantidadAVender++ },
                        enabled = cantidadAVender < maxUnidades
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Aumentar")
                    }
                }
            }

            Column {
                Text("Precio actual: ${"%.2f".format(accion.precioActual)} ${accion.moneda}", style = MaterialTheme.typography.bodyMedium)
                Text("Precio compra: ${"%.2f".format(accion.precioCompra)} ${accion.moneda}", style = MaterialTheme.typography.bodyMedium)
                Text("Ganancia estimada: ${"%.2f".format(gananciaActual)} ${accion.moneda}", style = MaterialTheme.typography.bodyMedium, color = colorGanancia)
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {  },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Vender $cantidadAVender acción${if (cantidadAVender > 1) "es" else ""}")
            }
        }
    }
}