package itesm.rieti.view.historialActivity

import android.content.ClipData
import android.graphics.Color.rgb
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import itesm.rieti.viewModel.history.HistorialState
import itesm.rieti.viewModel.history.HistorialVM
import coil3.compose.AsyncImage
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import itesm.rieti.model.api.evidencias.ManejadorEvidencia
import itesm.rieti.model.esquemas.Evidencia
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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
    Log.i("Detalles", "Reporte Response: ${historialState.selectedReporte}")
    Log.i("Detalles", "Status: ${historialState.selectedReporte?.expediente?.status?.desc}")

    val selectedReporte = reporteResponse ?: historialState.selectedReporte ?: return
    val reporte = selectedReporte.reporte

    val estadoScroll = rememberScrollState()
    val clipboard = LocalClipboard.current
    val scope = rememberCoroutineScope()

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
                modifier = modifier
                    .weight(1f)
                    .size(25.dp)
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
                modifier = modifier
                    .weight(1f)
                    .size(25.dp),
                enabled = reporte.folioE != null
            ) {
                Icon(
                    painter = painterResource(R.drawable.pencil),
                    contentDescription = "Edit Report Details",
                    tint = if (reporte.folioE != null) MaterialTheme.colorScheme.onSurface else Color.Transparent
                )
            }
        }
        Spacer(modifier.height(8.dp))
        Text(
            text = buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        fontWeight = FontWeight.SemiBold,
                    )
                ) { append("Estado: ") }
                withStyle(
                    SpanStyle(
                        fontWeight = FontWeight.Bold,
                        color = selectedReporte.expediente?.status?.color ?: Color(rgb(255, 62, 68))
                    )
                ) { append(selectedReporte.expediente?.status?.desc ?: "Pendiente") }
            },
            fontWeight = FontWeight.Medium,
            style = MaterialTheme.typography.titleMedium,
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                            textDecoration = TextDecoration.Underline
                        )
                    ) {
                        append(reporte.folio)
                    }
                },
                fontWeight = FontWeight.Light,
                modifier = modifier
                    .padding(end = 8.dp)
                    .clickable(
                        onClick = {
                            scope.launch {
                                clipboard.setClipEntry(
                                    ClipEntry(
                                        ClipData.newPlainText(
                                            "Folio de reporte copiado (${reporte.folio})",
                                            reporte.folio
                                        )
                                    )
                                )
                            }
                        }
                    ),
                textAlign = TextAlign.Center,
            )
            IconButton(
                onClick = {
                    scope.launch {
                        clipboard.setClipEntry(ClipEntry(ClipData.newPlainText("Folio de reporte copiado (${reporte.folio})", reporte.folio)))
                    }
                },
                modifier = modifier.size(15.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.copy),
                    contentDescription = "Copy to clipboard",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        Spacer(modifier.height(8.dp))
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(estadoScroll)
        ) {
            EvidenciasReporte(folio = reporte.folio)
            DateLabel(reporte.dia)
            NNALabel(reporte.numNinios)
            RangoEdadLabel(reporte.rangoEdad!!)
            TipoTrabajoLabel(reporte.tipoTrabajo!!)
            if (reporte.detallesAdicionales != null) {
                Spacer(modifier.height(14.dp))
                Text(
                    text = "Detalles adicionales:",
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = reporte.detallesAdicionales,
                    fontWeight = FontWeight.Light,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
            if (selectedReporte.expediente != null) {
                DetallesExpediente(selectedReporte.expediente)
            }
            Spacer(modifier.height(14.dp))
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        SpanStyle(fontWeight = FontWeight.Bold)
                    ) { append("Ubicación: ") }
                    withStyle(
                        SpanStyle(fontWeight = FontWeight.Light)
                    ) { append(" ${reporte.municipio ?: ""}") }
                },
                style = MaterialTheme.typography.titleMedium
            )
            if (reporte.direccion != null) {
                Text(
                    text = reporte.direccion,
                    fontWeight = FontWeight.Light,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Spacer(modifier.height(8.dp))
            MapaEstatico(
                latitud = reporte.latitud,
                longitud = reporte.longitud
            )
        }
    }
}

