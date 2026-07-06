package navarez.katia.proyectofinal.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import navarez.katia.proyectofinal.model.Libro

@Dao
interface LibroDAO {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertLibro(libro: Libro): Long

    @Update
    suspend fun updateLibro(libro: Libro)

    @Delete
    suspend fun deleteLibro(libro: Libro)

    @Query("SELECT * FROM Libros WHERE usuarioId = :usuarioId")
    fun getLibrosByUsuario(usuarioId: Int): Flow<List<Libro>>

    @Query("SELECT * FROM Libros WHERE idLibro = :libroId")
    suspend fun getLibroById(libroId: Int): Libro?
}
