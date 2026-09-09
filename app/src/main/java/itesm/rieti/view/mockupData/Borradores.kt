package itesm.rieti.view.mockupData

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import itesm.rieti.model.enums.Municipio
import itesm.rieti.model.enums.TipoTrabajo
import itesm.rieti.model.esquemas.Borrador
import java.time.LocalDateTime

class BorradorMockUps : PreviewParameterProvider<Borrador> {
    override val values = sequenceOf(
        Borrador(
            idBorrador = 1,
            fechaYHora = LocalDateTime.of(2026, 9, 9, 10, 6),
            municipio = Municipio.NAUCALPAN,
            descripcion = null,
            numeroNNA = null,
            rangoEdad = null,
            tipoTrabajo = null,
        ),
        Borrador(
            idBorrador = 2,
            fechaYHora = LocalDateTime.of(2026, 9, 8, 13, 14),
            municipio = Municipio.CUAUTITLAN,
            descripcion = null,
            numeroNNA = null,
            rangoEdad = null,
            tipoTrabajo = TipoTrabajo.MENDICION_FORZADA,
        ),
        Borrador(
            idBorrador = 3,
            fechaYHora = LocalDateTime.of(2026, 9, 2, 8, 40),
            municipio = Municipio.HUIXQUILUCAN,
            descripcion = "Vi a",
            numeroNNA = null,
            rangoEdad = null,
            tipoTrabajo = null,
        )
    )
}