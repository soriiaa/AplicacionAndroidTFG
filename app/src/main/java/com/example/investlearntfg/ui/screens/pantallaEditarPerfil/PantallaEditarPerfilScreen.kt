package com.example.investlearntfg.ui.screens.pantallaEditarPerfil

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.investlearntfg.R
import com.example.investlearntfg.ui.components.BotonVolverAtrasPredeterminado

@Composable
fun PantallaEditarPerfilScreen(
    navController: NavController,
    viewModel: PantallaEditarPerfilScreenViewModel = hiltViewModel()
) {

    val focusManager = LocalFocusManager.current
    val entradaActualDeNavegacion = remember { navController.currentBackStackEntryFlow }.collectAsState(null)

    val nicknameIntroducido by viewModel.nicknameIntroducido.collectAsState()
    val nombreIntroducido by viewModel.nombreIntroducido.collectAsState()
    val apellidosIntroducido by viewModel.apellidosIntroducido.collectAsState()

    val botonHabilitado by viewModel.botonGuardarHabilitado.collectAsState()
    val mostrarDialogoExito by viewModel.mostrarDialogoExito.collectAsState()
    val mostrarDialogoError by viewModel.mostrarDialogoError.collectAsState()

    LaunchedEffect(entradaActualDeNavegacion.value) {
        viewModel.cargarDatosUsuario()
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
                text = "Editar Perfil",
                modifier = Modifier.align(Alignment.Center),
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Button(
                onClick = { viewModel.subirCambiosAFirebase() },
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.color4)),
                enabled = botonHabilitado,
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
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Introduce la nueva información en los campos de texto y pulsa en el botón azul para guardar los cambios.",
                modifier = Modifier
                    .padding(26.dp)
            )
        }

        Box(
            modifier = Modifier
                .weight(4f)
                .fillMaxWidth(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                CampoEditar(
                    valor = nicknameIntroducido,
                    nombreCampo = "Nickname",
                    onValorChange = {
                        viewModel.setNicknameIntroducido(it)
                        viewModel.comprobarDiferenciaEntreDatos()
                    }
                )
                CampoEditar(
                    valor = nombreIntroducido,
                    nombreCampo = "Nombre",
                    onValorChange = {
                        viewModel.setNombreIntroducido(it)
                        viewModel.comprobarDiferenciaEntreDatos()
                    }
                )
                CampoEditar(
                    valor = apellidosIntroducido,
                    nombreCampo = "Apellidos",
                    onValorChange = {
                        viewModel.setApellidosIntroducido(it)
                        viewModel.comprobarDiferenciaEntreDatos()
                    }
                )
            }
        }
    }

    if (mostrarDialogoExito) {
        AlertDialog(
            onDismissRequest = { navController.popBackStack() },
            confirmButton = {
                TextButton(onClick = {
                    navController.popBackStack()
                }) {
                    Text("Aceptar", color = Color.White)
                }
            },
            title = { Text("Cambios guardados", color = Color.White) },
            text = { Text("Tu perfil se actualizó correctamente.", color = Color.White) },
            containerColor = colorResource(R.color.color7)
        )
    }

    if (mostrarDialogoError) {
        AlertDialog(
            onDismissRequest = { navController.popBackStack() },
            confirmButton = {
                TextButton(onClick = {
                    navController.popBackStack()
                }) {
                    Text("Aceptar", color = Color.White)
                }
            },
            title = { Text("Error", color = Color.White) },
            text = { Text("Ha ocurrido un error al guardar los cambios. Inténtalo de nuevo.", color = Color.White) },
            containerColor = colorResource(R.color.color7)
        )
    }

}

@Composable
fun CampoEditar(
    valor: String,
    nombreCampo: String,
    onValorChange: (String) -> Unit
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onValorChange,
        label = { Text(nombreCampo) },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp)
    )
}

@Preview(showBackground = true, name = "Editar Perfil Preview")
@Composable
fun PreviewPantallaEditarPerfilScreen() {
    PantallaEditarPerfilScreen(navController = rememberNavController())
}