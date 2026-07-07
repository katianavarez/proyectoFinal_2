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
import navarez.katia.proyectofinal.data.repository.UsuarioRepository
import navarez.katia.proyectofinal.model.Usuario

class UsuarioViewModel(private val repository: UsuarioRepository) : ViewModel() {

    private val _usuarioActual = MutableStateFlow<Usuario?>(null)
    val usuarioActual: StateFlow<Usuario?> = _usuarioActual

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun login(correo: String, password: String, onSuccess: (Int) -> Unit) {
        viewModelScope.launch {
            val usuario = repository.login(correo, password)
            if (usuario != null) {
                _usuarioActual.value = usuario
                onSuccess(usuario.id)
            } else {
                _error.value = "Correo o contraseña incorrectos"
            }
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
            val usuario = Usuario(
                nombre = nombre,
                correo = correo,
                password = password,
                fechaNacimiento = fechaNacimiento.ifEmpty { null },
                genero = genero.ifEmpty { null },
                profesion = profesion.ifEmpty { null }
            )
            val result = repository.registrar(usuario)
            result.onSuccess { onSuccess() }
            result.onFailure { _error.value = it.message }
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

    fun limpiarError() {
        _error.value = null
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
