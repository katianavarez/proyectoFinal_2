package navarez.katia.proyectofinal.navigation

import kotlinx.serialization.Serializable

@Serializable
object Login

@Serializable
object Registro

@Serializable
data class ListaLibros(val usuarioId: Int)

@Serializable
data class AgregarLibro(val usuarioId: Int)

@Serializable
data class Perfil(val usuarioId: Int)

@Serializable
data class Estadisticas(val usuarioId: Int)

@Serializable
data class Detalle(val libroId: Int, val usuarioId: Int)