package itesm.rieti.view.mockupData

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import itesm.rieti.model.enums.Municipio
import itesm.rieti.model.enums.TipoTrabajo
import itesm.rieti.model.esquemas.Reporte
import java.time.LocalDateTime

class ReporteMockups : PreviewParameterProvider<Reporte> {
    override val values = sequenceOf(
        Reporte(
            folio = "20260906ATZL87",
            detalles_adicionales = "Vi a dos chicos de aprox. 14 años vendiendo mazapanes en el semáforo",
            dia = LocalDateTime.of(2026, 9, 6, 14, 40),
            numNinios = 2,
            edad = 12,
            tipoTrabajo = TipoTrabajo.MENDICION_FORZADA,
            municipio = Municipio.ATIZAPAN,
            expediente = ExpedienteMockups().values.toList()[0]
        ),
        Reporte(
            folio = "20260903ECAR64",
            detalles_adicionales = "Vi a una niña y un niño de aprox. 10 años lavando parabrisas en el semáforo",
            dia = LocalDateTime.of(2026, 9, 3, 11, 18),
            expediente = ExpedienteMockups().values.toList()[1],
            numNinios = 2,
            edad = 10,
            tipoTrabajo = TipoTrabajo.MENDICION_FORZADA,
            municipio = Municipio.ECATEPEC,
        ),
        Reporte(
            folio = "20260829COAR24",
            detalles_adicionales = "Hay cinco jóvenes como de 15 años vendiendo chicles en la esquina",
            dia = LocalDateTime.of(2026, 8, 29, 9, 46),
            expediente = ExpedienteMockups().values.toList()[2],
            numNinios = 5,
            edad = 15,
            tipoTrabajo = TipoTrabajo.MENDICION_FORZADA,
            municipio = Municipio.COACALCO
        )

    )
}