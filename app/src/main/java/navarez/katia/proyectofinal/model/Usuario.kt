package navarez.katia.proyectofinal.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "Usuarios",
    indices = [
        Index(value = ["uid"], unique = true),
        Index(value = ["correo"], unique = true)
    ]
)
data class Usuario(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idUsuario")
    val id: Int = 0,

    // uid de Firebase Auth: enlaza el perfil local con la identidad remota
    @ColumnInfo(name = "uid")
    val uid: String,

    @ColumnInfo(name = "nombre")
    val nombre: String,

    @ColumnInfo(name = "correo")
    val correo: String,

    @ColumnInfo(name = "fechaNacimiento")
    val fechaNacimiento: String? = null,

    @ColumnInfo(name = "genero")
    val genero: String? = null,

    @ColumnInfo(name = "profesion")
    val profesion: String? = null,

    @ColumnInfo(name = "fotoPerfil")
    val fotoPerfil: String? = null
)