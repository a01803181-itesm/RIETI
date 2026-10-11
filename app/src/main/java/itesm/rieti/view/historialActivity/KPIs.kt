package itesm.rieti.view.historialActivity

import android.graphics.Color.rgb
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import itesm.rieti.viewModel.history.HistorialState

/**
 * Enumeración que representa los Indicadores Clave de Rendimiento (KPIs).
 *
 * @property desc Descripción legible del KPI.
 * @property color Color asociado al KPI.
 */
enum class KPI(val desc: String, val color: Color) {
    REPORTES_TOTALES(desc = "Reportes Totales", color = Color(rgb(86, 193, 214))),
    EN_PROGRESO(desc = "En Progreso", color = Color(rgb(206, 124, 71))),
    COMPLETADOS(desc = "Completados", color = Color(rgb(117, 205, 85))),
    PENDIENTES(desc = "Pendientes", color = Color(rgb(255, 62, 68)))
}

/**
 * Componente que organiza y muestra una cuadrícula de KPIs basada en el estado del historial.
 *
 * @param historialState Estado del historial.
 * @param modifier Modificador para la vista.
 */
@Composable
fun GrillaKPIs(
    historialState: HistorialState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.height(200.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilaKPIs(modifier = modifier.weight(1f)) {
            TarjetaKPI(
                kpi = KPI.REPORTES_TOTALES,
                value = historialState.reportes.size,
                modifier = Modifier.weight(1f)
            )
            TarjetaKPI(
                kpi = KPI.EN_PROGRESO,
                value = 0, // TODO: Obtener el número de reportes en progreso
                modifier = Modifier.weight(1f)
            )
        }
        FilaKPIs(modifier = modifier.weight(1f)) {
            TarjetaKPI(
                kpi = KPI.COMPLETADOS,
                value = 0, // TODO: Obtener el número de reportes completados
                modifier = Modifier.weight(1f)
            )
            TarjetaKPI(
                kpi = KPI.PENDIENTES,
                value = 0, // TODO: Obtener el número de reportes pendientes
                modifier = Modifier.weight(1f)
            )
        }
    }
}

/**
 * Componente que representa una fila dentro de la cuadrícula de KPIs.
 *
 * @param modifier Modificador para la vista.
 * @param content Contenido composable que se mostrará en la fila.
 */
@Composable
fun FilaKPIs(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) { content() }
}

/**
 * Tarjeta individual que muestra el nombre y valor de un KPI.
 *
 * @param kpi El tipo de KPI a mostrar.
 * @param value Valor actual del KPI.
 * @param modifier Modificador para la vista.
 */
@Composable
fun TarjetaKPI(
    kpi: KPI,
    value: Int,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Column(
            modifier = modifier.wrapContentHeight().padding(all = 12.dp)
        ) {
            Text(
                text = kpi.desc,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Light,
                fontSize = 18.sp
            )
            Text(
                text = value.toString(),
                textAlign = TextAlign.Left,
                color = kpi.color,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        }
    }
}