package navarez.katia.proyectofinal.data.repository

import kotlinx.coroutines.flow.Flow
import navarez.katia.proyectofinal.data.database.LibroDAO
import navarez.katia.proyectofinal.model.Libro

class LibroRepository(private val libroDAO: LibroDAO) {

    fun getLibrosByUsuario(usuarioId: Int): Flow<List<Libro>> {
        return libroDAO.getLibrosByUsuario(usuarioId)
    }

    suspend fun getLibroById(libroId: Int): Libro? {
        return libroDAO.getLibroById(libroId)
    }

    suspend fun insertLibro(libro: Libro): Long {
        return libroDAO.insertLibro(libro)
    }

    suspend fun updateLibro(libro: Libro) {
        libroDAO.updateLibro(libro)
    }

    suspend fun deleteLibro(libro: Libro) {
        libroDAO.deleteLibro(libro)
    }
}
