package itesm.rieti.model.esquemas

import itesm.rieti.model.enums.Status
import java.time.LocalDateTime

/**
 * Clase de datos que representa un expediente registrado en el sistema.
 *
 * @property folioExpediente Folio único e identificador del expediente.
 * @property alimentador Correo del alimentador (usuario) que dio de alta el expediente.
 * @property status Estado actual del expediente (por defecto es REGISTRADO).
 * @property descripcion Descripción general de la situación del expediente.
 * @property ultimaActualizacion Fecha y hora de la última vez que el expediente fue actualizado.
 */
data class Expediente(
    val folioExpediente: String,
    val alimentador: String,
    val status: Status = Status.REGISTRADO,
    val descripcion: String?,
    val ultimaActualizacion: LocalDateTime
)