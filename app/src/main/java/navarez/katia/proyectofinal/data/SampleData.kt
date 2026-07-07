package navarez.katia.proyectofinal.data

import navarez.katia.proyectofinal.R
import navarez.katia.proyectofinal.model.EstadoLibro
import navarez.katia.proyectofinal.model.Libro

object SampleData {
    val libros = listOf(
        Libro(
            id = 1, usuarioId = 1,
            titulo = "El Alquimista", autor = "Paulo Coelho",
            categoria = "Ficción", generoOTema = "Novela",
            numPaginas = 192,
            sinopsis = "Un joven pastor andaluz emprende un viaje en busca de un tesoro.",
            estado = EstadoLibro.EN_CURSO, paginaActual = 125
        ),
        Libro(
            id = 2, usuarioId = 1,
            titulo = "Cien años de soledad", autor = "Gabriel García Márquez",
            categoria = "Ficción", generoOTema = "Realismo mágico",
            numPaginas = 471,
            sinopsis = "La historia de la familia Buendía a lo largo de varias generaciones.",
            estado = EstadoLibro.TERMINADO, rating = 5f, resena = "Una obra maestra.",
            fechaInicio = "2023-10-01", fechaFin = "2023-10-15"
        ),
        Libro(
            id = 3, usuarioId = 1,
            titulo = "Sapiens", autor = "Yuval Noah Harari",
            categoria = "No ficción", generoOTema = "Historia",
            numPaginas = 443,
            sinopsis = "Una breve historia de la humanidad.",
            estado = EstadoLibro.POR_LEER
        ),
        Libro(
            id = 4, usuarioId = 1,
            titulo = "Clean Code", autor = "Robert C. Martin",
            categoria = "Académico", generoOTema = "Ingeniería de software",
            numPaginas = 352,
            sinopsis = "Buenas prácticas para escribir código mantenible.",
            estado = EstadoLibro.POR_LEER
        )
    )
}
