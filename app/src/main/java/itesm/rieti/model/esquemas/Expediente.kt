package itesm.rieti.model.esquemas

import itesm.rieti.model.enums.Status
import java.time.LocalDateTime

data class Expediente(
    val folioExpediente: String,
    val alimentador: String,
    val status: Status = Status.REGISTRADO,
    val descripcion: String?,
    val ultimaActualizacion: LocalDateTime
)