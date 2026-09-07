package itesm.rieti.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import itesm.rieti.view.mockupData.Reporte
import itesm.rieti.view.mockupData.ReporteMockups
import itesm.rieti.R

@Composable
fun HistorialActivity(mockItems: List<Reporte>, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(all = 14.dp).fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Historial de Reportes",
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
        )

        Spacer(Modifier.height(18.dp))

        LazyColumn {
            items(mockItems) { reporte ->
                TarjetaReporte(reporte)
            }
        }
    }
}

@Composable
fun TarjetaReporte(reporte: Reporte, modifier: Modifier = Modifier) {
    Card(modifier = modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Column(
            modifier = modifier.wrapContentHeight().padding(all = 18.dp)
        ) {
            Row(
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = modifier.weight(1f)
                ) {
                    Text(
                        text = reporte.municipio.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                    )
                    Text(
                        text = reporte.folioReporte,
                        fontWeight = FontWeight.Light,
                        fontSize = 14.sp,
                    )
                }
                Column(
                    modifier = modifier.weight(1f),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = reporte.status.name,
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp,
                    )
                }
            }
            Spacer(Modifier.height(14.dp))
            Text(
                text = reporte.descripcion,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
            )
            Spacer(Modifier.height(14.dp))
            Row(
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = modifier.weight(1f)
                ) {
                    Text(
                        text = reporte.fechaYHora.toString(),
                    )
                }
                Column(
                    modifier = modifier.weight(1f),
                    horizontalAlignment = Alignment.End
                ) {
                    IconButton(
                        onClick = {},
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color.Transparent
                        )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.long_arrow),
                            contentDescription = "Further info",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TarjetaReportePreview() {
    val mockItems: List<Reporte> = ReporteMockups().values.toList()
    HistorialActivity(mockItems)
}