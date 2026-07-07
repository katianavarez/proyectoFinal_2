package navarez.katia.proyectofinal.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.launch
import navarez.katia.proyectofinal.data.database.AppDatabase
import navarez.katia.proyectofinal.data.repository.LibroRepository
import navarez.katia.proyectofinal.data.repository.UsuarioRepository
import navarez.katia.proyectofinal.model.Libro
class LibroViewModel(
    private val libroRepository: LibroRepository,
    private val usuarioRepository: UsuarioRepository
) : ViewModel() {

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
