package itesm.rieti.model.esquemas

import itesm.rieti.model.enums.TipoTrabajo
import java.time.LocalDateTime

/**
 * Clase de datos que representa un borrador de un reporte incompleto guardado localmente.
 *
 * @property idBorrador Identificador único del borrador en la base de datos local.
 * @property descripcion Detalles adicionales observados.
 * @property fechaYHora Fecha y hora en la que ocurrió el avistamiento.
 * @property numeroNNA Cantidad aproximada de NNA.
 * @property rangoEdad Rango de edad estimado.
 * @property tipoTrabajo Tipo de trabajo que se encontraba realizando.
 * @property municipio Municipio donde fue observado.
 */
data class Borrador(
    val idBorrador: Int,
    val descripcion: String?,
    val fechaYHora: LocalDateTime,
    val numeroNNA: Int?,
    val rangoEdad: String?,
    val tipoTrabajo: TipoTrabajo?,
    val municipio: String?
)
