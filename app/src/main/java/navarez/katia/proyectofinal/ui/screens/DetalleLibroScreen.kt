package navarez.katia.proyectofinal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import navarez.katia.proyectofinal.model.EstadoLibro
import navarez.katia.proyectofinal.viewmodel.LibroViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleLibroScreen(
    libroId: Int,
    viewModel: LibroViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToListaLibros: () -> Unit,
    onNavigateToEstadisticas: () -> Unit,
    onNavigateToPerfil: () -> Unit
) {
    val libroDetalle by viewModel.libroDetalle.collectAsState()

    LaunchedEffect(libroId) {
        viewModel.cargarLibro(libroId)
    }

    var paginaActual by remember { mutableStateOf("") }
    var calificacion by remember { mutableStateOf(0) }
    var resena by remember { mutableStateOf("") }
    var fechaInicio by remember { mutableStateOf("") }
    var fechaFin by remember { mutableStateOf("") }
    var mostrarPickerInicio by remember { mutableStateOf(false) }
    var mostrarPickerFin by remember { mutableStateOf(false) }
    var estadoActual by remember { mutableStateOf(EstadoLibro.POR_LEER) }

    LaunchedEffect(libroDetalle) {
        libroDetalle?.let {
            paginaActual = it.paginaActual.toString()
            calificacion = it.rating.toInt()
            resena = it.resena
            fechaInicio = it.fechaInicio ?: ""
            fechaFin = it.fechaFin ?: ""
            estadoActual = it.estado
        }
    }

    if (libroDetalle == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val libro = libroDetalle!!
    val puedeResenar = estadoActual != EstadoLibro.POR_LEER

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Diario de Lectura", style = MaterialTheme.typography.titleMedium) },
                navigationIcon = {
                    IconButton(onClick = { onNavigateBack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    titleContentColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = MaterialTheme.colorScheme.primary,
                    actionIconContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = true,
                    onClick = { onNavigateToListaLibros() },
                    icon = { Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = null) },
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
                    .height(220.dp),
                contentAlignment = Alignment.Center
            ) {
                if (libro.portada != null) {
                    AsyncImage(
                        model = libro.portada,
                        contentDescription = "Portada de ${libro.titulo}",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .height(200.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .height(200.dp)
                            .aspectRatio(0.7f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Book,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            val (textoBadge, colorBadge) = when (estadoActual) {
                EstadoLibro.EN_CURSO -> "En curso" to Color(0xFFB3E5FC)
                EstadoLibro.TERMINADO -> "Terminado" to Color(0xFFC8E6C9)
                EstadoLibro.POR_LEER -> "Por leer" to Color(0xFFE0E0E0)
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(colorBadge)
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(textoBadge, style = MaterialTheme.typography.labelMedium)
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = libro.titulo,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = libro.autor,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Category,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        libro.categoria,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.AutoMirrored.Filled.MenuBook,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        "${libro.numPaginas} páginas",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            if (estadoActual == EstadoLibro.EN_CURSO) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        val porcentaje = paginaActual.toIntOrNull()?.toFloat()?.div(libro.numPaginas) ?: 0f
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Progreso de lectura",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                "${(porcentaje * 100).toInt()}%",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { porcentaje },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(12.dp))
                        Text("Página actual", style = MaterialTheme.typography.labelMedium)
                        Spacer(Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            OutlinedTextField(
                                value = paginaActual,
                                onValueChange = { paginaActual = it },
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text("de ${libro.numPaginas}", style = MaterialTheme.typography.bodyMedium)
                        }
                        Spacer(Modifier.height(12.dp))
                        Button(
                            onClick = {
                                viewModel.actualizarLibro(libro.copy(estado = EstadoLibro.TERMINADO))
                                estadoActual = EstadoLibro.TERMINADO
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("Marcar como completado")
                        }
                    }
                }
                Spacer(Modifier.height(12.dp))
            }

            if (estadoActual == EstadoLibro.POR_LEER) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Bookmark,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                "Estado: Por leer",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        OutlinedButton(onClick = {
                            viewModel.actualizarLibro(libro.copy(estado = EstadoLibro.EN_CURSO))
                            estadoActual = EstadoLibro.EN_CURSO
                        }) { Text("Iniciar libro") }
                    }
                }
                Spacer(Modifier.height(12.dp))
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Tu Reseña",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    if (!puedeResenar) {
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Disponible cuando empieces a leer este libro",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(Modifier.height(12.dp))
                    Text("Calificación", style = MaterialTheme.typography.labelMedium)
                    Spacer(Modifier.height(4.dp))
                    Row {
                        (1..5).forEach { i ->
                            Icon(
                                imageVector = if (i <= calificacion) Icons.Default.Star else Icons.Default.StarBorder,
                                contentDescription = "Estrella $i",
                                tint = if (!puedeResenar) MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                                else if (i <= calificacion) Color(0xFFFFC107)
                                else MaterialTheme.colorScheme.outline,
                                modifier = Modifier
                                    .size(32.dp)
                                    .clickable(enabled = puedeResenar) { calificacion = i }
                            )
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    Text("¿Qué te pareció?", style = MaterialTheme.typography.labelMedium)
                    Spacer(Modifier.height(4.dp))
                    OutlinedTextField(
                        value = resena,
                        onValueChange = { resena = it },
                        enabled = puedeResenar,
                        placeholder = { Text("Escribe tus pensamientos sobre esta lectura...") },
                        minLines = 3,
                        modifier = Modifier.fillMaxWidth().height(120.dp)
                    )
                    Spacer(Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Fecha Inicio", style = MaterialTheme.typography.labelMedium)
                            Spacer(Modifier.height(4.dp))
                            OutlinedTextField(
                                value = fechaInicio,
                                onValueChange = {},
                                readOnly = true,
                                enabled = puedeResenar,
                                placeholder = { Text("mm/dd/yyyy") },
                                trailingIcon = {
                                    Icon(Icons.Default.CalendarToday, contentDescription = null,
                                        modifier = Modifier.clickable(enabled = puedeResenar) { mostrarPickerInicio = true })
                                },
                                modifier = Modifier.fillMaxWidth().clickable(enabled = puedeResenar) { mostrarPickerInicio = true }
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Fecha Fin", style = MaterialTheme.typography.labelMedium)
                            Spacer(Modifier.height(4.dp))
                            OutlinedTextField(
                                value = fechaFin,
                                onValueChange = {},
                                readOnly = true,
                                enabled = puedeResenar,
                                placeholder = { Text("mm/dd/yyyy") },
                                trailingIcon = {
                                    Icon(Icons.Default.CalendarToday, contentDescription = null,
                                        modifier = Modifier.clickable(enabled = puedeResenar) { mostrarPickerFin = true })
                                },
                                modifier = Modifier.fillMaxWidth().clickable(enabled = puedeResenar) { mostrarPickerFin = true }
                            )
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                    TextButton(
                        onClick = {
                            viewModel.actualizarLibro(
                                libro.copy(
                                    paginaActual = paginaActual.toIntOrNull() ?: libro.paginaActual,
                                    rating = calificacion.toFloat(),
                                    resena = resena,
                                    fechaInicio = fechaInicio.ifEmpty { null },
                                    fechaFin = fechaFin.ifEmpty { null },
                                    estado = estadoActual
                                )
                            )
                        },
                        enabled = puedeResenar,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("Guardar Reseña", fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }

    if (mostrarPickerInicio) {
        val estado = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { mostrarPickerInicio = false },
            confirmButton = {
                TextButton(onClick = {
                    estado.selectedDateMillis?.let { millis ->
                        val formato = java.text.SimpleDateFormat("MM/dd/yyyy", java.util.Locale.getDefault())
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
                        val formato = java.text.SimpleDateFormat("MM/dd/yyyy", java.util.Locale.getDefault())
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
