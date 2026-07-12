package navarez.katia.proyectofinal.data.repository

import navarez.katia.proyectofinal.data.database.UsuarioDAO
import navarez.katia.proyectofinal.model.Usuario

class UsuarioRepository(private val usuarioDAO: UsuarioDAO) {

    suspend fun obtenerOCrearPerfil(uid: String, correo: String, nombre: String): Usuario {
        usuarioDAO.getUsuarioByUid(uid)?.let { return it }
        val nuevo = Usuario(uid = uid, nombre = nombre, correo = correo)
        val idLocal = usuarioDAO.insertUsuario(nuevo).toInt()
        return nuevo.copy(id = idLocal)
    }

    suspend fun getUsuarioByUid(uid: String): Usuario? = usuarioDAO.getUsuarioByUid(uid)

    suspend fun getUsuarioById(usuarioId: Int): Usuario? = usuarioDAO.getUsuarioById(usuarioId)

    suspend fun actualizarPerfil(usuario: Usuario) = usuarioDAO.updateUsuario(usuario)

    suspend fun resolverUsuarioIdValido(idPreferido: Int): Int {
        usuarioDAO.getUsuarioById(idPreferido)?.let { return it.id }
        usuarioDAO.getCualquierUsuario()?.let { return it.id }
        return idPreferido
    }
}