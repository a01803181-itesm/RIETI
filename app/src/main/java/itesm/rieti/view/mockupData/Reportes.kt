package itesm.rieti.view.mockupData

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import itesm.rieti.model.enums.Municipio
import itesm.rieti.model.enums.RangoEdad
import itesm.rieti.model.enums.TipoTrabajo
import itesm.rieti.model.esquemas.Reporte
import java.time.LocalDateTime

class ReporteMockups : PreviewParameterProvider<Reporte> {
    override val values = sequenceOf(
        Reporte(
            folio = "20260906ATZL87",
            detalles_adicionales = "Vi a dos chicos de aprox. 14 años vendiendo mazapanes en el semáforo",
            dia = "2026-09-06T14:40:00",
            numNinios = 2,
            rangoEdad = RangoEdad.PUBERTOS,
            tipoTrabajo = TipoTrabajo.CONSTRUCCION,
            municipio = Municipio.ATIZAPAN,
            expediente = ExpedienteMockups().values.toList()[0]
        ),
        Reporte(
            folio = "20260903ECAR64",
            detalles_adicionales = "Vi a una niña y un niño de aprox. 10 años lavando parabrisas en el semáforo",
            dia = "2026-09-03T11:18:00",
            expediente = ExpedienteMockups().values.toList()[1],
            numNinios = 2,
            rangoEdad = RangoEdad.PUBERTOS,
            tipoTrabajo = TipoTrabajo.TRABAJO_DOMESTICO,
            municipio = Municipio.ECATEPEC,
        ),
        Reporte(
            folio = "20260829COAR24",
            detalles_adicionales = "Hay cinco jóvenes como de 15 años vendiendo chicles en la esquina",
            dia = "2026-08-29T09:46:00",
            expediente = ExpedienteMockups().values.toList()[2],
            numNinios = 5,
            rangoEdad = RangoEdad.JOVENES,
            tipoTrabajo = TipoTrabajo.LIMPIEZA_DE_PARABRISAS,
            municipio = Municipio.COACALCO
        )

    )
}