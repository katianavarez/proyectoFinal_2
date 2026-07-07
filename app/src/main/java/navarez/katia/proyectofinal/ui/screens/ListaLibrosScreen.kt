package navarez.katia.proyectofinal.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import navarez.katia.proyectofinal.R
import navarez.katia.proyectofinal.data.SampleData
import navarez.katia.proyectofinal.model.EstadoLibro
import navarez.katia.proyectofinal.model.Libro

@Composable
fun ListaLibrosScreen(usuarioId: Int, onNavigateToDetalle: (Int) -> Unit, onNavigateToAgregar: () -> Unit) {
    var filtroSeleccionado by remember { mutableStateOf("Todos") }
    var ordenarPorRating by remember { mutableStateOf(false) }
    val filtros = listOf("Todos", "Por leer", "En curso", "Terminados")

    val librosFiltrados = when (filtroSeleccionado) {
        "Por leer" -> SampleData.libros.filter { it.estado == EstadoLibro.POR_LEER }
        "En curso" -> SampleData.libros.filter { it.estado == EstadoLibro.EN_CURSO }
        "Terminados" -> SampleData.libros.filter { it.estado == EstadoLibro.TERMINADO }
        else -> SampleData.libros
    }

    val librosOrdenados = if (ordenarPorRating) {
        librosFiltrados.sortedByDescending { it.rating }
    } else {
        librosFiltrados
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.MenuBook, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Diario de Lectura", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.weight(1f))

                val iconoEstrella = if (ordenarPorRating) Icons.Default.Star else Icons.Default.StarBorder
                Icon(
                    imageVector = iconoEstrella,
                    contentDescription = "Ordenar por rating",
                    tint = if (ordenarPorRating) Color(0xFFFFC107) else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.clickable { ordenarPorRating = !ordenarPorRating }
                )

                Spacer(modifier = Modifier.width(16.dp))
                Icon(imageVector = Icons.Default.Search, contentDescription = "Buscar")
            }

            HorizontalDivider()

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {

                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Mis libros", style = MaterialTheme.typography.headlineSmall)
                Text(text = "TU BIBLIOTECA PERSONAL", style = MaterialTheme.typography.labelMedium)

                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                    filtros.forEach { filtro ->
                        val seleccionado = filtro == filtroSeleccionado
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(
                                    if (seleccionado) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.surfaceVariant
                                )
                                .clickable { filtroSeleccionado = filtro }
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = filtro,
                                color = if (seleccionado) MaterialTheme.colorScheme.onPrimary
                                else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn {
                    items(librosOrdenados) { libro ->
                        LibroCard(
                            libro = libro,
                            onClick = { onNavigateToDetalle(libro.id) }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }

        FloatingActionButton(
            onClick = { onNavigateToAgregar() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Agregar libro")
        }
    }
}

@Composable
fun LibroCard(libro: Libro, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            Image(
                painter = painterResource(id = R.drawable.el_alquimista),
                contentDescription = "Portada de ${libro.titulo}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(width = 64.dp, height = 90.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                var textoBadge = ""
                var colorBadge = Color(0xFFE0E0E0)
                if (libro.estado == EstadoLibro.POR_LEER) {
                    textoBadge = "Por leer"
                    colorBadge = Color(0xFFE0E0E0)
                } else if (libro.estado == EstadoLibro.EN_CURSO) {
                    textoBadge = "En curso"
                    colorBadge = Color(0xFFB3E5FC)
                } else {
                    textoBadge = "Terminado"
                    colorBadge = Color(0xFFC8E6C9)
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(colorBadge)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(text = textoBadge, style = MaterialTheme.typography.labelSmall)
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(text = libro.titulo, style = MaterialTheme.typography.titleMedium)
                Text(text = libro.autor, style = MaterialTheme.typography.bodyMedium)

                Spacer(modifier = Modifier.height(8.dp))

                when (libro.estado) {
                    EstadoLibro.EN_CURSO -> {
                        val porcentaje = (libro.paginaActual.toFloat() / libro.numPaginas.toFloat())
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Progreso", style = MaterialTheme.typography.labelSmall)
                            Text(
                                text = "${(porcentaje * 100).toInt()}%",
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        LinearProgressIndicator(
                            progress = porcentaje,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    EstadoLibro.TERMINADO -> {
                        Row {
                            repeat(libro.rating.toInt()) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Color(0xFFFFC107)
                                )
                            }
                        }
                    }
                    EstadoLibro.POR_LEER -> {
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListaLibrosScreenPreview() {
    MaterialTheme {
        ListaLibrosScreen(
            usuarioId = 0,
            onNavigateToDetalle = {},
            onNavigateToAgregar = {}
        )
    }
}