@Composable
fun DateLabel(date: String?) {
    val dateFormatted = try {
        date?.let {
            LocalDateTime.parse(it).format(DateTimeFormatter.ofPattern("d MMM uuuu, hh:mm a"))
        } ?: ""
    } catch (_: Exception) { date ?: "" }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Fecha:",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = dateFormatted,
            fontWeight = FontWeight.Light,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
fun NNALabel(nna: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Número de NNA:",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = nna.toString(),
            fontWeight = FontWeight.Light,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
fun RangoEdadLabel(rangoEdad: RangoEdad) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Rango de edad:",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = rangoEdad.desc,
            fontWeight = FontWeight.Light,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
fun TipoTrabajoLabel(tipoTrabajo: TipoTrabajo) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Tipo de trabajo:",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = tipoTrabajo.desc,
            fontWeight = FontWeight.Light,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
fun EvidenciasReporte(folio: String, modifier: Modifier = Modifier) {
    var evidencias by remember(folio) { mutableStateOf<List<Evidencia>>(emptyList()) }
    var cargando by remember(folio) {
        mutableStateOf(true)
    }
    var error by remember(folio) { mutableStateOf(false) }

    LaunchedEffect(folio) {
        cargando = true
        error = false
        evidencias = emptyList()

        try {
            val response = ManejadorEvidencia.ObtenerEvidencia(folio)

            if (response.isSuccessful) {
                evidencias = response.body() ?: emptyList()
            } else {
                error = true
            }
        } catch (_: Exception) {
            error = true
        } finally {
            cargando = false
        }
    }
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Evidencias del reporte",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
        )
        when {
            cargando -> { CircularProgressIndicator() }
            error -> { Text(text = "Error al cargar evidencias") }
            evidencias.isEmpty() -> { Text(text = "Este reporte no tiene evidencias") }
            else -> {
                evidencias.forEach { evidencia ->
                    AsyncImage(
                        model = evidencia.url,
                        contentDescription = "Fotografía del reporte",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }
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
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = "Detalles Expediente",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        modifier = modifier.fillMaxWidth(),
        textAlign = TextAlign.Left
    )
    Spacer(modifier = Modifier.height(2.dp))
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                MaterialTheme.colorScheme.outline,
                shape = MaterialTheme.shapes.medium
            )
            .padding(8.dp)
    ) {
        FolioExpedienteLabel(expediente.folioE)
        LugarLabel(expediente.lugar)
        if (expediente.descripcion != null) DescripcionExpedienteLabel(expediente.descripcion)
        if (expediente.correoAl != null) CorreoAlimentadorLabel(expediente.correoAl)
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun FolioExpedienteLabel(folio: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Folio Expediente:",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = folio,
            fontWeight = FontWeight.Light,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
fun LugarLabel(lugar: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Lugar:",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = lugar,
            fontWeight = FontWeight.Light,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
fun CorreoAlimentadorLabel(correo: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Correo Alimentador:",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = correo,
            fontWeight = FontWeight.Light,
            style = MaterialTheme.typography.titleMedium
        )
    }
}
@Composable
fun DescripcionExpedienteLabel(descripcion: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Descripción:",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = descripcion,
            fontWeight = FontWeight.Light,
            style = MaterialTheme.typography.titleMedium
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
        ),
        expediente = Expediente(
            folioE = "EXP-12345",
            lugar = "Lugar del expediente de prueba",
            descripcion = "Descripción del expediente de prueba.",
            correoAl = "someone@somewhere.com",
        )
    )
    DetallesReporteActivity(historialVM, historialState, reporteResponse = sampleReporte)
}