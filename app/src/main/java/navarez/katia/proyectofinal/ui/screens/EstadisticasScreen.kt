package navarez.katia.proyectofinal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import navarez.katia.proyectofinal.model.EstadoLibro
import navarez.katia.proyectofinal.model.Libro
import navarez.katia.proyectofinal.viewmodel.LibroViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private val MESES_ABREV = listOf("ENE", "FEB", "MAR", "ABR", "MAY", "JUN", "JUL", "AGO", "SEP", "OCT", "NOV", "DIC")
private val MESES_COMPL = listOf(
    "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
    "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
)

private fun parsearMesAno(fecha: String?): Pair<Int, Int>? {
    if (fecha.isNullOrBlank()) return null
    return try {
        val formato = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        formato.isLenient = false
        val date = formato.parse(fecha) ?: return null
        val cal = Calendar.getInstance()
        cal.time = date
        (cal.get(Calendar.MONTH) + 1) to cal.get(Calendar.YEAR)
    } catch (e: Exception) {
        null
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstadisticasScreen(
    usuarioId: Int,
    viewModel: LibroViewModel,
    onNavigateToListaLibros: () -> Unit,
    onNavigateToPerfil: () -> Unit
) {
    val libros by viewModel.libros.collectAsState()

    LaunchedEffect(usuarioId) {
        viewModel.cargarLibros(usuarioId)
    }

    val totalLibros = libros.size
    val enCurso = libros.count { it.estado == EstadoLibro.EN_CURSO }
    val terminados = libros.count { it.estado == EstadoLibro.TERMINADO }
    val paginasLeidas = libros.sumOf {
        when (it.estado) {
            EstadoLibro.TERMINADO -> it.numPaginas
            EstadoLibro.EN_CURSO -> it.paginaActual
            else -> 0
        }
    }

    data class LibroConFecha(val libro: Libro, val mes: Int, val ano: Int)

    val librosConFecha = remember(libros) {
        libros.filter { it.estado == EstadoLibro.TERMINADO }
            .mapNotNull { libro ->
                parsearMesAno(libro.fechaFin)?.let { (mes, ano) -> LibroConFecha(libro, mes, ano) }
            }
    }

    val anosDisponibles = remember(librosConFecha) {
        librosConFecha.map { it.ano }.distinct().sortedDescending()
    }

    var anoSeleccionado by remember { mutableStateOf<Int?>(null) }
    LaunchedEffect(anosDisponibles) {
        if (anoSeleccionado == null || anoSeleccionado !in anosDisponibles) {
            anoSeleccionado = anosDisponibles.firstOrNull() ?: Calendar.getInstance().get(Calendar.YEAR)
        }
    }
    val anoActivo = anoSeleccionado ?: Calendar.getInstance().get(Calendar.YEAR)

    var mesSeleccionado by remember { mutableStateOf<Int?>(null) } // null = Todo el año
    var anoExpandido by remember { mutableStateOf(false) }
    var mesExpandido by remember { mutableStateOf(false) }

    val librosDelAnio = librosConFecha.filter { it.ano == anoActivo }
    val librosLeidosFiltrados = librosDelAnio.filter { mesSeleccionado == null || it.mes == mesSeleccionado }

    val librosPorMes = (1..12).map { mes -> MESES_ABREV[mes - 1] to librosDelAnio.count { it.mes == mes } }
    val paginasPorMes = (1..12).map { mes ->
        MESES_ABREV[mes - 1] to librosDelAnio.filter { it.mes == mes }.sumOf { it.libro.numPaginas }
    }

    val librosPorAnio = librosConFecha.groupBy { it.ano }
        .map { it.key to it.value.size }
        .sortedBy { it.first }
    val paginasPorAno = librosConFecha.groupBy { it.ano }
        .map { it.key to it.value.sumOf { c -> c.libro.numPaginas } }
        .sortedBy { it.first }

    val categorias = librosLeidosFiltrados.map { it.libro }.groupBy { it.categoria }
        .map { it.key to it.value.size }
        .sortedByDescending { it.second }

    val generos = librosLeidosFiltrados.map { it.libro }.groupBy { it.generoOTema }
        .map { it.key to it.value.size }
        .sortedByDescending { it.second }
        .take(5)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.MenuBook, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Diario de Lectura", style = MaterialTheme.typography.titleMedium)
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
                    selected = false,
                    onClick = { onNavigateToListaLibros() },
                    icon = { Icon(Icons.Default.MenuBook, contentDescription = null) },
                    label = { Text("Mis libros") }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = {},
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
            Text(
                "Resumen de Lectura",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Tu biblioteca personal",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(modifier = Modifier.weight(1f)) {
                    SelectorFiltro(
                        texto = anoActivo.toString(),
                        onClick = { anoExpandido = true }
                    )
                    DropdownMenu(expanded = anoExpandido, onDismissRequest = { anoExpandido = false }) {
                        val opciones = if (anosDisponibles.isEmpty()) listOf(anoActivo) else anosDisponibles
                        opciones.forEach { ano ->
                            DropdownMenuItem(
                                text = { Text(ano.toString()) },
                                onClick = { anoSeleccionado = ano; anoExpandido = false }
                            )
                        }
                    }
                }
                Box(modifier = Modifier.weight(1f)) {
                    SelectorFiltro(
                        texto = mesSeleccionado?.let { MESES_COMPL[it - 1] } ?: "Todo el año",
                        onClick = { mesExpandido = true }
                    )
                    DropdownMenu(expanded = mesExpandido, onDismissRequest = { mesExpandido = false }) {
                        DropdownMenuItem(
                            text = { Text("Todo el año") },
                            onClick = { mesSeleccionado = null; mesExpandido = false }
                        )
                        MESES_COMPL.forEachIndexed { index, nombre ->
                            DropdownMenuItem(
                                text = { Text(nombre) },
                                onClick = { mesSeleccionado = index + 1; mesExpandido = false }
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Text(
                "Resumen general (todos los años)",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    numero = totalLibros.toString(),
                    etiqueta = "Total",
                    color = MaterialTheme.colorScheme.primary
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    numero = terminados.toString(),
                    etiqueta = "Terminados",
                    color = Color(0xFF4CAF50)
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    numero = enCurso.toString(),
                    etiqueta = "En curso",
                    color = Color(0xFF2196F3)
                )
            }

            Spacer(Modifier.height(12.dp))

            Card(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Book,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(
                            "Páginas leídas",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            "$paginasLeidas páginas",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            GraficaBarras(
                titulo = "Libros por mes",
                datos = librosPorMes,
                indiceResaltado = mesSeleccionado?.minus(1)
            )

            Spacer(Modifier.height(16.dp))

            GraficaBarras(
                titulo = "Libros por año",
                datos = librosPorAnio.map { it.first.toString() to it.second },
                indiceResaltado = librosPorAnio.indexOfFirst { it.first == anoActivo }.takeIf { it >= 0 }
            )

            Spacer(Modifier.height(16.dp))

            GraficaBarras(
                titulo = "Páginas por mes",
                datos = paginasPorMes,
                indiceResaltado = mesSeleccionado?.minus(1)
            )

            Spacer(Modifier.height(16.dp))

            GraficaBarras(
                titulo = "Páginas por año",
                datos = paginasPorAno.map { it.first.toString() to it.second },
                indiceResaltado = paginasPorAno.indexOfFirst { it.first == anoActivo }.takeIf { it >= 0 }
            )

            Spacer(Modifier.height(20.dp))

            Text(
                "Categorías y géneros — " + (mesSeleccionado?.let { "${MESES_COMPL[it - 1]} $anoActivo" } ?: "todo $anoActivo"),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )

            if (categorias.isNotEmpty()) {
                Spacer(Modifier.height(16.dp))

                Text(
                    "Categorías",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(Modifier.height(12.dp))

                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        val totalCategorias = categorias.sumOf { it.second }
                        categorias.forEach { (categoria, cantidad) ->
                            val progreso = if (totalCategorias > 0) cantidad.toFloat() / totalCategorias else 0f
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(categoria, style = MaterialTheme.typography.bodyMedium)
                                Text(
                                    cantidad.toString(),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(Modifier.height(4.dp))
                            LinearProgressIndicator(
                                progress = { progreso },
                                modifier = Modifier.fillMaxWidth(),
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(Modifier.height(12.dp))
                        }
                    }
                }
            }

            if (generos.isNotEmpty()) {
                Spacer(Modifier.height(20.dp))

                Text(
                    "Géneros literarios",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(Modifier.height(12.dp))

                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        val totalGeneros = generos.sumOf { it.second }
                        generos.forEach { (genero, cantidad) ->
                            val progreso = if (totalGeneros > 0) cantidad.toFloat() / totalGeneros else 0f
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(genero, style = MaterialTheme.typography.bodyMedium)
                                Text(
                                    cantidad.toString(),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(Modifier.height(4.dp))
                            LinearProgressIndicator(
                                progress = { progreso },
                                modifier = Modifier.fillMaxWidth(),
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(Modifier.height(12.dp))
                        }
                    }
                }
            }

            if (categorias.isEmpty() && generos.isEmpty() && librosLeidosFiltrados.isEmpty()) {
                Spacer(Modifier.height(12.dp))
                Text(
                    "Sin libros terminados en este período",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (totalLibros == 0) {
                Spacer(Modifier.height(48.dp))
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.BarChart,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.height(12.dp))
                        Text(
                            "Aún no tienes libros",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            "Agrega libros para ver tus estadísticas",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun SelectorFiltro(texto: String, onClick: () -> Unit) {
    OutlinedButton(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Text(texto, style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.width(6.dp))
        Icon(
            Icons.Default.KeyboardArrowDown,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
fun GraficaBarras(
    titulo: String,
    datos: List<Pair<String, Int>>,
    indiceResaltado: Int? = null
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(titulo, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(16.dp))

            if (datos.isEmpty() || datos.all { it.second == 0 }) {
                Text(
                    "Sin datos en este período",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                val maxValor = (datos.maxOfOrNull { it.second } ?: 0).coerceAtLeast(1)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    datos.forEachIndexed { index, (etiqueta, valor) ->
                        val alturaDp = (valor.toFloat() / maxValor.toFloat() * 90f).coerceAtLeast(3f)
                        val resaltada = indiceResaltado == index
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.width(34.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .height(90.dp),
                                contentAlignment = Alignment.BottomCenter
                            ) {
                                Box(
                                    modifier = Modifier
                                        .height(alturaDp.dp)
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                        .background(
                                            if (resaltada) MaterialTheme.colorScheme.primary
                                            else MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)
                                        )
                                )
                            }
                            Spacer(Modifier.height(4.dp))
                            Text(
                                etiqueta,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    numero: String,
    etiqueta: String,
    color: Color
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                numero,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                etiqueta,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
