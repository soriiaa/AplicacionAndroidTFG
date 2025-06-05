package com.example.investlearntfg.ui.screens.pantallaPerfil

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.investlearntfg.R
import com.example.investlearntfg.data.model.AccionPropiedad
import com.example.investlearntfg.data.model.EmpresaPreview
import com.example.investlearntfg.data.model.Transaccion
import com.example.investlearntfg.ui.components.BottomNavigationBarPredeterminado
import com.example.investlearntfg.ui.navigation.Destinations
import com.example.investlearntfg.ui.theme.color6
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun PantallaPerfilScreen(
    navController: NavHostController,
    viewModel: PantallaPerfilViewModel = hiltViewModel()
) {

    val focusManager = LocalFocusManager.current
    val entradaActualDeNavegacion = remember { navController.currentBackStackEntryFlow }.collectAsState(null)

    val nickname by viewModel.nickname.collectAsState()
    val accionesEnPropiedad = viewModel.accionesEnPropiedad.collectAsState()
    val historialTransacciones by viewModel.historialTransacciones.collectAsState()

    val cargandoAccionesEnPropiedad by viewModel.cargandoAccionesEnPropiedad.collectAsState()
    val cargandoHistorialAcciones by viewModel.cargandoHistorialAcciones.collectAsState()

    LaunchedEffect(entradaActualDeNavegacion.value) {
        viewModel.cargarAccionesEnPropiedad()
        viewModel.cargarHistorialTransacciones()
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

            // Boton Izquierda
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 5.dp)
            ) {
                BotonEditarPerfil(navController)
            }

            Box {
                Image(
                    painter = painterResource(id = R.drawable.foto_perfil_modo_oscuro),
                    contentDescription = "Foto de perfil",
                    modifier = Modifier
                        .size(130.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                IconButton(
                    onClick = { /* TODO: acción para cambiar foto */ },
                    modifier = Modifier
                        .size(36.dp)
                        .align(Alignment.BottomEnd)
                        .offset(x = (-10).dp, y = 1.dp)
                        .background(color = colorResource(id = R.color.color4), shape = CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Cambiar foto de perfil",
                        tint = Color.DarkGray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Boton derecha
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 5.dp)
            ) {
                IconButton(
                    onClick = { navController.navigate(Destinations.PANTALLA_CONFIGURACION_SCREEN) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Ir a configuración",
                        tint = Color.White
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .weight(0.15f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = nickname,
                fontSize = 18.sp
            )
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            PestanasAccionesHistorial(accionesEnPropiedad, viewModel, navController, historialTransacciones, cargandoAccionesEnPropiedad, cargandoHistorialAcciones)
        }
        BottomNavigationBarPredeterminado(navController)
    }
}

@Composable
fun BotonEditarPerfil(navController: NavController) {
    IconButton(
        onClick = { navController.navigate(Destinations.PANTALLA_EDITAR_PERFIL_SCREEN) },
        modifier = Modifier
            .size(48.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = stringResource(R.string.texto_editar_perfil),
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun PestanasAccionesHistorial(
    accionesEnPropiedad: State<List<AccionPropiedad>>,
    viewModel: PantallaPerfilViewModel,
    navController: NavHostController,
    historialTransacciones: List<Transaccion>,
    cargandoAccionesEnPropiedad: Boolean,
    cargandoHistorialAcciones: Boolean
) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Tus Acciones", "Historial")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.backgroundColor))
    ) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier
                .background(colorResource(id = R.color.backgroundColor)),
            divider = {
                Divider(
                    color = colorResource(id = R.color.color2),
                    thickness = 1.dp
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title) },
                    modifier = Modifier
                        .background(colorResource(id = R.color.backgroundColor))
                )
            }
        }

        when (selectedTabIndex) {
            0 -> {
                Acciones(accionesEnPropiedad, cargandoAccionesEnPropiedad, navController, viewModel)
            }
            1 -> {
                Historial(historialTransacciones, cargandoHistorialAcciones)
            }
        }
    }
}

@Composable
fun Acciones(
    accionesEnPropiedad: State<List<AccionPropiedad>>,
    cargandoAccionesEnPropiedad: Boolean,
    navController: NavHostController,
    viewModel: PantallaPerfilViewModel
) {
    val listaAcciones = accionesEnPropiedad.value

    when {
        cargandoAccionesEnPropiedad -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        listaAcciones.isNotEmpty() -> {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(listaAcciones) { accion ->
                    val empresaPreview = remember(accion) {
                        EmpresaPreview(
                            ticker = accion.ticker,
                            nombre = accion.nombre,
                            logo = accion.fotoUrl,
                            precio = accion.precioActual,
                            simboloMoneda = viewModel.obtenerSimboloMoneda(accion.moneda)
                        )
                    }

                    val empresaJson = remember(empresaPreview) {
                        Uri.encode(Gson().toJson(empresaPreview))
                    }

                    TarjetaAccion(
                        viewModel = viewModel,
                        accion = accion,
                        onCardClick = {
                            navController.navigate("${Destinations.PANTALLA_ACCION_SCREEN}/$empresaJson")
                        },
                        onVenderClick = {
                            navController.navigate("${Destinations.PANTALLA_VENTA_ACCION_SCREEN}/$empresaJson")
                        }
                    )
                }
            }
        }

        else -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Icono de carrito vacío",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Todavía no has comprado nada",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Cuando compres acciones, aparecerán aquí para que puedas seguir su evolución.",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun Historial(historialTransacciones: List<Transaccion>, cargandoHistorialAcciones: Boolean) {
    if (historialTransacciones.isNotEmpty() && !cargandoHistorialAcciones) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(historialTransacciones.sortedByDescending { it.fecha?.toDate() }) { transaccion ->
                CardTransaccion(transaccion)
            }
        }
    } else if (historialTransacciones.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = "Icono de historial vacío",
                    tint = Color.Gray,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Sin movimientos por ahora",
                    color = Color.LightGray,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Aquí verás todas tus compras y ventas una vez empieces a operar en el mercado.",
                    color = Color.LightGray,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun CardTransaccion(transaccion: Transaccion) {
    val fecha = transaccion.fecha?.toDate()?.let {
        SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(it)
    } ?: "Fecha desconocida"

    val colorTipo = when (transaccion.tipo_transaccion.lowercase()) {
        "compra" -> Color(0xFF4CAF50)
        "venta" -> Color(0xFFF44336)
        else -> Color.Gray
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 12.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = color6)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = transaccion.foto_accion,
                contentDescription = "Logo de la acción",
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaccion.ticker,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = buildAnnotatedString {
                        // Tipo de transacción (con color)
                        withStyle(style = SpanStyle(color = colorTipo)) {
                            append(transaccion.tipo_transaccion.replaceFirstChar { it.uppercase() })
                        }
                        append(" • ${transaccion.unidades} uds • ")
                        append("%.2f€".format(transaccion.precio_transaccion * transaccion.unidades))
                    },
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = fecha,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun TarjetaAccion(
    viewModel: PantallaPerfilViewModel,
    accion: AccionPropiedad,
    onCardClick: () -> Unit,
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
            .padding(start = 16.dp, end = 16.dp)
            .clickable(onClick = onCardClick),
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
                contentScale = ContentScale.Crop
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
