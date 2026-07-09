package navarez.katia.proyectofinal.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import navarez.katia.proyectofinal.data.repository.FirebaseAuthRepository

class AuthViewModel(
    private val repo: FirebaseAuthRepository = FirebaseAuthRepository()
) : ViewModel() {

    private val _usuario = MutableStateFlow<FirebaseUser?>(repo.usuarioActual)
    val usuario: StateFlow<FirebaseUser?> = _usuario

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando

    fun login(correo: String, password: String, onSuccess: (String) -> Unit) {
        viewModelScope.launch {
            _cargando.value = true
            repo.login(correo.trim(), password)
                .onSuccess { _usuario.value = it; onSuccess(it.uid) }
                .onFailure { _error.value = traducir(it) }
            _cargando.value = false
        }
    }

    fun registrar(correo: String, password: String, onSuccess: (String) -> Unit) {
        viewModelScope.launch {
            _cargando.value = true
            repo.registrar(correo.trim(), password)
                .onSuccess { _usuario.value = it; onSuccess(it.uid) }
                .onFailure { _error.value = traducir(it) }
            _cargando.value = false
        }
    }

    fun cerrarSesion() {
        repo.cerrarSesion()
        _usuario.value = null
    }

    fun limpiarError() { _error.value = null }

    private fun traducir(e: Throwable): String = when {
        e.message?.contains("password", true) == true ->
            "La contraseña debe tener al menos 6 caracteres"
        e.message?.contains("email address is already", true) == true ->
            "Ya existe una cuenta con ese correo"
        e.message?.contains("no user record", true) == true ||
                e.message?.contains("credential is incorrect", true) == true ->
            "Correo o contraseña incorrectos"
        else -> e.message ?: "Error de autenticación"
    }
}