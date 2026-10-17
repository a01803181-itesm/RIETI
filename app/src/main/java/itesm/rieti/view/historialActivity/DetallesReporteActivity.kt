package itesm.rieti.view.historialActivity

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import itesm.rieti.R
import itesm.rieti.model.enums.RangoEdad
import itesm.rieti.model.enums.TipoTrabajo
import itesm.rieti.model.esquemas.Expediente
import itesm.rieti.model.esquemas.Reporte
import itesm.rieti.model.esquemas.ReporteResponse
import itesm.rieti.view.nuevoReporteActivity.MapScreen
import itesm.rieti.viewModel.history.HistorialState
import itesm.rieti.viewModel.history.HistorialVM

/**
 * Pantalla que muestra los detalles completos de un reporte seleccionado.
 *
 * @param historialVM ViewModel del historial.
 * @param historialState Estado del historial actual.
 * @param modifier Modificador para la vista.
 * @param reporteResponse Objeto [ReporteResponse] con los detalles a mostrar.
 */
@Composable
fun DetallesReporteActivity(
    historialVM: HistorialVM,
    historialState: HistorialState,
    modifier: Modifier = Modifier,
    reporteResponse: ReporteResponse? = historialState.selectedReporte
) {
    val selectedReporte = reporteResponse ?: historialState.selectedReporte ?: return
    val reporte = selectedReporte.reporte

    val estadoScroll = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(
                onClick = { historialVM.setReporteSeleccionado(null) },
                modifier = modifier.weight(1f).size(25.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.cross),
                    contentDescription = "Go back"
                )
            }
            Text(
                text = "Detalles Reporte",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
                modifier = modifier.weight(3f),
                textAlign = TextAlign.Center
            )
            IconButton(
                onClick = { },
                modifier = modifier.weight(1f).size(25.dp),
                enabled = reporte.folioE != null // TODO: Adapt legacy logic to new DB structure: && selectedReporte.expediente?.status == Status.REGISTRADO
            ) {
                Icon(
                    painter = painterResource(R.drawable.pencil),
                    contentDescription = "Edit Report Details",
                    tint = if (reporte.folioE != null /* TODO: Adapt legacy logic to new DB structure: && selectedReporte.expediente?.status == Status.REGISTRADO*/) MaterialTheme.colorScheme.onSurface else Color.Transparent
                )
            }
        }
        Spacer(modifier.height(14.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Folio: ${reporte.folio}",
                fontWeight = FontWeight.Light,
                modifier = modifier.padding(end = 8.dp),
                textAlign = TextAlign.Center
            )
            IconButton(
                onClick = {},
                modifier = modifier.size(15.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.copy),
                    contentDescription = "Copy to clipboard",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        Column(
            modifier = modifier.verticalScroll(estadoScroll)
        ) {
            Spacer(modifier.height(14.dp))
            Text(
                text = reporte.detallesAdicionales ?: "",
                fontWeight = FontWeight.Normal,
                style = MaterialTheme.typography.bodyLarge,
            )
            Spacer(modifier.height(14.dp))
            ElementoLista("Fecha:", reporte.dia ?: "")
            ElementoLista("Número de NNA:", reporte.numNinios.toString())
            ElementoLista("Rango de edad:", reporte.rangoEdad?.toString() ?: "")
            ElementoLista("Tipo de trabajo:", reporte.tipoTrabajo?.name ?: "")
            // TODO: Adapt legacy logic to new DB structure: if (selectedReporte.expediente != null) DetallesExpediente(selectedReporte.expediente)
            Spacer(modifier.height(14.dp))
            Text(
                text = "Ubicación",
                fontWeight = FontWeight.Normal,
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                text = reporte.municipio ?: "",
                fontWeight = FontWeight.Medium,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier.height(8.dp))
            MapaEstatico(
                latitud = reporte.latitud,
                longitud = reporte.longitud
            )
        }
    }
}

/**
 * Componente que muestra un mapa estático centrado en las coordenadas especificadas con un pin rojo.
 *
 * @param latitud Coordenada de latitud.
 * @param longitud Coordenada de longitud.
 * @param modifier Modificador para la vista.
 */
@Composable
fun MapaEstatico(
    latitud: Float?,
    longitud: Float?,
    modifier: Modifier = Modifier
) {
    val defaultLat = 19.593161340526745
    val defaultLng = -99.22934729362493

    val lat = if (latitud != null && latitud != 0f) latitud.toDouble() else defaultLat
    val lng = if (longitud != null && longitud != 0f) longitud.toDouble() else defaultLng
    val location = LatLng(lat, lng)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(location, 16f)
    }

    LaunchedEffect(lat, lng) {
        cameraPositionState.position = CameraPosition.fromLatLngZoom(location, 16f)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp)
            .clip(MaterialTheme.shapes.medium)
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false,
                scrollGesturesEnabled = false,
                zoomGesturesEnabled = false,
                tiltGesturesEnabled = false,
                rotationGesturesEnabled = false,
                myLocationButtonEnabled = false
            )
        )
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = "Pin de ubicación",
            tint = Color.Red,
            modifier = Modifier
                .padding(bottom = 32.dp)
                .size(36.dp)
                .align(Alignment.Center)
        )
    }
}

/**
 * Componente que muestra los detalles específicos de un expediente asociado a un reporte.
 *
 * @param expediente El expediente a mostrar.
 * @param modifier Modificador para la vista.
 */
@Composable
fun DetallesExpediente(expediente: Expediente, modifier: Modifier = Modifier) {
    Spacer(modifier.height(14.dp))
    Text(
        text = "Detalles Expediente",
        fontWeight = FontWeight.SemiBold,
        style = MaterialTheme.typography.titleLarge
    )
    ElementoLista("Folio Expediente:", expediente.folioExpediente)
    ElementoLista("Alimentador:", expediente.alimentador)
    ElementoLista("Status:", expediente.status.name)
    if (expediente.descripcion != null) ElementoLista("Descripción:", expediente.descripcion)
    ElementoLista("Última Actualización:", expediente.ultimaActualizacion.toString())
}

/**
 * Componente de interfaz de usuario que representa un elemento en una lista con una etiqueta y su valor.
 *
 * @param etiqueta Texto que describe el valor.
 * @param valor Texto del valor a mostrar.
 * @param modifier Modificador para la vista.
 */
@Composable
fun ElementoLista(etiqueta: String, valor: String, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(
            text = etiqueta,
            fontWeight = FontWeight.Medium,
            style = MaterialTheme.typography.bodyLarge,
            modifier = modifier.weight(1f)
        )
        Text(
            text = valor,
            fontWeight = FontWeight.Normal,
            style = MaterialTheme.typography.bodyLarge,
            modifier = modifier.weight(2f)
        )
    }
}

/**
 * Vista previa de los detalles de un reporte.
 *
 * @param historialVM ViewModel del historial.
 * @param historialState Estado del historial.
 */
@Preview(showBackground = true)
@Composable
fun DetallesReportePreview(historialVM: HistorialVM = viewModel(), historialState: HistorialState = HistorialState()) {
    val sampleReporte = ReporteResponse(
        reporte = Reporte(
            folio = "REP-12345",
            dia = "2023-10-25",
            numNinios = 2,
            rangoEdad = RangoEdad.INFANTES,
            tipoTrabajo = TipoTrabajo.VENTA_AMBULANTE,
            detallesAdicionales = "Detalles de prueba para el reporte.",
            municipio = "Atizapán de Zaragoza"
        )
    )
    DetallesReporteActivity(historialVM, historialState, reporteResponse = sampleReporte)
}