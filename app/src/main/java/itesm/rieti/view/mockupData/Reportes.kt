package itesm.rieti.view.mockupData

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import itesm.rieti.model.enums.Municipio
import itesm.rieti.model.enums.Status
import itesm.rieti.model.enums.TipoTrabajo
import java.time.LocalDateTime

data class Reporte(
    val folioReporte: String,
    val folioExpediente: String?,
    val alimentador: String?,
    val descripcion: String,
    val fechaYHora: LocalDateTime,
    val status: Status,
    val numeroNNA: Int,
    val rangoEdad: String,
    val tipoTrabajo: TipoTrabajo,
    val municipio: Municipio
)

class ReporteMockups : PreviewParameterProvider<Reporte> {
    override val values = sequenceOf(
        Reporte(
            folioReporte = "20260906ATZL87",
            descripcion = "Vi a dos chicos de aprox. 14 años vendiendo mazapanes en el semáforo",
            fechaYHora = LocalDateTime.of(2026, 9, 6, 14, 40),
            status = Status.REGISTRADO,
            numeroNNA = 2,
            rangoEdad = "12 a 15 años",
            tipoTrabajo = TipoTrabajo.MENDICION_FORZADA,
            municipio = Municipio.ATIZAPAN,
            folioExpediente = null,
            alimentador = null
        ),
        Reporte(
            folioReporte = "20260903ECAR64",
            descripcion = "Vi a una niña y un niño de aprox. 10 años lavando parabrisas en el semáforo",
            fechaYHora = LocalDateTime.of(2026, 9, 3, 11, 18),
            status = Status.EN_REVISION,
            numeroNNA = 2,
            rangoEdad = "10 años",
            tipoTrabajo = TipoTrabajo.MENDICION_FORZADA,
            municipio = Municipio.ECAPTEPEC,
            folioExpediente = null,
            alimentador = null
        ),
        Reporte(
            folioReporte = "pigtjd946",
            descripcion = "Hay cinco jóvenes como de 15 años vendiendo chicles en la esquina",
            fechaYHora = LocalDateTime.of(2026, 8, 29, 9, 46),
            status = Status.EN_SEGUIMIENTO,
            numeroNNA = 5,
            rangoEdad = "15 años",
            tipoTrabajo = TipoTrabajo.MENDICION_FORZADA,
            municipio = Municipio.COACALCO,
            folioExpediente = null,
            alimentador = null
        )

    )
}