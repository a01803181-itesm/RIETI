package itesm.rieti.model.esquemas

import itesm.rieti.model.enums.Municipio
import itesm.rieti.model.enums.TipoTrabajo
import java.time.LocalDateTime

data class Reporte(
    val folio: String? = null,
    val expediente: Expediente? = null,
    val edad: Int? = null,
    val dia: String? = null,
    val tipoTrabajo: TipoTrabajo? = null,
    val numNinios: Int? = null,
    val direccion: String? = null,
    val municipio: Municipio? = null,
    val latitud: Float? = null,
    val longitud: Float? = null,
    val nombre: String? = null,
    val ap_paterno: String? = null,
    val ap_materno: String? = null,
    val detalles_adicionales: String? = null,
    val correoU: String? = null,
    val folioE: String? = null,
    val correoAl: String? = null
)