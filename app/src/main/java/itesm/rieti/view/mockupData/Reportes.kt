package itesm.rieti.view.mockupData

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import itesm.rieti.model.enums.Municipio
import itesm.rieti.model.enums.Status
import itesm.rieti.model.enums.TipoTrabajo
import itesm.rieti.model.esquemas.Expediente
import itesm.rieti.model.esquemas.Reporte
import java.time.LocalDateTime

class ExpedienteMockups : PreviewParameterProvider<Expediente> {
    override val values = sequenceOf(
        Expediente(
            folioExpediente = "20260906JVLA4HFD",
            alimentador = "Antonio Cedillo",
            status = Status.REGISTRADO,
            descripcion = null,
            ultimaActualizacion = LocalDateTime.of(2026, 9, 10, 11, 43)
        ),
        Expediente(
            folioExpediente = "20260903JGMR8CHM",
            alimentador = "Marco Antonio",
            status = Status.EN_REVISION,
            descripcion = "Revisando la veracidad de los datos del reporte",
            ultimaActualizacion = LocalDateTime.of(2026, 9, 5, 14, 54)
        ),
        Expediente(
            folioExpediente = "20260829ACOL1TRF",
            alimentador = "Roberto Román",
            status = Status.EN_SEGUIMIENTO,
            descripcion = "El alimentador se encuentra atendiendo el caso",
            ultimaActualizacion = LocalDateTime.of(2026, 9, 8, 8, 26)
        )
    )
}

class ReporteMockups : PreviewParameterProvider<Reporte> {
    override val values = sequenceOf(
        Reporte(
            folioReporte = "20260906ATZL87",
            descripcion = "Vi a dos chicos de aprox. 14 años vendiendo mazapanes en el semáforo",
            fechaYHora = LocalDateTime.of(2026, 9, 6, 14, 40),
            numeroNNA = 2,
            rangoEdad = "12 a 15 años",
            tipoTrabajo = TipoTrabajo.MENDICION_FORZADA,
            municipio = Municipio.ATIZAPAN,
            expediente = ExpedienteMockups().values.toList()[0]
        ),
        Reporte(
            folioReporte = "20260903ECAR64",
            descripcion = "Vi a una niña y un niño de aprox. 10 años lavando parabrisas en el semáforo",
            fechaYHora = LocalDateTime.of(2026, 9, 3, 11, 18),
            expediente = ExpedienteMockups().values.toList()[1],
            numeroNNA = 2,
            rangoEdad = "10 años",
            tipoTrabajo = TipoTrabajo.MENDICION_FORZADA,
            municipio = Municipio.ECATEPEC,
        ),
        Reporte(
            folioReporte = "20260829COAR24",
            descripcion = "Hay cinco jóvenes como de 15 años vendiendo chicles en la esquina",
            fechaYHora = LocalDateTime.of(2026, 8, 29, 9, 46),
            expediente = ExpedienteMockups().values.toList()[2],
            numeroNNA = 5,
            rangoEdad = "15 años",
            tipoTrabajo = TipoTrabajo.MENDICION_FORZADA,
            municipio = Municipio.COACALCO
        )

    )
}