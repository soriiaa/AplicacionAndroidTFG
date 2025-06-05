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
import androidx.compose.material3.AlertDialog
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

@Composable
fun PantallaConfiguracionScreen(
    navController: NavController,
    viewModel: PantallaConfiguracionViewModel = hiltViewModel()
) {

    val focusManager = LocalFocusManager.current
    val entradaActualDeNavegacion = remember { navController.currentBackStackEntryFlow }.collectAsState(null)

    val monedaUsuario by viewModel.monedaInicialUsuario.collectAsState()
    val botonActivado by viewModel.botonActivado.collectAsState()

    val mostrarDialogoMoneda by viewModel.mostrarDialogoMoneda.collectAsState()


    LaunchedEffect(entradaActualDeNavegacion.value) {
        viewModel.cargarMonedaActualUsuario()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.backgroundColor))
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
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
            Button(
                onClick = { viewModel.actualizarMonedaPrincipal() },
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.color4)),
                enabled = botonActivado,
                modifier = Modifier
                    .height(40.dp)
                    .width(90.dp)
                    .align(Alignment.CenterEnd)
                    .padding(end = 15.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(6.dp)
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
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
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

        Box(
            modifier = Modifier
                .weight(3f)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { /* TODO: Cambiar dirección de correo */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(6.dp)
                ) {
                    Text(
                        text = "Cambiar dirección de correo",
                        fontSize = 15.sp
                    )
                }

                Button(
                    onClick = { /* TODO: Cambiar contraseña */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(6.dp)
                ) {
                    Text(
                        text = "Cambiar contraseña",
                        fontSize = 15.sp
                    )
                }

                Button(
                    onClick = { /* TODO: Eliminar cuenta */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(6.dp)
                ) {
                    Text(
                        text = "Eliminar cuenta",
                        fontSize = 15.sp,
                        color = Color.White
                    )
                }

            }
        }
    }

    if (mostrarDialogoMoneda.isNotEmpty()) {
        AlertDialog(
            onDismissRequest = { viewModel.ocultarDialogoMoneda() },
            title = {
                Text(
                    if (mostrarDialogoMoneda == "exito") "Éxito" else "Error"
                )
            },
            text = {
                Text(
                    if (mostrarDialogoMoneda == "exito") "Moneda principal actualizada correctamente."
                    else "Error al actualizar la moneda."
                )
            },
            confirmButton = {
                Button(onClick = { viewModel.ocultarDialogoMoneda() }) {
                    Text("Aceptar")
                }
            },
            containerColor = colorResource(id = R.color.color7)
        )
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