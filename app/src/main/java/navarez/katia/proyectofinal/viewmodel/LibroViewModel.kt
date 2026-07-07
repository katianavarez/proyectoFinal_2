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
import navarez.katia.proyectofinal.data.repository.LibroRepository
import navarez.katia.proyectofinal.data.repository.UsuarioRepository
import navarez.katia.proyectofinal.model.Libro

class LibroViewModel(
    private val libroRepository: LibroRepository,
    private val usuarioRepository: UsuarioRepository
) : ViewModel() {

    private val _libros = MutableStateFlow<List<Libro>>(emptyList())
    val libros: StateFlow<List<Libro>> = _libros

    private val _libroDetalle = MutableStateFlow<Libro?>(null)
    val libroDetalle: StateFlow<Libro?> = _libroDetalle

    fun cargarLibros(usuarioId: Int) {
        viewModelScope.launch {
            libroRepository.getLibrosByUsuario(usuarioId).collect {
                _libros.value = it
            }
        }
    }

    fun cargarLibro(libroId: Int) {
        viewModelScope.launch {
            _libroDetalle.value = libroRepository.getLibroById(libroId)
        }
    }

    fun guardarLibro(
        usuarioId: Int,
        libro: Libro,
        onResultado: (Result<Unit>) -> Unit
    ) {
        viewModelScope.launch {
            val resultado = runCatching {
                val idValido = usuarioRepository.resolverUsuarioIdValido(usuarioId)
                libroRepository.insertLibro(libro.copy(usuarioId = idValido))
                Unit
            }
            onResultado(resultado)
        }
    }

    fun actualizarLibro(libro: Libro, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            libroRepository.updateLibro(libro)
            _libroDetalle.value = libro
            onSuccess()
        }
    }

    fun eliminarLibro(libro: Libro, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            libroRepository.deleteLibro(libro)
            onSuccess()
        }
    }

    companion object {
        fun Factory(context: Context): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val db = AppDatabase.getInstance(context.applicationContext)
                LibroViewModel(
                    LibroRepository(db.libroDao()),
                    UsuarioRepository(db.usuarioDao())
                )
            }
        }
    }
}
