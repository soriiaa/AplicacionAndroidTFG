package com.example.investlearntfg.ui.screens.pantallaAccion

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.investlearntfg.R
import com.example.investlearntfg.data.model.CompanyProfile2Response
import com.example.investlearntfg.ui.components.BotonAnadirFavoritoPredeterminado
import com.example.investlearntfg.ui.components.BotonVolverAtrasPredeterminado
import com.example.investlearntfg.ui.components.BotonesComprarVender
import com.example.investlearntfg.ui.components.GraficoPredeterminado
import com.example.investlearntfg.ui.components.ImagenAccionPredeterminada
import com.example.investlearntfg.ui.navigation.Destinations
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PantallaAccionScreen(
    navController: NavController,
    viewModel: PantallaAccionViewModel = hiltViewModel()
) {

    val focusManager = LocalFocusManager.current
    val empresa by viewModel.empresa.collectAsState()
    val velas by viewModel.velas.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var periodoSeleccionado by remember { mutableStateOf("D") }
    val dineroDisponible = viewModel.dineroCuenta.collectAsState()
    val simboloMoneda by viewModel.simboloMonedaUsuario.collectAsState()
    val perfilCompletoEmpresa by viewModel.perfilCompletoEmpresa.collectAsState()
    val esFavorita by viewModel.esEmpresaFavorita.collectAsState()
    val datosPrecioAccion by viewModel.precioCompania2.collectAsState()
    var mostrarAlertaSinAcciones by remember { mutableStateOf(false) }

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
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.TopCenter
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 5.dp)
            ) {
                BotonVolverAtrasPredeterminado(navController)
            }
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(top = 25.dp)
            ) {
                empresa?.let {
                    ImagenAccionPredeterminada(it.logo)
                }
            }
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 5.dp)
            ) {
                BotonAnadirFavoritoPredeterminado(esFavorita) {
                    viewModel.alternarFavorito()
                }
            }
        }

        Box(
            modifier = Modifier
                .weight(0.7f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            empresa?.let {
                Text(
                    text = it.nombre,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
            }
        }

        Box(
            modifier = Modifier
                .weight(0.6f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            SelectorPeriodo(
                seleccionado = periodoSeleccionado,
                alSeleccionar = { nuevo ->
                    periodoSeleccionado = nuevo
                    viewModel.traerVelasAccion(nuevo)
                }
            )
        }

        Box(
            modifier = Modifier
                .weight(4.6f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                velas?.let { GraficoPredeterminado(it) }
            }
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)) {
                                append("Capital: ")
                            }
                            append(String.format("%.2f", dineroDisponible.value) + " $simboloMoneda")
                        },
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)) {
                                append("Precio: ")
                            }
                            append("${datosPrecioAccion?.c} ${empresa?.simboloMoneda}")
                        },
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                val variacion = datosPrecioAccion?.dp

                val variacionColor = when {
                    variacion == null -> Color.Gray
                    variacion >= 0 -> Color(0xFF4CAF50)
                    else -> Color(0xFFF44336)
                }

                val signo = if (variacion != null && variacion >= 0) "+" else ""

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(variacionColor.copy(alpha = 0.15f))
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "$signo${"%.2f".format(variacion)}%",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = variacionColor
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            val empresaJson = Uri.encode(Gson().toJson(empresa))

            BotonesComprarVender(
                onComprarClick = {
                    navController.navigate("${Destinations.PANTALLA_COMPRA_ACCION_SCREEN}/$empresaJson")
                },
                onVenderClick = {
                    viewModel.viewModelScope.launch {
                        if (viewModel.puedeVender()) {
                            navController.navigate("${Destinations.PANTALLA_VENTA_ACCION_SCREEN}/$empresaJson")
                        } else {
                            mostrarAlertaSinAcciones = true
                        }
                    }
                }
            )
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            DesplegableInformacionAccion(perfilCompletoEmpresa)
        }
    }

    if (mostrarAlertaSinAcciones) {
        AlertDialog(
            onDismissRequest = { mostrarAlertaSinAcciones = false },
            title = {
                Text("Acciones no disponibles")
            },
            text = {
                Text("Parece que no tienes acciones disponibles de esta empresa en tu portafolio. Solo puedes vender acciones que posees actualmente.")
            },
            confirmButton = {
                TextButton(onClick = { mostrarAlertaSinAcciones = false }) {
                    Text("Entendido")
                }
            },
            containerColor = colorResource(R.color.color7)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DesplegableInformacionAccion(perfil: CompanyProfile2Response?) {
    val estadoHoja = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var mostrarHoja by remember { mutableStateOf(false) }

    if (mostrarHoja) {
        ModalBottomSheet(
            onDismissRequest = {
                scope.launch {
                    estadoHoja.hide()
                    mostrarHoja = false
                }
            },
            sheetState = estadoHoja,
            containerColor = colorResource(id = R.color.backgroundColor)
        ) {
            if (perfil != null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        perfil.logo?.let { url ->
                            AsyncImage(
                                model = url,
                                contentDescription = "Logo",
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Fit
                            )
                        }
                        Column {
                            Text(
                                text = perfil.name ?: "Nombre no disponible",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = perfil.ticker ?: "",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                        }
                    }

                    Divider()

                    InfoFila("Industria", perfil.finnhubIndustry)
                    InfoFila("País", perfil.country)
                    InfoFila("Moneda", perfil.currency)
                    InfoFila("Bolsa", perfil.exchange)
                    InfoFila("Capitalización", perfil.marketCapitalization?.toString())
                    InfoFila("Acciones", perfil.shareOutstanding?.toString())
                    InfoFila("IPO", perfil.ipo)
                    InfoFila("Web", perfil.weburl)
                    InfoFila("Teléfono", perfil.phone)

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            scope.launch {
                                estadoHoja.hide()
                                mostrarHoja = false
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Cerrar")
                    }
                }
            } else {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text("No hay información disponible.")
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            scope.launch {
                                estadoHoja.hide()
                                mostrarHoja = false
                            }
                        }
                    ) {
                        Text("Cerrar")
                    }
                }
            }
        }
    }

    Button(
        onClick = {
            scope.launch {
                mostrarHoja = true
                estadoHoja.show()
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(48.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(Color(0xFF2196F3)) // Azul
    ) {
        Text(
            "Detalles",
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontSize = 14.sp
        )
    }


}

@Composable
fun InfoFila(etiqueta: String, valor: String?) {
    if (!valor.isNullOrBlank()) {
        val contexto = LocalContext.current
        val esUrl = valor.startsWith("http://") || valor.startsWith("https://")

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "$etiqueta:",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )

            if (esUrl) {
                ClickableText(
                    text = AnnotatedString(
                        text = valor,
                        spanStyle = SpanStyle(
                            color = Color(0xFF64B5F6), // azul moderno
                            textDecoration = TextDecoration.Underline
                        )
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(valor))
                        contexto.startActivity(intent)
                    }
                )
            } else {
                Text(
                    text = valor,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.LightGray
                )
            }
        }
    }
}

@Composable
fun SelectorPeriodo(
    seleccionado: String,
    alSeleccionar: (String) -> Unit,
    opciones: List<String> = listOf("D", "S", "M")
) {
    Row(
        modifier = Modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        opciones.forEachIndexed { index, opcion ->
            val estaSeleccionado = opcion == seleccionado

            Box(
                modifier = Modifier
                    .clickable { alSeleccionar(opcion) }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = opcion,
                        color = if (estaSeleccionado) Color.White else Color.LightGray,
                        fontWeight = if (estaSeleccionado) FontWeight.Bold else FontWeight.Normal
                    )
                    Box(
                        modifier = Modifier
                            .height(2.dp)
                            .width(20.dp)
                            .background(
                                if (estaSeleccionado) Color(0xFF2196F3) else Color.Transparent
                            )
                    )
                }
            }

            if (index < opciones.lastIndex) {
                Text(
                    text = " | ",
                    color = Color.LightGray,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        }
    }
}