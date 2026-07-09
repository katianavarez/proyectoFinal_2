package navarez.katia.proyectofinal.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import navarez.katia.proyectofinal.model.Usuario

@Dao
interface UsuarioDAO {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUsuario(usuario: Usuario): Long

    @Update
    suspend fun updateUsuario(usuario: Usuario)

    @Query("SELECT * FROM Usuarios WHERE uid = :uid")
    suspend fun getUsuarioByUid(uid: String): Usuario?

    @Query("SELECT * FROM Usuarios WHERE idUsuario = :usuarioId")
    suspend fun getUsuarioById(usuarioId: Int): Usuario?

    @Query("SELECT * FROM Usuarios LIMIT 1")
    suspend fun getCualquierUsuario(): Usuario?
}