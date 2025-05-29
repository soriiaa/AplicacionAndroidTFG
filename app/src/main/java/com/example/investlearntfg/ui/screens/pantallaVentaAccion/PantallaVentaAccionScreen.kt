package com.example.investlearntfg.ui.screens.pantallaVentaAccion

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.investlearntfg.R
import com.example.investlearntfg.data.model.AccionPropiedad
import com.example.investlearntfg.ui.components.SelectorCantidadAcciones
import com.example.investlearntfg.ui.screens.pantallaPerfil.PantallaPerfilViewModel
import com.example.investlearntfg.ui.theme.color5
import kotlinx.coroutines.delay

@Composable
fun PantallaVentaAccionScreen(
    navController: NavController,
    viewModel: PantallaVentaAccionViewModel = hiltViewModel()
) {

    val empresa by viewModel.empresa.collectAsState()
    val precioCompania2 by viewModel.precioCompania2.collectAsState()
    val dineroCuenta by viewModel.dineroCuenta.collectAsState()
    val simboloMonedaUsuarioViewModel2 by viewModel.simboloMonedaUsuario.collectAsState()
    val tipoCambioViewModel2 by viewModel.tipoCambio.collectAsState()
    val tipoCambioInversoViewModel2 by viewModel.tipoCambioInverso.collectAsState()


    val nombreAccion = empresa?.nombre ?: ""
    val simboloMonedaAccion = empresa?.let { viewModel.obtenerCodigoMoneda(it.simboloMoneda) }
    val simboloMonedaUsuario = simboloMonedaUsuarioViewModel2 ?: ""
    val tipoCambio = tipoCambioViewModel2 ?: 0.0
    val tipoCambioInverso = tipoCambioInversoViewModel2 ?: 0.0
    val precioAccion = precioCompania2?.c ?: 0.0
    val variacion = precioCompania2?.d ?: 0.0
    val variacionPorcentaje = precioCompania2?.dp ?: 0.0
    val saldoDisponible = dineroCuenta ?: 0.0
    var mostrarDialogoConfirmacion by remember { mutableStateOf(false) }


    LaunchedEffect(empresa?.ticker) {
        empresa?.ticker?.let {
            while (true) {
                viewModel.cargarPrecioAccion()
                delay(10000)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Vender acciones de",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
            Text(
                text = nombreAccion,
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = color5),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Precio actual ($simboloMonedaAccion)",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "$${"%.2f".format(precioAccion)}",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "$${"%.2f".format(variacion)}",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = if (variacionPorcentaje >= 0) "+${"%.2f".format(variacionPorcentaje)}%" else "${"%.2f".format(variacionPorcentaje)}%",
                                style = MaterialTheme.typography.bodyLarge,
                                color = if (variacionPorcentaje >= 0) Color(0xFF0DCB11) else Color(0xFFF44336)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Saldo disponible: ${"%,.2f".format(saldoDisponible)} $simboloMonedaUsuario",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Conversión automática: 1 $simboloMonedaUsuario ≈ ${"%.2f".format(tipoCambio)} $simboloMonedaAccion",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
        }

        // Parte inferior -> Botones de vender y cancelar
        Column(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Vender", style = MaterialTheme.typography.titleMedium)
            }
            TextButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                Text("Cancelar", color = MaterialTheme.colorScheme.onBackground)
            }
        }
    }


}

@Composable
fun CardPaquete(
    viewModel: PantallaPerfilViewModel,
    accion: AccionPropiedad,
    onVenderClick: () -> Unit
) {
    val ganancia = ((accion.precioActual - accion.precioCompra) / accion.precioCompra) * 100
    val color = when {
        ganancia > 0 -> Color(0xFF4CAF50)
        ganancia < 0 -> Color(0xFFF44336)
        else -> Color.Gray
    }

    Card(
        Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(
            Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = accion.fotoUrl, contentDescription = "Logo ${accion.ticker}",
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.placeholder_blanco),
                error = painterResource(R.drawable.placeholder_blanco)
            )
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    "${accion.ticker} - ${accion.nombre.orEmpty()}",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    "Comprado: ${accion.fecha}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )

                val simboloMoneda = viewModel.obtenerSimboloMoneda(accion.moneda)


                Text(
                    "Compra: ${"%.2f".format(accion.precioCompra)} $simboloMoneda",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "Actual: ${"%.2f".format(accion.precioActual)} $simboloMoneda",
                    style = MaterialTheme.typography.bodyMedium
                )

                Text("Unidades: ${accion.unidades}", style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(Modifier.width(12.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "${"%.2f".format(ganancia)}%",
                    color = color,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.height(6.dp))
                Button(
                    onClick = onVenderClick,
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text("Vender", color = Color.White, fontSize = 14.sp)
                }
            }
        }
    }
}