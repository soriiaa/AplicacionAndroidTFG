package com.example.investlearntfg.ui.screens.pantallaConfiguracion

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.investlearntfg.R
import com.example.investlearntfg.ui.components.BotonVolverAtrasPredeterminado
import com.example.investlearntfg.ui.components.BottomNavigationBarPredeterminado

@Composable
fun PantallaConfiguracionScreen(
    navController: NavController,
    viewModel: PantallaConfiguracionViewModel = hiltViewModel()
) {

    val focusManager = LocalFocusManager.current
    val entradaActualDeNavegacion = remember { navController.currentBackStackEntryFlow }.collectAsState(null)

    val monedaUsuario by viewModel.monedaInicialUsuario.collectAsState()
    val botonActivado by viewModel.botonActivado.collectAsState()

    LaunchedEffect(entradaActualDeNavegacion.value) {
        viewModel.cargarMonedaActualUsuario()
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
                .weight(1f)
                .fillMaxWidth()
        ) {
            BotonVolverAtrasPredeterminado(
                navController,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 5.dp)
            )
            Text(
                text = "Configuración",
                modifier = Modifier.align(Alignment.Center),
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Button(
                onClick = { viewModel.subirCambiosAFirebase() },
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.color4)),
                enabled = botonActivado,
                modifier = Modifier
                    .height(40.dp)
                    .width(90.dp)
                    .align(Alignment.CenterEnd)
                    .padding(end = 15.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Done,
                    contentDescription = "Guardar",
                    tint = Color.White
                )
            }
        }

        Box(
            modifier = Modifier
                .weight(5f)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Selecciona tu moneda principal",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                val opcionesMoneda = listOf(
                    "Dólar estadounidense - $ - USD",
                    "Euro - € - EUR",
                    "Libra esterlina - £ - GBP",
                    "Yen japonés - ¥ - JPY",
                    "Franco suizo - CHF - CHF"
                )

                DesplegableMonedaPantallaConfiguracionScreen(
                    opciones = opcionesMoneda,
                    monedaSeleccionada = monedaUsuario,
                    onSeleccionChanged = { nuevaMoneda ->
                        viewModel.setMonedaSeleccionada(nuevaMoneda)
                        viewModel.comprobarDiferenciasParaBoton()
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun DesplegableMonedaPantallaConfiguracionScreen(
    opciones: List<String>,
    monedaSeleccionada: String,
    onSeleccionChanged: (String) -> Unit
) {

    var seleccionado by remember { mutableStateOf(monedaSeleccionada) }
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(monedaSeleccionada) {
        seleccionado = monedaSeleccionada
    }

    Box(
        modifier = Modifier
            .widthIn(max = 280.dp)
            .clip(RoundedCornerShape(5.dp))
            .background(colorResource(id = R.color.color1))
            .border(2.dp, colorResource(id = R.color.color1), RoundedCornerShape(5.dp))
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier
                .background(
                    colorResource(id = R.color.color1),
                    shape = RoundedCornerShape(5.dp)
                )
                .border(2.dp, colorResource(id = R.color.color1), RoundedCornerShape(5.dp)),
        ) {
            TextField(
                value = seleccionado,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorResource(id = R.color.color1),
                    unfocusedContainerColor = colorResource(id = R.color.color1),
                    disabledContainerColor = colorResource(id = R.color.color1),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                textStyle = TextStyle(color = Color.Black, fontSize = 17.sp),
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier
                    .menuAnchor()
                    .background(colorResource(id = R.color.color1))
                    .border(2.dp, colorResource(id = R.color.color1), RoundedCornerShape(5.dp))
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                opciones.forEach { opcion ->
                    DropdownMenuItem(
                        text = { Text(opcion) },
                        onClick = {
                            seleccionado = opcion
                            onSeleccionChanged(opcion)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}