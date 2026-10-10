package itesm.rieti.model.esquemas

import com.google.gson.annotations.SerializedName
import itesm.rieti.model.enums.RangoEdad
import itesm.rieti.model.enums.TipoTrabajo

data class Reporte(
    val folio: String = "",
    @SerializedName("edad")
    val rangoEdad: RangoEdad? = null,
    val dia: String? = null,
    val tipoTrabajo: TipoTrabajo? = null,
    val numNinios: Int = 0,
    val direccion: String? = null,
    val municipio: String? = null,
    val latitud: Float? = null,
    val longitud: Float? = null,
    val nombre: String? = null,
    @SerializedName("ap_paterno")
    val apPaterno: String? = null,
    @SerializedName("ap_materno")
    val apMaterno: String? = null,
    @SerializedName("detalles_adicionales")
    val detallesAdicionales: String? = null,
    val correoU: String? = null,
    val folioE: String? = null,
    val correoAl: String? = null
)