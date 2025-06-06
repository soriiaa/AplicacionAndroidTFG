package com.example.investlearntfg.ui.screens.pantallaNoticias

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.investlearntfg.data.model.Noticia
import com.example.investlearntfg.ui.components.BottomNavigationBarPredeterminado
import com.example.investlearntfg.ui.components.LogoAplicacionPulsable
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PantallaNoticiasScreen(
    navController: NavController,
    viewModel: PantallaNoticiasViewModel = hiltViewModel()
) {
    val focusManager = LocalFocusManager.current

    val listaNoticias by viewModel.noticias.collectAsState()
    val estaCargando by viewModel.estaCargando.collectAsState()

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
                    .align(Alignment.Center),
                contentAlignment = Alignment.Center
            ) {
                LogoAplicacionPulsable {
                    viewModel.cargarNoticias()
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(6f),
            contentAlignment = Alignment.Center
        ) {
            if (estaCargando) {
                CircularProgressIndicator(color = Color.White)
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(listaNoticias) { noticia ->
                        NoticiaCard(noticia = noticia)
                    }
                }
            }
        }
        BottomNavigationBarPredeterminado(navController)
    }
}

@Composable
fun NoticiaCard(noticia: Noticia, modifier: Modifier = Modifier) {

    val uriHandler = LocalUriHandler.current

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 6.dp, bottom = 6.dp, start = 16.dp, end = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .clickable { uriHandler.openUri(noticia.url) }
                .padding(16.dp)
        ) {
            if (!noticia.image.isNullOrEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(noticia.image),
                    contentDescription = "Imagen noticia",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            Text(
                text = noticia.headline,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = noticia.summary,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val formattedDate = rememberFormattedDate(noticia.datetime)
                Text(
                    text = "${noticia.source} · $formattedDate",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )

                TextButton(onClick = { uriHandler.openUri(noticia.url) }) {
                    Text(text = "Leer más")
                }
            }
        }
    }
}

@Composable
fun rememberFormattedDate(timestamp: Long): String {
    val sdf = remember {
        SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    }
    val date = Date(timestamp * 1000)
    return sdf.format(date)
}
