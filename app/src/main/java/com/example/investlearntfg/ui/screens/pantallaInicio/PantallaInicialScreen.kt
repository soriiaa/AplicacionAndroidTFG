package com.example.investlearntfg.ui.screens.pantallaInicio

import android.net.Uri
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.investlearntfg.R
import com.example.investlearntfg.data.model.EmpresaPreview
import com.example.investlearntfg.ui.components.BottomNavigationBarPredeterminado
import com.example.investlearntfg.ui.components.CardAccionPredeterminado1
import com.example.investlearntfg.ui.components.LogoAplicacionPulsable
import com.example.investlearntfg.ui.navigation.Destinations
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun PantallaInicialScreen(
    navController: NavHostController,
    viewModel: PantallaInicialViewModel = hiltViewModel(),
    onLogout: () -> Unit
) {

    val focusManager = LocalFocusManager.current
    val entradaActualDeNavegacion = remember { navController.currentBackStackEntryFlow }.collectAsState(null)

    val dineroEnCuenta by viewModel.dineroEnCuenta.collectAsState()
    val simboloMonedaUsuario by viewModel.simboloMoneda.collectAsState()
    val gananciaUsuario by viewModel.gananciaUsuario.collectAsState()
    val totalTransacciones by viewModel.totalTransacciones.collectAsState()

    val empresasFavoritas by viewModel.empresasFavoritas.collectAsState()
    val cargando by viewModel.cargandoAccionesFavoritas.collectAsState()
    val listaEmpresasFavoritasPreview by viewModel.listaEmpresasFavoritasPreview.collectAsState()

    val fechaHoy: String = LocalDate.now().format(DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy"))

    LaunchedEffect(entradaActualDeNavegacion.value) {
        viewModel.cargarEstadisticas()
        viewModel.cargarAccionesFavoritas()
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
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.TopCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .align(Alignment.CenterStart)
                    .padding(start = 5.dp),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = {
                    viewModel.cerrarSesion()
                    onLogout()
                }) {
                    Icon(
                        imageVector = Icons.Default.Logout,
                        contentDescription = "Cerrar sesión",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .align(Alignment.Center),
                contentAlignment = Alignment.Center
            ) {
                LogoAplicacionPulsable {
                    viewModel.recargarPantalla()
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .align(Alignment.CenterEnd)
                    .padding(end = 5.dp),
                contentAlignment = Alignment.Center
            ) {
                PantallaConDesplegable()
            }
        }

        Box(
            modifier = Modifier
                .weight(2.1f)
                .fillMaxWidth()
                .padding(start = 5.dp, end = 5.dp)
        ) {
            EstadisticasPantallaInicio(dineroEnCuenta, simboloMonedaUsuario, gananciaUsuario, totalTransacciones, fechaHoy)
        }

        Box(
            modifier = Modifier
                .weight(3.1f)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "Acciones favoritas",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp,
                        color = Color.White
                    ),
                    modifier = Modifier.padding(start = 17.dp, bottom = 17.dp, top = 5.dp)
                )

                AccionesFavoritas(empresasFavoritas, cargando, listaEmpresasFavoritasPreview, navController, viewModel)
            }
        }

        BottomNavigationBarPredeterminado(navController)
    }
}

@Composable
fun AccionesFavoritas(
    empresasFavoritas: Set<String>,
    cargando: Boolean,
    listaEmpresasFavoritasPreview: List<EmpresaPreview>,
    navController: NavHostController,
    viewModel: PantallaInicialViewModel
) {
    if (empresasFavoritas.isNotEmpty() && !cargando) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(listaEmpresasFavoritasPreview) { empresa ->

                val empresaJson = Uri.encode(Gson().toJson(empresa))

                CardAccionPredeterminado1(
                    empresa = empresa,
                    esFavorita = empresasFavoritas.contains(empresa.ticker),
                    onClickFavorito = { viewModel.alternarFavorito(empresa) },
                    onClickCard = {
                        navController.navigate("${Destinations.PANTALLA_ACCION_SCREEN}/$empresaJson")
                    }
                )
            }
        }

    } else if (empresasFavoritas.isEmpty()) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.FavoriteBorder,
                    contentDescription = "Icono de favorito vacío",
                    tint = Color.Gray,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Esto está muy vacío 😕",
                    color = Color.LightGray,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Añade algunas acciones favoritas para tenerlas aquí.",
                    color = Color.LightGray,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 100.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            CircularProgressIndicator(color = Color.White)
        }
    }
}

