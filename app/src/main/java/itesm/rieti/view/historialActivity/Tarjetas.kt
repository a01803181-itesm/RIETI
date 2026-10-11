package itesm.rieti.view.historialActivity

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
import itesm.rieti.viewModel.history.HistorialState
import itesm.rieti.viewModel.history.HistorialVM
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Componente de tarjeta para mostrar información resumida de un reporte.
 *
 * @param historialVM ViewModel del historial.
 * @param reporte Datos del reporte a mostrar.
 * @param modifier Modificador para la vista.
 */
@Composable
fun TarjetaReporte(
    historialVM: HistorialVM,
    reporte: Reporte,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = { historialVM.setReporteSeleccionado(reporte) },
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = modifier
                .wrapContentHeight()
                .padding(all = 18.dp)
        ) {
            Text(
                text = reporte.municipio ?: "",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
            )
            Text(
                text = reporte.folio,
                fontWeight = FontWeight.Light,
                fontSize = 10.sp,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = reporte.detallesAdicionales ?: reporte.direccion!!,
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
                    val dateFormatted = try {
                        val parsed = LocalDateTime.parse(reporte.dia!!)
                        parsed.format(DateTimeFormatter.ofPattern("d MMM uuuu, hh:mm a"))
                    } catch (_: Exception) { reporte.dia!! }
                    Text(
                        text = dateFormatted,
                        fontWeight = FontWeight.Light,
                        fontSize = 14.sp
                    )
                }
                Column(
                    modifier = modifier.weight(1f),
                    horizontalAlignment = Alignment.End
                ) {
                    IconButton(
                        onClick = { historialVM.setReporteSeleccionado(reporte) },
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

/**
 * Componente de tarjeta para mostrar información de un borrador guardado.
 *
 * @param historialVM ViewModel del historial.
 * @param borrador Datos del borrador a mostrar.
 * @param modifier Modificador para la vista.
 */
@Composable
fun TarjetaBorrador(
    historialVM: HistorialVM,
    borrador: Borrador,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = modifier
                .wrapContentHeight()
                .padding(all = 18.dp)
        ) {
            DosColumnas(
                contenidoColumna1 = {
                    Column(
                        modifier = modifier.weight(1f)
                    ) {
                        Text(
                            text = borrador.municipio ?: "",
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
                        onClick = { historialVM.setBorradorSeleccionado(borrador) },
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
/**
 * Función auxiliar para crear un diseño de dos columnas.
 *
 * @param contenidoColumna1 Contenido de la primera columna.
 * @param contenidoColumna2 Contenido de la segunda columna.
 * @param modifier Modificador para la vista.
 */
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

/**
 * Vista previa de la tarjeta de reporte.
 *
 * @param historialVM ViewModel del historial.
 * @param historialState Estado del historial.
 */
@Preview
@Composable
fun TarjetaReportePreview(historialVM: HistorialVM = HistorialVM(), historialState: HistorialState = HistorialState()) {
    TarjetaReporte(historialVM, historialState.reportes[0])
}

/**
 * Vista previa de la tarjeta de borrador.
 *
 * @param historialVM ViewModel del historial.
 * @param historialState Estado del historial.
 */
@Preview
@Composable
fun TarjetaBorradorPreview(historialVM: HistorialVM = HistorialVM(), historialState: HistorialState = HistorialState()) {
    TarjetaBorrador(historialVM, historialState.borradores[0])
}