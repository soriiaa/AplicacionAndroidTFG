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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.investlearntfg.R
import com.example.investlearntfg.ui.components.BottomNavigationBarPredeterminado
import com.example.investlearntfg.ui.components.CardAccionPredeterminado1
import com.example.investlearntfg.ui.components.LogoAplicacionPulsable
import com.example.investlearntfg.ui.navigation.Destinations
import com.google.gson.Gson
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
    val accionesEnPropiedad by viewModel.accionesEnPropiedad.collectAsState()
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
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Información",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .weight(2.1f)
                .fillMaxWidth()
                .padding(start = 5.dp, end = 5.dp)
        ) {
            EstadisticasPantallaInicio(dineroEnCuenta, simboloMonedaUsuario, accionesEnPropiedad, totalTransacciones, fechaHoy)
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
                    modifier = Modifier.padding(start = 17.dp, bottom = 12.dp)
                )

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
        }

        BottomNavigationBarPredeterminado(navController)
    }
}

@Composable
fun EstadisticasPantallaInicio(
    dineroEnCuenta: Double,
    simboloMonedaUsuario: String,
    accionesEnPropiedad: Int,
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
                            text = "Tus acciones",
                            style = titleStyle.copy(color = Color.White)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = accionesEnPropiedad.toString(),
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