@Composable
fun EstadisticasPantallaInicio(
    dineroEnCuenta: Double,
    simboloMonedaUsuario: String,
    gananciasTotalesUsuario: Double,
    totalTransacciones: Int,
    fechaHoy: String
) {
    val titleFontSize = 18.sp
    val valueFontSize = 14.sp

    val titleStyle = MaterialTheme.typography.titleMedium.copy(
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        fontSize = titleFontSize
    )
    val valueStyle = MaterialTheme.typography.bodyMedium.copy(
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        fontSize = valueFontSize
    )


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier
                .weight(0.8f)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.color5)),
                elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 20.dp),
                    contentAlignment = Alignment.TopStart
                ) {
                    Column(horizontalAlignment = Alignment.Start) {
                        Text(
                            text = "Tu dinero",
                            style = titleStyle.copy(color = Color.White)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        val dineroFormateado = String.format("%.2f", dineroEnCuenta)
                        Text(
                            text = "$dineroFormateado $simboloMonedaUsuario",
                            style = valueStyle.copy(color = Color.White)
                        )
                    }
                }
            }

            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.color5)),
                elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 20.dp),
                    contentAlignment = Alignment.TopStart
                ) {
                    Column(horizontalAlignment = Alignment.Start) {
                        Text(
                            text = "Ganancia",
                            style = titleStyle.copy(color = Color.White)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "${String.format("%.2f", gananciasTotalesUsuario)} $simboloMonedaUsuario",
                            style = valueStyle.copy(color = Color.White)
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .weight(0.8f)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.color5)),
                elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 20.dp),
                    contentAlignment = Alignment.TopStart
                ) {
                    Column(horizontalAlignment = Alignment.Start) {
                        Text(
                            text = "Movimientos",
                            style = titleStyle.copy(color = Color.White)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = totalTransacciones.toString(),
                            style = valueStyle.copy(color = Color.White)
                        )
                    }
                }
            }

            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.color5)),
                elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 20.dp),
                    contentAlignment = Alignment.TopStart
                ) {
                    Column(horizontalAlignment = Alignment.Start) {
                        Text(
                            text = "Hoy es",
                            style = titleStyle.copy(color = Color.White)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = fechaHoy,
                            style = valueStyle.copy(color = Color.White)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaConDesplegable() {
    val estadoHoja = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var mostrarHoja by remember { mutableStateOf(false) }

    // Mostrar el ModalBottomSheet si mostrarHoja es true
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 600.dp)
                    .padding(horizontal = 20.dp, vertical = 28.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(32.dp)
                ) {
                    Text(
                        text = "¿Cómo funciona la app?",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )

                    // Dinero inicial y objetivo
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Dinero inicial y objetivo",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                        Text(
                            text = "Empiezas con 5000 € (o su equivalente en otras monedas) para invertir como tú quieras. Es una simulación pensada para que aprendas a invertir, sin riesgo real, pero con una experiencia lo más parecida posible a la realidad.",
                            fontSize = 15.sp,
                            lineHeight = 20.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }

                    // Pantalla principal
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Pantalla principal",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                        Text(
                            text = "Aquí ves tus estadísticas clave: tu saldo actual, las acciones que más te interesan, tus últimos movimientos y la fecha actual. Todo diseñado para que te sitúes de un vistazo.",
                            fontSize = 15.sp,
                            lineHeight = 20.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }

                    // Pantalla de búsqueda
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Pantalla de búsqueda",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                        Text(
                            text = "Busca acciones de todo el mundo, explora algunas destacadas y márcalas como favoritas para tenerlas siempre a mano. Perfecto para descubrir nuevas empresas.",
                            fontSize = 15.sp,
                            lineHeight = 20.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }

                    // Pantalla del perfil
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Pantalla del perfil",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                        Text(
                            text = "Consulta las acciones que has comprado, revisa tu historial de movimientos y accede a tres botones: uno para editar tu perfil, otro para cambiar tu foto y otro para ir a la configuración.",
                            fontSize = 15.sp,
                            lineHeight = 20.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }

                    // Pantalla de una acción
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Pantalla de la acción",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                        Text(
                            text = "Podrás ver un gráfico de la acción (último día, semana o mes), algo de información relevante y dos opciones: comprar o vender. Si no tienes acciones de esa empresa, el botón de vender estará desactivado.",
                            fontSize = 15.sp,
                            lineHeight = 20.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxHeight()
            .padding(end = 5.dp),
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            onClick = {
                scope.launch {
                    mostrarHoja = true
                    estadoHoja.show()
                }
            }
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Información",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}