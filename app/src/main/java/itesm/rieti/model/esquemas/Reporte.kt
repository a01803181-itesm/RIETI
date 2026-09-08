package itesm.rieti.model.esquemas

import itesm.rieti.model.enums.Municipio
import itesm.rieti.model.enums.TipoTrabajo
import java.time.LocalDateTime

data class Reporte(
    val folioReporte: String,
    val expediente: Expediente?,
    val descripcion: String,
    val fechaYHora: LocalDateTime,
    val numeroNNA: Int,
    val rangoEdad: String,
    val tipoTrabajo: TipoTrabajo,
    val municipio: Municipio
)
