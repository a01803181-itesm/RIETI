package itesm.rieti.view.mockupData

import android.graphics.Color.rgb
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import itesm.rieti.model.esquemas.KPI

class KPIMockups : PreviewParameterProvider<KPI> {
    override val values = sequenceOf(
        KPI(
            parametro = "Reportes totales",
            valor = 9,
            color = Color(rgb(86, 193, 214))
        ),
        KPI(
            parametro = "En progreso",
            valor = 2,
            color = Color(rgb(206, 124, 71))
        ),
        KPI(
            parametro = "Completados",
            valor = 5,
            color = Color(rgb(117, 205, 85))
        ),
        KPI(
            parametro = "Pendientes",
            valor = 2,
            color = Color(rgb(255, 62, 68))
        )
    )
}