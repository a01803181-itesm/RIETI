package itesm.rieti.view.HistorialActivity

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import itesm.rieti.R
import itesm.rieti.model.esquemas.Borrador
import itesm.rieti.model.esquemas.Reporte
import itesm.rieti.view.mockupData.BorradorMockUps
import itesm.rieti.view.mockupData.ReporteMockups

@Composable
fun TarjetaReporte(reporte: Reporte, onReporte: (Reporte) -> Unit, modifier: Modifier = Modifier) {
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
                        onClick = { onReporte(reporte) },
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
fun TarjetaBorrador(borrador: Borrador, modifier: Modifier = Modifier) {
    Card(modifier = modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Column(
            modifier = modifier.wrapContentHeight().padding(all = 18.dp)
        ) {
            DosColumnas(
                contenidoColumna1 = {
                    Column(
                        modifier = modifier.weight(1f)
                    ) {
                        Text(
                            text = borrador.municipio.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                        )
                    }
                },
                contenidoColumna2 = {
                    Column(
                        modifier = modifier.weight(1f),
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = "BORRADOR",
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                        )
                    }
                }
            )
            Spacer(Modifier.height(8.dp))
            if (borrador.descripcion != null) {
                Text(
                    text = "Descripción: ${borrador.descripcion}",
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                )
                Spacer(Modifier.height(8.dp))
            }
            Row(
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = modifier.weight(1f)
                ) {
                    Text(
                        text = borrador.fechaYHora.toString(),
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

@Preview
@Composable
fun TarjetaReportePreview() {
    val reporte: Reporte = ReporteMockups().values.toList()[0]
    TarjetaReporte(reporte, { })
}

@Preview
@Composable
fun TarjetaBorradorPreview() {
    val borrador: Borrador = BorradorMockUps().values.toList()[0]
    TarjetaBorrador(borrador)
}