package itesm.rieti.model.esquemas

import com.google.gson.annotations.SerializedName
import itesm.rieti.model.enums.RangoEdad
import itesm.rieti.model.enums.TipoTrabajo

/**
 * Clase de datos que representa un reporte de trabajo infantil o en calle.
 *
 * @property folio Folio único del reporte.
 * @property rangoEdad Rango de edad de los menores involucrados.
 * @property dia Fecha en formato de cadena (día del suceso).
 * @property tipoTrabajo Tipo de trabajo que estaban realizando.
 * @property numNinios Número aproximado de niños observados.
 * @property direccion Dirección aproximada de los hechos.
 * @property municipio Municipio del incidente.
 * @property latitud Coordenada de latitud del reporte.
 * @property longitud Coordenada de longitud del reporte.
 * @property nombre Nombre del menor (opcional).
 * @property apPaterno Apellido paterno del menor (opcional).
 * @property apMaterno Apellido materno del menor (opcional).
 * @property detallesAdicionales Cualquier detalle extra sobre la situación.
 * @property correoU Correo del usuario que genera el reporte.
 * @property folioE Folio del expediente vinculado (si existe).
 * @property correoAl Correo del alimentador que da seguimiento al reporte (si aplica).
 */
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