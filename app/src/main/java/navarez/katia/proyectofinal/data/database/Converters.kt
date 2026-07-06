package navarez.katia.proyectofinal.data.database

import androidx.room.TypeConverter
import navarez.katia.proyectofinal.model.EstadoLibro

class Converters {

    @TypeConverter
    fun fromEstadoLibro(estado: EstadoLibro): String {
        return estado.name
    }

    @TypeConverter
    fun toEstadoLibro(valor: String): EstadoLibro {
        return EstadoLibro.valueOf(valor)
    }
}
