package itesm.rieti.view.mockupData

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import itesm.rieti.model.enums.Municipio
import itesm.rieti.model.enums.TipoTrabajo
import itesm.rieti.model.esquemas.Reporte
import java.time.LocalDateTime

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