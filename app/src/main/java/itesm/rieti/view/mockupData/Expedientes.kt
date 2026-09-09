package itesm.rieti.view.mockupData

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import itesm.rieti.model.enums.Status
import itesm.rieti.model.esquemas.Expediente
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
