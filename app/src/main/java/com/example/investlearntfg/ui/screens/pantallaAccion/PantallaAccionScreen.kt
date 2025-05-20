package com.example.investlearntfg.ui.screens.pantallaAccion

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.investlearntfg.R
import com.example.investlearntfg.data.model.EmpresaPreview
import com.example.investlearntfg.ui.components.BotonAnadirFavoritoPredeterminado
import com.example.investlearntfg.ui.components.BotonVolverAtrasPredeterminado
import com.example.investlearntfg.ui.components.BotonesComprarVender
import com.example.investlearntfg.ui.components.GraficoPredeterminado
import com.example.investlearntfg.ui.components.ImagenAccionPredeterminada
import com.example.investlearntfg.ui.components.LogoAplicacionPulsable
import com.example.investlearntfg.ui.components.SelectorCantidadAcciones
import com.example.investlearntfg.ui.theme.backgroundColor
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
    val simboloMoneda by viewModel.simboloMoneda.collectAsState()

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

                // TODO ESTO
                BotonAnadirFavoritoPredeterminado(false) { }
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
                .weight(4f)
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
                .weight(0.5f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("Capital disponible: ")
                    }
                    append("${dineroDisponible.value} $simboloMoneda")
                }
            )
        }
        Box(
            modifier = Modifier
                .weight(1.2f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            BotonesComprarVender()
        }
        Box(
            modifier = Modifier
                .weight(1.2f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            DesplegableInformacionAccion()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DesplegableInformacionAccion() {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )
    val scope = rememberCoroutineScope()
    var showSheet by remember { mutableStateOf(false) }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                scope.launch {
                    sheetState.hide()
                    showSheet = false
                }
            },
            sheetState = sheetState,
            containerColor = colorResource(id = R.color.backgroundColor)
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.9f)
                    .padding(16.dp)
            ) {
                Text(text = "Este es el contenido del bottom sheet")
                Spacer(Modifier.height(8.dp))
                Button(onClick = {
                    scope.launch {
                        sheetState.hide()
                        showSheet = false
                    }
                }) {
                    Text("Cerrar")
                }
            }
        }
    }
    Button(onClick = {
        scope.launch {
            showSheet = true
            sheetState.show()
        }
    }) {
        Text("Detalles")
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