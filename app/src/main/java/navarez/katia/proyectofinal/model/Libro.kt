package navarez.katia.proyectofinal.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "Libros",
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["idUsuario"],
            childColumns = ["usuarioId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["usuarioId"])]
)
data class Libro(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idLibro")
    val id: Int = 0,

    @ColumnInfo(name = "usuarioId")
    val usuarioId: Int,

    @ColumnInfo(name = "titulo")
    val titulo: String,

    @ColumnInfo(name = "autor")
    val autor: String,

    @ColumnInfo(name = "categoria")
    val categoria: String,

    @ColumnInfo(name = "generoOTema")
    val generoOTema: String,

    @ColumnInfo(name = "numPaginas")
    val numPaginas: Int,

    @ColumnInfo(name = "sinopsis")
    val sinopsis: String,

    @ColumnInfo(name = "estado")
    val estado: EstadoLibro,

    @ColumnInfo(name = "portada")
    val portada: String? = null,

    @ColumnInfo(name = "paginaActual")
    val paginaActual: Int = 0,

    @ColumnInfo(name = "rating")
    val rating: Float = 0f,

    @ColumnInfo(name = "resena")
    val resena: String = "",

    @ColumnInfo(name = "fechaInicio")
    val fechaInicio: String? = null,

    @ColumnInfo(name = "fechaFin")
    val fechaFin: String? = null
)
