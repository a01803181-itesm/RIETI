package itesm.rieti.view.mockupData

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import itesm.rieti.model.esquemas.KPI

class KPIMockups : PreviewParameterProvider<KPI> {
    override val values = sequenceOf(
        KPI(
            parametro = "Reportes totales",
            valor = 9,
            color = Color.Black
        ),
        KPI(
            parametro = "En progreso",
            valor = 2,
            color = Color.Yellow
        ),
        KPI(
            parametro = "Completados",
            valor = 5,
            color = Color.Green
        ),
        KPI(
            parametro = "Pendientes",
            valor = 2,
            color = Color.Red
        )
    )
}