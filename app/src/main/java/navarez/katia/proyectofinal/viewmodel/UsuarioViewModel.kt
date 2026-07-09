package navarez.katia.proyectofinal.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import navarez.katia.proyectofinal.data.database.AppDatabase
import navarez.katia.proyectofinal.data.repository.FirebaseAuthRepository
import navarez.katia.proyectofinal.data.repository.UsuarioRepository
import navarez.katia.proyectofinal.model.Usuario

class UsuarioViewModel(
    private val repository: UsuarioRepository,
    private val authRepository: FirebaseAuthRepository = FirebaseAuthRepository()
) : ViewModel() {

    private val _usuarioActual = MutableStateFlow<Usuario?>(null)
    val usuarioActual: StateFlow<Usuario?> = _usuarioActual

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando

    fun login(correo: String, password: String, onSuccess: (Int) -> Unit) {
        viewModelScope.launch {
            _cargando.value = true
            authRepository.login(correo.trim(), password)
                .onSuccess { fbUser ->
                    val perfil = repository.obtenerOCrearPerfil(
                        uid = fbUser.uid,
                        correo = fbUser.email ?: correo.trim(),
                        nombre = fbUser.displayName ?: correo.substringBefore("@")
                    )
                    _usuarioActual.value = perfil
                    onSuccess(perfil.id)
                }
                .onFailure { _error.value = traducirError(it) }
            _cargando.value = false
        }
    }

    fun registrar(
        nombre: String,
        correo: String,
        password: String,
        confirmar: String,
        fechaNacimiento: String,
        genero: String,
        profesion: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            if (password != confirmar) {
                _error.value = "Las contraseñas no coinciden"
                return@launch
            }
            _cargando.value = true
            authRepository.registrar(correo.trim(), password)
                .onSuccess { fbUser ->
                    val perfil = repository.obtenerOCrearPerfil(
                        uid = fbUser.uid,
                        correo = fbUser.email ?: correo.trim(),
                        nombre = nombre
                    )
                    val completo = perfil.copy(
                        nombre = nombre,
                        fechaNacimiento = fechaNacimiento.ifEmpty { null },
                        genero = genero.ifEmpty { null },
                        profesion = profesion.ifEmpty { null }
                    )
                    repository.actualizarPerfil(completo)
                    _usuarioActual.value = completo
                    onSuccess()
                }
                .onFailure { _error.value = traducirError(it) }
            _cargando.value = false
        }
    }

    fun cargarUsuario(usuarioId: Int) {
        viewModelScope.launch {
            _usuarioActual.value = repository.getUsuarioById(usuarioId)
        }
    }

    fun actualizarPerfil(usuario: Usuario, onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.actualizarPerfil(usuario)
            _usuarioActual.value = usuario
            onSuccess()
        }
    }

    fun cerrarSesion() {
        authRepository.cerrarSesion()
        _usuarioActual.value = null
    }

    fun limpiarError() {
        _error.value = null
    }

    private fun traducirError(e: Throwable): String = when {
        e.message?.contains("least 6 characters", true) == true ->
            "La contraseña debe tener al menos 6 caracteres"
        e.message?.contains("email address is already", true) == true ->
            "Ya existe una cuenta con ese correo"
        e.message?.contains("badly formatted", true) == true ->
            "El correo no tiene un formato válido"
        e.message?.contains("no user record", true) == true ||
                e.message?.contains("password is invalid", true) == true ||
                e.message?.contains("credential is incorrect", true) == true ->
            "Correo o contraseña incorrectos"
        e.message?.contains("network", true) == true ->
            "Sin conexión. Revisa tu internet."
        else -> e.message ?: "Error de autenticación"
    }

    companion object {
        fun Factory(context: Context): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val db = AppDatabase.getInstance(context.applicationContext)
                UsuarioViewModel(UsuarioRepository(db.usuarioDao()))
            }
        }
    }
}