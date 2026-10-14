package itesm.rieti.model.drafts

import androidx.room.Entity
import androidx.room.PrimaryKey
import itesm.rieti.model.enums.RangoEdad
import itesm.rieti.model.enums.TipoTrabajo

@Entity(tableName = "borradores")
data class BorradorEntity(
    @PrimaryKey(autoGenerate = true) val idBorrador: Int = 0,
    val rangoEdad: RangoEdad? = null,
    val dia: String? = null,
    val tipoTrabajo: TipoTrabajo? = null,
    val numNinios: Int = 0,
    val direccion: String? = null,
    val municipio: String? = null,
    val latitud: Float? = null,
    val longitud: Float? = null,
    val nombre: String? = null,
    val apPaterno: String? = null,
    val apMaterno: String? = null,
    val detallesAdicionales: String? = null,
    val correoU: String? = null,
    val folioE: String? = null,
    val correoAl: String? = null,
    val evidenciaURI: String? = null
)