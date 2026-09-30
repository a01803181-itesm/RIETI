package itesm.rieti.model.esquemas

import itesm.rieti.model.enums.Municipio
import itesm.rieti.model.enums.TipoTrabajo
import java.time.LocalDateTime

data class Reporte(
    val folio: String,
    val rangoEdad: String,
    val expediente: Expediente?,
    val descripcion: String,
    val fechaYHora: LocalDateTime,
    val numeroNNA: Int,
    val tipoTrabajo: TipoTrabajo,
    val municipio: Municipio
)

//folio: str
//edad: int
//dia: AwareDatetime
//tipoTrabajo: str
//numNNA: int
//direccion: str
//latitud: Latitude
//longitud: Longitude
//correoU: EmailStr
//folioE: str
//correoAl: EmailStr