package navarez.katia.proyectofinal.ui.screens

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import java.text.SimpleDateFormat
import java.util.Locale
import navarez.katia.proyectofinal.viewmodel.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    usuarioId: Int,
    viewModel: UsuarioViewModel,
    onNavigateToListaLibros: () -> Unit,
    onNavigateToEstadisticas: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    val usuarioActual by viewModel.usuarioActual.collectAsState()

    LaunchedEffect(usuarioId) {
        viewModel.cargarUsuario(usuarioId)
    }

    var nombre by remember { mutableStateOf("") }
    var fechaNacimiento by remember { mutableStateOf("") }
    var genero by remember { mutableStateOf("") }
    var profesion by remember { mutableStateOf("") }
    var mostrarDatePicker by remember { mutableStateOf(false) }
    var profileImageUri by remember { mutableStateOf<android.net.Uri?>(null) }

    var mostrarDialogoPassword by remember { mutableStateOf(false) }
    var passwordActual by remember { mutableStateOf("") }
    var passwordNueva by remember { mutableStateOf("") }
    var passwordConfirmar by remember { mutableStateOf("") }
    var errorPassword by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current

    LaunchedEffect(usuarioActual) {
        usuarioActual?.let {
            nombre = it.nombre
            fechaNacimiento = it.fechaNacimiento ?: ""
            genero = it.genero ?: ""
            profesion = it.profesion ?: ""
            it.fotoPerfil?.let { fotoStr ->
                profileImageUri = android.net.Uri.parse(fotoStr)
            }
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: android.net.Uri? ->
        profileImageUri = uri
    }

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
                    selected = false,
                    onClick = { onNavigateToEstadisticas() },
                    icon = { Icon(Icons.Default.BarChart, contentDescription = null) },
                    label = { Text("Estadísticas") }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = {},
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clickable { launcher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (profileImageUri != null) {
                    AsyncImage(
                        model = profileImageUri,
                        contentDescription = "Foto de perfil",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(Color.Gray)
                    )
                }
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .align(Alignment.BottomEnd)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar foto de perfil",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = nombre.ifEmpty { "Usuario" },
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Lector entusiasta",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(32.dp))

                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Nombre completo", style = MaterialTheme.typography.labelMedium)
                    Spacer(Modifier.height(6.dp))
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        placeholder = { Text("nombre completo") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Correo electrónico", style = MaterialTheme.typography.labelMedium)
                    Spacer(Modifier.height(6.dp))
                    OutlinedTextField(
                        value = usuarioActual?.correo ?: "",
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                        enabled = false,
                        onValueChange = {},
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Fecha de nacimiento", style = MaterialTheme.typography.labelMedium)
                    Spacer(Modifier.height(6.dp))
                    OutlinedTextField(
                        value = fechaNacimiento,
                        onValueChange = {},
                        leadingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = null) },
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { mostrarDatePicker = true }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Género", style = MaterialTheme.typography.labelMedium)
                    Spacer(Modifier.height(6.dp))
                    OutlinedTextField(
                        value = genero,
                        onValueChange = { genero = it },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Profesión", style = MaterialTheme.typography.labelMedium)
                    Spacer(Modifier.height(6.dp))
                    OutlinedTextField(
                        value = profesion,
                        onValueChange = { profesion = it },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        usuarioActual?.let { usuario ->
                            viewModel.actualizarPerfil(
                                usuario.copy(
                                    nombre = nombre,
                                    fechaNacimiento = fechaNacimiento.ifEmpty { null },
                                    genero = genero.ifEmpty { null },
                                    profesion = profesion.ifEmpty { null },
                                    fotoPerfil = profileImageUri?.toString()
                                )
                            ) {}
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Guardar cambios", modifier = Modifier.padding(vertical = 4.dp))
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = { mostrarDialogoPassword = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Cambiar contraseña", modifier = Modifier.padding(vertical = 4.dp))
                }

                Spacer(modifier = Modifier.height(40.dp))

                Row(
                    modifier = Modifier
                        .clickable { onCerrarSesion() }
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = "Cerrar sesión",
                        tint = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Cerrar sesión",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            if (mostrarDatePicker) {
                val datePickerState = rememberDatePickerState()
                DatePickerDialog(
                    onDismissRequest = { mostrarDatePicker = false },
                    confirmButton = {
                        TextButton(onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                                fechaNacimiento = formato.format(java.util.Date(millis))
                            }
                            mostrarDatePicker = false
                        }) { Text("Aceptar") }
                    },
                    dismissButton = {
                        TextButton(onClick = { mostrarDatePicker = false }) { Text("Cancelar") }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }

            if (mostrarDialogoPassword) {
                AlertDialog(
                    onDismissRequest = { mostrarDialogoPassword = false },
                    title = { Text("Cambiar contraseña") },
                    text = {
                        Column {
                            OutlinedTextField(
                                value = passwordActual,
                                onValueChange = { passwordActual = it; errorPassword = null },
                                label = { Text("Contraseña actual") },
                                visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(),
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(8.dp))
                            OutlinedTextField(
                                value = passwordNueva,
                                onValueChange = { passwordNueva = it; errorPassword = null },
                                label = { Text("Nueva contraseña") },
                                visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(),
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(8.dp))
                            OutlinedTextField(
                                value = passwordConfirmar,
                                onValueChange = { passwordConfirmar = it; errorPassword = null },
                                label = { Text("Confirmar nueva contraseña") },
                                visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(),
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                            if (errorPassword != null) {
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    errorPassword!!,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            if (passwordNueva != passwordConfirmar) {
                                errorPassword = "Las contraseñas no coinciden"
                            } else {
                                viewModel.cambiarPassword(passwordActual, passwordNueva) { resultado ->
                                    resultado
                                        .onSuccess {
                                            Toast.makeText(context, "Contraseña actualizada", Toast.LENGTH_SHORT).show()
                                            mostrarDialogoPassword = false
                                            passwordActual = ""
                                            passwordNueva = ""
                                            passwordConfirmar = ""
                                        }
                                        .onFailure { errorPassword = it.message }
                                }
                            }
                        }) { Text("Guardar") }
                    },
                    dismissButton = {
                        TextButton(onClick = { mostrarDialogoPassword = false }) { Text("Cancelar") }
                    }
                )
            }
        }
    }
}
