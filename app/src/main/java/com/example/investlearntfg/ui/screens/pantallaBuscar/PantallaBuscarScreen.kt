package com.example.investlearntfg.ui.screens.pantallaBuscar

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.investlearntfg.R
import com.example.investlearntfg.ui.components.BottomNavigationBarPredeterminado
import com.example.investlearntfg.ui.components.CardAccionPredeterminado1
import com.example.investlearntfg.ui.components.LogoAplicacionPulsable
import com.example.investlearntfg.ui.components.TextFieldPredeterminado2Redondeado
import com.example.investlearntfg.ui.navigation.Destinations
import com.google.gson.Gson

@Composable
fun PantallaBuscarScreen(
    navController: NavHostController,
    viewModel: PantallaBuscarViewModel = hiltViewModel()
) {

    val focusManager = LocalFocusManager.current
    val textoBuscador by viewModel.textoBuscador.collectAsState()
    val empresas by viewModel.empresas.collectAsState()
    val cargando by viewModel.cargando.collectAsState()
    val empresasFavoritas by viewModel.empresasFavoritas.collectAsState()
    var buscadorActivo by remember { mutableStateOf(false) }
    val empresasBusqueda by viewModel.empresasBusqueda.collectAsState()

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
            androidx.compose.animation.AnimatedVisibility(visible = !buscadorActivo) {
                LogoAplicacionPulsable {
                    viewModel.cargarEmpresasDestacadas()
                    viewModel.cargarFavoritos()
                }
            }
        }

        Box(
            modifier = Modifier
                .weight(0.5f)
                .fillMaxWidth(),
            contentAlignment = Alignment.TopCenter
        ) {

        }


        Box(
            modifier = Modifier
                .weight(1.5f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            TextFieldPredeterminado2Redondeado(
                textoInicial = stringResource(R.string.texto_buscador),
                textoEscrito = textoBuscador,
                onValueChange = { nuevoTexto ->
                    viewModel.onTextoBuscadorChange(nuevoTexto)
                },
                modifier = Modifier
                    .onFocusChanged { focusState ->
                        buscadorActivo = focusState.isFocused
                    }
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(start = 40.dp),
            contentAlignment = Alignment.BottomStart
        ) {
            androidx.compose.animation.AnimatedVisibility(visible = !buscadorActivo) {
                Text(
                    text = "Acciones destacadas",
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Box(
            modifier = Modifier
                .weight(10f)
                .fillMaxWidth(),
            contentAlignment = Alignment.TopCenter
        ) {

            androidx.compose.animation.AnimatedVisibility(visible = buscadorActivo) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .background(Color(0xFF1F1F1F), shape = RoundedCornerShape(12.dp))
                        .padding(8.dp)
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 250.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(empresasBusqueda) { empresa ->

                            val empresaJson = Uri.encode(Gson().toJson(empresa))

                            CardAccionPredeterminado1(
                                empresa = empresa,
                                esFavorita = empresasFavoritas.contains(empresa.ticker),
                                onClickFavorito = { viewModel.alternarFavorito(empresa) },
                                onClickCard = {
                                    navController.navigate("${Destinations.PANTALLA_ACCION_SCREEN}/$empresaJson")
                                    focusManager.clearFocus()
                                }
                            )
                        }
                    }
                }


            }

            androidx.compose.animation.AnimatedVisibility(visible = !buscadorActivo) {

                if (empresas.isNotEmpty() && !cargando) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(empresas) { empresa ->

                            val empresaJson = Uri.encode(Gson().toJson(empresa))

                            CardAccionPredeterminado1(
                                empresa = empresa,
                                esFavorita = empresasFavoritas.contains(empresa.ticker),
                                onClickFavorito = { viewModel.alternarFavorito(empresa) },
                                onClickCard = { navController.navigate("${Destinations.PANTALLA_ACCION_SCREEN}/$empresaJson") }
                            )
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .padding(top = 170.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.BottomCenter),
                            color = Color.White
                        )
                    }
                }
            }
        }
        BottomNavigationBarPredeterminado(navController)
    }
}