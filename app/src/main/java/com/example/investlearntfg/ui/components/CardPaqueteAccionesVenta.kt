package com.example.investlearntfg.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.investlearntfg.data.model.EmpresaPreview
import com.example.investlearntfg.data.model.PaqueteAcciones

@Composable
fun CardPaqueteAccionesVenta(
    accion: EmpresaPreview,
    paquete: PaqueteAcciones,
    isSelected: Boolean,
    onSelectionChange: (Boolean) -> Unit
) {
    val gananciaPorcentaje = ((accion.precio - paquete.precioCompra) / paquete.precioCompra) * 100
    val gananciaNeta = (accion.precio - paquete.precioCompra) * paquete.unidades

    val color = when {
        gananciaPorcentaje > 0 -> Color(0xFF4CAF50)
        gananciaPorcentaje < 0 -> Color(0xFFF44336)
        else -> Color.Gray
    }

    val simboloMoneda = accion.simboloMoneda

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onSelectionChange(!isSelected) },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isSelected,
                onCheckedChange = { onSelectionChange(!isSelected) },
                modifier = Modifier.size(24.dp),
                colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary)
            )

            Spacer(Modifier.width(24.dp))

            Column(Modifier.weight(1f)) {
                Text(
                    "${accion.ticker} - ${accion.nombre}",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    "Fecha compra: ${paquete.fecha}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )

                Text(
                    "Precio compra: %.2f $simboloMoneda".format(paquete.precioCompra),
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    "Ganancia neta: %.2f $simboloMoneda".format(gananciaNeta),
                    style = MaterialTheme.typography.bodyMedium,
                    color = color
                )

                Text("Unidades: ${paquete.unidades}", style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(Modifier.width(12.dp))

            Text(
                "%.2f%%".format(gananciaPorcentaje),
                color = color,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}