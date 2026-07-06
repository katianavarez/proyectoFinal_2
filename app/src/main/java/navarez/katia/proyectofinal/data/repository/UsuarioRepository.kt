package navarez.katia.proyectofinal.data.repository

import android.database.sqlite.SQLiteConstraintException
import navarez.katia.proyectofinal.data.database.UsuarioDAO
import navarez.katia.proyectofinal.model.Usuario

class UsuarioRepository(private val usuarioDAO: UsuarioDAO) {

    suspend fun registrar(usuario: Usuario): Result<Long> {
        return try {
            val id = usuarioDAO.insertUsuario(usuario)
            Result.success(id)
        } catch (e: SQLiteConstraintException) {
            Result.failure(Exception("Ya existe una cuenta con ese correo"))
        }
    }

    suspend fun login(correo: String, password: String): Usuario? {
        return usuarioDAO.login(correo, password)
    }

    suspend fun actualizarPerfil(usuario: Usuario) {
        usuarioDAO.updateUsuario(usuario)
    }

    suspend fun getUsuarioById(usuarioId: Int): Usuario? {
        return usuarioDAO.getUsuarioById(usuarioId)
    }
}
