package navarez.katia.proyectofinal.ui.screens

import androidx.compose.foundation.background
import navarez.katia.proyectofinal.model.EstadoLibro
import navarez.katia.proyectofinal.model.Libro
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgregarLibroScreen(
    onNavigateBack: () -> Unit,
    onNavigateToListaLibros: () -> Unit,
    onNavigateToEstadisticas: () -> Unit,
    onNavigateToPerfil: () -> Unit,
    usuarioId: Int = 0,
    onGuardarLibro: (Libro) -> Unit = {}
) {
    var titulo by remember { mutableStateOf("") }
    var autor by remember { mutableStateOf("") }
    var isbn by remember { mutableStateOf("") }
    var paginas by remember { mutableStateOf("") }

    var categoria by remember { mutableStateOf("Ficción") }
    var categoriaExpandida by remember { mutableStateOf(false) }
    var generoTema by remember { mutableStateOf("") }
    var sinopsis by remember { mutableStateOf("") }

    var estadoSeleccionado by remember { mutableStateOf(EstadoLibro.POR_LEER) }

    var paginaActual by remember { mutableStateOf("") }

    var calificacion by remember { mutableStateOf(0) }
    var fechaInicio by remember { mutableStateOf("") }
    var fechaFin by remember { mutableStateOf("") }
    var resena by remember { mutableStateOf("") }
    var mostrarPickerInicio by remember { mutableStateOf(false) }
    var mostrarPickerFin by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agregar Libro") },
                navigationIcon = {
                    IconButton(onClick = { onNavigateBack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Search, contentDescription = "Buscar")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = false,
                    onClick = { onNavigateToListaLibros() },
                    icon = { Icon(Icons.Default.MenuBook, contentDescription = null) },
                    label = { Text("Mis libros") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { onNavigateToEstadisticas() },
                    icon = { Icon(Icons.Default.BarChart, contentDescription = null) },
                    label = { Text("Estadísticas") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { onNavigateToPerfil() },
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    label = { Text("Perfil") }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                    .clickable { },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.AddPhotoAlternate,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.height(8.dp))
                    Text("Subir portada del libro", style = MaterialTheme.typography.bodyMedium)
                    Text(
                        "JPG, PNG HASTA 5MB",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            TituloSeccion("Información Básica")

            EtiquetaCampo("Título *")
            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                placeholder = { Text("Ej. Cien años de soledad") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            EtiquetaCampo("Autor(es) *")
            OutlinedTextField(
                value = autor,
                onValueChange = { autor = it },
                placeholder = { Text("Ej. Gabriel García Márquez") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Column(modifier = Modifier.weight(1.6f)) {
                    EtiquetaCampo("ISBN (Opcional)")
                    OutlinedTextField(
                        value = isbn,
                        onValueChange = { isbn = it },
                        placeholder = { Text("978-...") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    EtiquetaCampo("Páginas")
                    OutlinedTextField(
                        value = paginas,
                        onValueChange = { paginas = it },
                        placeholder = { Text("0") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            TituloSeccion("Categoría y Género")

            EtiquetaCampo("Categoría")
            Box {
                OutlinedTextField(
                    value = categoria,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Desplegar")
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable { categoriaExpandida = true }
                )
                DropdownMenu(
                    expanded = categoriaExpandida,
                    onDismissRequest = { categoriaExpandida = false }
                ) {
                    listOf("Ficción", "No ficción", "Académico", "Poesía", "Infantil").forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion) },
                            onClick = {
                                categoria = opcion
                                categoriaExpandida = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            EtiquetaCampo("Género / Tema")
            OutlinedTextField(
                value = generoTema,
                onValueChange = { generoTema = it },
                placeholder = { Text("Ej. Realismo mágico") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            EtiquetaCampo("Sinopsis")
            OutlinedTextField(
                value = sinopsis,
                onValueChange = { sinopsis = it },
                placeholder = { Text("Breve descripción del libro...") },
                minLines = 3,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
            )

            Spacer(Modifier.height(20.dp))

            TituloSeccion("Estado de Lectura")

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = estadoSeleccionado == EstadoLibro.POR_LEER,
                    onClick = { estadoSeleccionado = EstadoLibro.POR_LEER },
                    label = { Text("Por leer") }
                )
                FilterChip(
                    selected = estadoSeleccionado == EstadoLibro.EN_CURSO,
                    onClick = { estadoSeleccionado = EstadoLibro.EN_CURSO },
                    label = { Text("En curso") }
                )
                FilterChip(
                    selected = estadoSeleccionado == EstadoLibro.TERMINADO,
                    onClick = { estadoSeleccionado = EstadoLibro.TERMINADO },
                    label = { Text("Terminado") }
                )
            }

            when (estadoSeleccionado) {
                EstadoLibro.POR_LEER -> { }

                EstadoLibro.EN_CURSO -> {
                    Spacer(Modifier.height(16.dp))
                    EtiquetaCampo("Página actual")
                    OutlinedTextField(
                        value = paginaActual,
                        onValueChange = { paginaActual = it },
                        placeholder = { Text("0") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                EstadoLibro.TERMINADO -> {
                    Spacer(Modifier.height(16.dp))
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Tu Reseña", style = MaterialTheme.typography.titleMedium)

                            Spacer(Modifier.height(12.dp))

                            Text("Calificación", style = MaterialTheme.typography.labelMedium)
                            Spacer(Modifier.height(4.dp))
                            Row {
                                (1..5).forEach { i ->
                                    Icon(
                                        imageVector = if (i <= calificacion) Icons.Default.Star else Icons.Default.StarBorder,
                                        contentDescription = "Estrella $i",
                                        tint = if (i <= calificacion)
                                            MaterialTheme.colorScheme.primary
                                        else
                                            MaterialTheme.colorScheme.outline,
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clickable { calificacion = i }
                                    )
                                }
                            }

                            Spacer(Modifier.height(12.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Column(modifier = Modifier.weight(1f)) {
                                    EtiquetaCampo("Fecha Inicio")
                                    OutlinedTextField(
                                        value = fechaInicio,
                                        onValueChange = {},
                                        readOnly = true,
                                        placeholder = { Text("mm/dd/yyyy") },
                                        trailingIcon = {
                                            Icon(
                                                Icons.Default.CalendarToday,
                                                contentDescription = "Elegir fecha",
                                                modifier = Modifier.clickable { mostrarPickerInicio = true }
                                            )
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { mostrarPickerInicio = true }
                                    )
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    EtiquetaCampo("Fecha Fin")
                                    OutlinedTextField(
                                        value = fechaFin,
                                        onValueChange = {},
                                        readOnly = true,
                                        placeholder = { Text("mm/dd/yyyy") },
                                        trailingIcon = {
                                            Icon(
                                                Icons.Default.CalendarToday,
                                                contentDescription = "Elegir fecha",
                                                modifier = Modifier.clickable { mostrarPickerFin = true }
                                            )
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { mostrarPickerFin = true }
                                    )
                                }
                            }

                            Spacer(Modifier.height(12.dp))

                            EtiquetaCampo("Reseña")
                            OutlinedTextField(
                                value = resena,
                                onValueChange = { resena = it },
                                placeholder = { Text("Escribe tus pensamientos sobre esta lectura...") },
                                minLines = 3,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(110.dp)
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            val esTerminado = estadoSeleccionado == EstadoLibro.TERMINADO
            val esEnCurso = estadoSeleccionado == EstadoLibro.EN_CURSO

            Button(
                onClick = {
                    val nuevoLibro = Libro(
                        usuarioId = usuarioId,
                        titulo = titulo.trim(),
                        autor = autor.trim(),
                        categoria = categoria,
                        generoOTema = generoTema.trim(),
                        numPaginas = paginas.toIntOrNull() ?: 0,
                        sinopsis = sinopsis.trim(),
                        estado = estadoSeleccionado,
                        portada = null,
                        paginaActual = if (esEnCurso) paginaActual.toIntOrNull() ?: 0 else 0,
                        rating = if (esTerminado) calificacion.toFloat() else 0f,
                        resena = if (esTerminado) resena.trim() else "",
                        fechaInicio = if (esTerminado) fechaInicio.ifBlank { null } else null,
                        fechaFin = if (esTerminado) fechaFin.ifBlank { null } else null
                    )
                    onGuardarLibro(nuevoLibro)
                },
                enabled = titulo.isNotBlank() && autor.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar libro")
            }

            Spacer(Modifier.height(8.dp))
        }
    }

    if (mostrarPickerInicio) {
        val estado = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { mostrarPickerInicio = false },
            confirmButton = {
                TextButton(onClick = {
                    estado.selectedDateMillis?.let { millis ->
                        val formato = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
                        fechaInicio = formato.format(java.util.Date(millis))
                    }
                    mostrarPickerInicio = false
                }) { Text("Aceptar") }
            },
            dismissButton = {
                TextButton(onClick = { mostrarPickerInicio = false }) { Text("Cancelar") }
            }
        ) { DatePicker(state = estado) }
    }

    if (mostrarPickerFin) {
        val estado = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { mostrarPickerFin = false },
            confirmButton = {
                TextButton(onClick = {
                    estado.selectedDateMillis?.let { millis ->
                        val formato = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
                        fechaFin = formato.format(java.util.Date(millis))
                    }
                    mostrarPickerFin = false
                }) { Text("Aceptar") }
            },
            dismissButton = {
                TextButton(onClick = { mostrarPickerFin = false }) { Text("Cancelar") }
            }
        ) { DatePicker(state = estado) }
    }
}

@Composable
private fun TituloSeccion(texto: String) {
    Text(
        text = texto,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold
    )
    Spacer(Modifier.height(8.dp))
}

@Composable
private fun EtiquetaCampo(texto: String) {
    Text(text = texto, style = MaterialTheme.typography.labelMedium)
    Spacer(Modifier.height(4.dp))
}

@Preview(showBackground = true, heightDp = 2200)
@Composable
fun AgregarLibroScreenPreview() {
    MaterialTheme {
        AgregarLibroScreen(
            onNavigateBack = {},
            onNavigateToListaLibros = {},
            onNavigateToEstadisticas = {},
            onNavigateToPerfil = {}
        )
    }
}
