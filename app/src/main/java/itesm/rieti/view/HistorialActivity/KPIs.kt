package itesm.rieti.view.HistorialActivity

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import itesm.rieti.model.esquemas.KPI
import itesm.rieti.view.mockupData.KPIMockups

@Composable
fun GrillaKPIs(modifier: Modifier = Modifier) {
    val KPIs: List<KPI> = KPIMockups().values.toList()
    Column(
        modifier = modifier.height(200.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilaKPIs(KPIs[0], KPIs[1], modifier = modifier.weight(1f))
        FilaKPIs(KPIs[2], KPIs[3], modifier = modifier.weight(1f))
    }
}

@Composable
fun FilaKPIs(kpi1: KPI, kpi2: KPI, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TarjetaKPI(kpi1, modifier = modifier)
        TarjetaKPI(kpi2, modifier = modifier)
    }
}

@Composable
fun TarjetaKPI(kpi: KPI, modifier: Modifier = Modifier) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = modifier.wrapContentHeight().padding(all = 12.dp)
        ) {
            Text(
                text = kpi.parametro,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Light,
                fontSize = 18.sp
            )
            Text(
                text = kpi.valor.toString(),
                textAlign = TextAlign.Left,
                color = kpi.color,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        }
    }
}