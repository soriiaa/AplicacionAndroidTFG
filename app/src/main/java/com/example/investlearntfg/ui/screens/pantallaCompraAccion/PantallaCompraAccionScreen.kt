package com.example.investlearntfg.ui.screens.pantallaCompraAccion

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.investlearntfg.ui.components.SelectorCantidadAcciones
import com.example.investlearntfg.ui.theme.color5
import kotlinx.coroutines.delay

@Composable
fun PantallaCompraAccionScreen(
    navController: NavController,
    viewModel: PantallaCompraAccionViewModel = hiltViewModel(),
    precioActual: Double = 176.25,
    onComprarClick: (cantidad: Int) -> Unit = {}
) {
    val focusManager = LocalFocusManager.current
    var cantidad by remember { mutableStateOf(1) }
    val totalUSD = precioActual * cantidad


    val empresa by viewModel.empresa.collectAsState()
    val datosPrecioAccion by viewModel.precioCompania2.collectAsState()
    val dineroDisponible = viewModel.dineroCuenta.collectAsState()
    val simboloMonedaUsuario by viewModel.simboloMonedaUsuario.collectAsState()
    val tipoCambio by viewModel.tipoCambio.collectAsState()


    val tarjetaColor = color5


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
            .padding(24.dp)
            .pointerInput(Unit) {
                detectTapGestures(onTap = { focusManager.clearFocus() })
            },
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Comprar acciones de",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
            empresa?.let {
                Text(
                    text = it.nombre,
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = tarjetaColor),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    val codigoMoneda = empresa?.let { viewModel.obtenerCodigoMoneda(it.simboloMoneda) }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Precio actual ($codigoMoneda)",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = datosPrecioAccion?.let { "%.2f".format(it.c) }?.let { "$it" } ?: "-",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            datosPrecioAccion?.let { precio ->
                                Text(
                                    text = "%.2f".format(precio.d),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = if (precio.dp >= 0) "+${"%.2f".format(precio.dp)}%" else "${"%.2f".format(precio.dp)}%",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = if (precio.dp >= 0)
                                        androidx.compose.ui.graphics.Color(0xFF0DCB11)
                                    else
                                        androidx.compose.ui.graphics.Color(0xFFF44336)
                                )
                            }
                        }
                    }
                }
            }



            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Saldo disponible: ${"%,.2f".format(dineroDisponible.value)} $simboloMonedaUsuario",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            if (empresa?.simboloMoneda != simboloMonedaUsuario) {
                Text(
                    text = "Conversión automática: 1 $simboloMonedaUsuario ≈ ${"%.2f".format(tipoCambio)} ${empresa?.simboloMoneda}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            }
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.height(8.dp))

            SelectorCantidadAcciones(
                cantidadInicial = cantidad,
                onCantidadCambio = { nuevaCantidad -> cantidad = nuevaCantidad },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Total estimado a invertir",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "$${"%.2f".format(totalUSD)} ≈ ${"%.2f".format(1.0)} €",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = {
                    if (cantidad > 0) {
                        onComprarClick(cantidad)
                        navController.popBackStack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Comprar", style = MaterialTheme.typography.titleMedium)
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
