package com.example.investlearntfg.ui.screens.pantallaVentaAccion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.investlearntfg.R
import com.example.investlearntfg.ui.components.CardPaqueteAccionesVenta

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
    val listaPaquetesEnPosesion = viewModel.listaPaquetesEnPosesion.collectAsState()
    val paquetesSeleccionados by viewModel.paquetesSeleccionados.collectAsState()
    var mostrarConfirmacion by remember { mutableStateOf(false) }
    var mostrandoCarga by remember { mutableStateOf(false) }
    var resultadoOperacion by remember { mutableStateOf<String?>(null) }
    var titulo by remember { mutableStateOf<String?>("¿Confirmar venta?") }

    LaunchedEffect(empresa?.ticker) {
        empresa?.ticker?.let {
            while (true) {
                viewModel.cargarPrecioAccion()
                delay(10000)
            }
        }
        viewModel.obtenerPaquetesPropiedadPorTicker()
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
                text = empresa?.nombre ?: "",
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
                                text = "Precio actual (${empresa?.let { viewModel.obtenerCodigoMoneda(it.simboloMoneda) }})",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "$${"%.2f".format(precioCompania2?.c ?: 0.0)}",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "$${"%.2f".format(precioCompania2?.d ?: 0.0)}",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = if ((precioCompania2?.dp ?: 0.0) >= 0) "+${"%.2f".format(precioCompania2?.dp ?: 0.0)}%" else "${"%.2f".format(precioCompania2?.dp ?: 0.0)}%",
                                style = MaterialTheme.typography.bodyLarge,
                                color = if ((precioCompania2?.dp ?: 0.0) >= 0) Color(0xFF0DCB11) else Color(0xFFF44336)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Saldo disponible: ${"%,.2f".format(dineroCuenta ?: 0.0)} ${simboloMonedaUsuarioViewModel2 ?: ""}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Conversión automática: 1 ${simboloMonedaUsuarioViewModel2 ?: ""} ≈ ${"%.2f".format(tipoCambioViewModel2 ?: 0.0)} ${empresa?.let { viewModel.obtenerCodigoMoneda(it.simboloMoneda) }}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
        }

        Box(modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            // Esto es el lazy Column que almacena las cards con los paquetes de las acciones que se van a vender
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(listaPaquetesEnPosesion.value) { paquete ->
                    empresa?.let {
                        CardPaqueteAccionesVenta(
                            accion = it,
                            paquete = paquete,
                            isSelected = paquetesSeleccionados.contains(paquete.id),
                            onSelectionChange = { viewModel.alternarSeleccion(paquete.id) }
                        )
                    }
                }
            }
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = { mostrarConfirmacion = true },
                enabled = paquetesSeleccionados.isNotEmpty(),
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

    if (mostrarConfirmacion) {
        AlertDialog(
            onDismissRequest = {
                if (!mostrandoCarga) {
                    mostrarConfirmacion = false
                    resultadoOperacion = null
                }
            },
            title = { titulo?.let { Text(it) } },
            text = {
                when {
                    mostrandoCarga -> {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        }
                    }
                    resultadoOperacion != null -> {
                        Text(resultadoOperacion ?: "")
                    }
                    else -> {
                        Text("¿Estás seguro de que quieres vender las acciones seleccionadas?")
                    }
                }
            },
            containerColor = colorResource(R.color.color7),
            confirmButton = {
                if (!mostrandoCarga && resultadoOperacion == null) {
                    TextButton(onClick = {
                        mostrandoCarga = true
                        // Simula la venta llamando a la función en el ViewModel
                        viewModel.venderPaquetesSeleccionados(
                            onResult = { exito, mensaje ->
                                mostrandoCarga = false
                                resultadoOperacion = if (exito) "Venta realizada con éxito" else "Error: $mensaje"
                                titulo = if (exito) "Éxito" else "Error: $mensaje"
                                if (exito) {
                                    navController.popBackStack()
                                }
                            }
                        )
                    }) {
                        Text("Aceptar")
                    }
                }
            },
            dismissButton = {
                if (!mostrandoCarga) {
                    TextButton(onClick = {
                        mostrarConfirmacion = false
                        resultadoOperacion = null
                    }) {
                        Text("Aceptar")
                    }
                }
            }
        )
    }
}