package itesm.rieti.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import itesm.rieti.view.mockupData.ReporteMockups
import itesm.rieti.R
import itesm.rieti.model.esquemas.KPI
import itesm.rieti.model.esquemas.Reporte
import itesm.rieti.view.mockupData.KPIMockups

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
        Spacer(Modifier.height(12.dp))
        GrillaKPIs()
        Spacer(Modifier.height(12.dp))
        Toggle("Reportes", {}, "Borradores", {})
        Spacer(Modifier.height(6.dp))
        LazyColumn {
            items(mockItems) { reporte ->
                TarjetaReporte(reporte)
            }
        }
    }
}

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

@Composable
fun Toggle(titulo1: String, onClick1: () -> Unit, titulo2: String, onClick2: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        ElementoToggle(
            titulo = titulo1,
            backgroundColor = Color(MaterialTheme.colorScheme.primary.toArgb()),
            onClick = onClick1,
            modifier = modifier.weight(1f)
        )
        ElementoToggle(
            titulo = titulo2,
            backgroundColor = Color(MaterialTheme.colorScheme.secondary.toArgb()),
            onClick = onClick2,
            modifier = modifier.weight(1f)
        )
    }
}

@Composable
fun ElementoToggle(titulo: String, backgroundColor: Color,  onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(backgroundColor)
            .padding(12.dp),
    ) {
        Text(
            text = titulo,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            color = Color.White,
            modifier = modifier.fillMaxWidth()
        )
    }
}

@Composable
fun TarjetaReporte(reporte: Reporte, modifier: Modifier = Modifier) {
    Card(modifier = modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Column(
            modifier = modifier.wrapContentHeight().padding(all = 18.dp)
        ) {
            if (reporte.expediente != null) {
                DosColumnas(
                    contenidoColumna1 = {
                        Column(
                            modifier = modifier.weight(1f)
                        ) {
                            Text(
                                text = reporte.municipio.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                            )
                            Text(
                                text = reporte.folioReporte,
                                fontWeight = FontWeight.Light,
                                fontSize = 10.sp,
                            )
                        }
                    },
                    contenidoColumna2 = {
                        Column(
                            modifier = modifier.weight(1f),
                            horizontalAlignment = Alignment.End
                        ) {
                            Text(
                                text = reporte.expediente.status.name,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                            )
                        }
                    }
                )
            } else {
                Column(
                    modifier = modifier.weight(1f)
                ) {
                    Text(
                        text = reporte.municipio.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                    )
                    Text(
                        text = reporte.folioReporte,
                        fontWeight = FontWeight.Light,
                        fontSize = 10.sp,
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = reporte.descripcion,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
            )
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = modifier.weight(1f)
                ) {
                    Text(
                        text = reporte.fechaYHora.toString(),
                        fontWeight = FontWeight.Light,
                        fontSize = 14.sp
                    )
                }
                Column(
                    modifier = modifier.weight(1f),
                    horizontalAlignment = Alignment.End
                ) {
                    IconButton(
                        onClick = { },
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

@Composable
fun DosColumnas(contenidoColumna1: @Composable () -> Unit, contenidoColumna2: @Composable () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        contenidoColumna1()
        contenidoColumna2()
    }
}

@Preview(showBackground = true)
@Composable
fun TarjetaReportePreview() {
    val mockItems: List<Reporte> = ReporteMockups().values.toList()
    HistorialActivity(mockItems)
}