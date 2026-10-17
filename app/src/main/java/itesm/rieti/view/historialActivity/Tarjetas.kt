package itesm.rieti.view.historialActivity

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import itesm.rieti.R
import itesm.rieti.model.drafts.Borrador
import itesm.rieti.model.esquemas.ReporteResponse
import itesm.rieti.view.mainActivity.Pantalla
import itesm.rieti.viewModel.history.HistorialState
import itesm.rieti.viewModel.history.HistorialVM
import itesm.rieti.viewModel.nuevoReporte.NuevoReporteVM
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * Componente de tarjeta para mostrar información resumida de un reporte.
 *
 * @param historialVM ViewModel del historial.
 * @param response Datos del reporte a mostrar.
 * @param modifier Modificador para la vista.
 */
@Composable
fun TarjetaReporte(
    historialVM: HistorialVM,
    response: ReporteResponse,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = { historialVM.setReporteSeleccionado(response) },
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
                text = response.reporte.municipio ?: "",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                text = response.reporte.folio,
                fontWeight = FontWeight.Light,
                style = MaterialTheme.typography.labelSmall,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = response.reporte.detallesAdicionales ?: response.reporte.direccion ?: "",
                fontWeight = FontWeight.Normal,
                style = MaterialTheme.typography.bodyMedium,
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
                        response.reporte.dia?.let {
                            LocalDateTime.parse(it).format(DateTimeFormatter.ofPattern("d MMM uuuu, hh:mm a"))
                        } ?: ""
                    } catch (_: Exception) { response.reporte.dia ?: "" }
                    Text(
                        text = dateFormatted,
                        fontWeight = FontWeight.Light,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Column(
                    modifier = modifier.weight(1f),
                    horizontalAlignment = Alignment.End
                ) {
                    IconButton(
                        onClick = { historialVM.setReporteSeleccionado(response) },
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
    navController: NavHostController,
    nuevoReporteVM: NuevoReporteVM,
    historialVM: HistorialVM,
    borrador: Borrador,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(
                onClick = {
                    navController.navigate(Pantalla.RUTA_INICIO)
                    nuevoReporteVM.setSelectedBorrador(borrador)
                }
            )
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
                            style = MaterialTheme.typography.bodyLarge,
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
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            )
            Spacer(Modifier.height(8.dp))
            if (borrador.detallesAdicionales != null) {
                Text(
                    text = "Descripción: ${borrador.detallesAdicionales}",
                    fontWeight = FontWeight.Normal,
                    style = MaterialTheme.typography.bodyMedium,
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
                    val dateFormatted = try {
                        val ldt = borrador.date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
                        ldt.format(DateTimeFormatter.ofPattern("d MMM uuuu, hh:mm a"))
                    } catch (_: Exception) { borrador.date.toString() }
                    Text(
                        text = dateFormatted,
                        fontWeight = FontWeight.Light,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Column(
                    modifier = modifier.weight(1f),
                    horizontalAlignment = Alignment.End
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.long_arrow),
                        contentDescription = "Further info",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(50.dp)
                    )
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
fun TarjetaReportePreview(historialVM: HistorialVM = viewModel(), historialState: HistorialState = HistorialState()) {
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
fun TarjetaBorradorPreview(
    navController: NavHostController = rememberNavController(),
    historialVM: HistorialVM = viewModel(),
    historialState: HistorialState = HistorialState(),
    nuevoReporteVM: NuevoReporteVM = NuevoReporteVM()
) {
    TarjetaBorrador(
        navController,
        nuevoReporteVM,
        historialVM,
        historialState.borradores[0]
    )
}