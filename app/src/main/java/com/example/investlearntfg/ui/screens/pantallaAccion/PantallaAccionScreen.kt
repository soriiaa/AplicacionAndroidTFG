package com.example.investlearntfg.ui.screens.pantallaAccion

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.investlearntfg.data.model.EmpresaPreview
import com.example.investlearntfg.ui.components.BotonAnadirFavoritoPredeterminado
import com.example.investlearntfg.ui.components.BotonVolverAtrasPredeterminado
import com.example.investlearntfg.ui.components.GraficoPredeterminado
import com.example.investlearntfg.ui.components.ImagenAccionPredeterminada
import com.example.investlearntfg.ui.components.LogoAplicacionPulsable

@Composable
fun PantallaAccionScreen(
    navController: NavController,
    viewModel: PantallaAccionViewModel = hiltViewModel()
) {

    val focusManager = LocalFocusManager.current
    val empresa by viewModel.empresa.collectAsState()
    val velas by viewModel.velas.collectAsState()

    var periodoSeleccionado by remember { mutableStateOf("D") }

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
                modifier = Modifier.align(Alignment.Center)
            ) {
                LogoAplicacionPulsable {
                    // Aquí va el onClick
                }
            }
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 5.dp)
            ) {

                // TODO ESTO
                BotonAnadirFavoritoPredeterminado(false) { }
            }
        }

        Box(
            modifier = Modifier
                .weight(1.5f)
                .fillMaxWidth()
                .padding(top = 15.dp),
            contentAlignment = Alignment.Center
        ) {
            empresa?.let {
                ImagenAccionPredeterminada(it.logo)
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
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Box(
            modifier = Modifier
                .weight(0.5f)
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
                .weight(4.5f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            velas?.let { GraficoPredeterminado(it) }
        }

        Box(
            modifier = Modifier
                .weight(2f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {

        }
    }
}

@Composable
fun SelectorPeriodo(
    seleccionado: String,
    alSeleccionar: (String) -> Unit,
    opciones: List<String> = listOf("H", "D", "M")
) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .background(Color.LightGray, shape = RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp)),
        horizontalArrangement = Arrangement.Center
    ) {
        opciones.forEachIndexed { indice, opcion ->
            val estaSeleccionado = opcion == seleccionado
            TextButton(
                onClick = { alSeleccionar(opcion) },
                modifier = Modifier
                    .weight(1f)
                    .then(
                        if (indice == 0) Modifier.clip(
                            RoundedCornerShape(
                                topStart = 12.dp,
                                bottomStart = 12.dp
                            )
                        )
                        else if (indice == opciones.lastIndex) Modifier.clip(
                            RoundedCornerShape(
                                topEnd = 12.dp,
                                bottomEnd = 12.dp
                            )
                        )
                        else Modifier
                    )
                    .background(if (estaSeleccionado) Color.Blue else Color.Transparent)
            ) {
                Text(
                    text = opcion,
                    color = if (estaSeleccionado) Color.White else Color.Black,
                    fontWeight = if (estaSeleccionado) FontWeight.Bold else FontWeight.Normal
                )
            }
            if (indice < opciones.lastIndex) {
                Divider(
                    color = Color.Gray,
                    modifier = Modifier
                        .width(1.dp)
                        .fillMaxHeight()
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewPantallaAccionScreen() {
    val navController = rememberNavController()
    val empresaEjemplo = EmpresaPreview(
        ticker = "AAPL",
        nombre = "Apple",
        logo = "https://static2.finnhub.io/file/publicdatany/finnhubimage/stock_logo/AAPL.png",
        precio = 192.50,
        simboloMoneda = "USD"
    )

    PantallaAccionScreen(navController = navController)
}