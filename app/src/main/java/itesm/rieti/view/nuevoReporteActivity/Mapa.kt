package itesm.rieti.view.nuevoReporteActivity

import android.content.Context
import android.location.Geocoder
import android.util.Log
import android.view.MotionEvent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraMoveStartedReason
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.rememberCameraPositionState
import itesm.rieti.model.api.FormError
import itesm.rieti.viewModel.nuevoReporte.NuevoReporteState
import itesm.rieti.viewModel.nuevoReporte.NuevoReporteVM
import itesm.rieti.viewModel.nuevoReporte.UbicacionVM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

suspend fun processAddressData(
    context: Context,
    nuevoReporteVM: NuevoReporteVM,
    cameraPositionState: CameraPositionState
) {
    val centroActual = cameraPositionState.position.target
    withContext(Dispatchers.IO) {
        try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val address = geocoder.getFromLocation(centroActual.latitude, centroActual.longitude, 1)?.firstOrNull()
            nuevoReporteVM.setAddress(address)
        } catch (e: Exception) {
            Log.e("Ubicacion", "Error al obtener la dirección: ${e.message}", e)
            nuevoReporteVM.setAddress(null)
        }
    }
}
@Composable
fun MapScreen(
    context: Context,
    ubicacionVM: UbicacionVM,
    nuevoReporteVM: NuevoReporteVM,
    nuevoReporteState: NuevoReporteState,
    modifier: Modifier = Modifier,
    onCameraMoved: () -> Unit,
    onCameraIdle: () -> Unit,
    onUbicacionSelected: (Float, Float) -> Unit
) {
    val ubicacion by ubicacionVM.ubicacion.collectAsState()
    if (ubicacion == null) return
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(ubicacion!!.latitude, ubicacion!!.longitude), 35f)
    }
    var dynamicModifier = modifier

    val coroutineScope = rememberCoroutineScope()

    // Establecer la ubicación inicial por defecto
    LaunchedEffect(Unit) {
        if (ubicacion != null) {
            onUbicacionSelected(ubicacion!!.latitude.toFloat(), ubicacion!!.longitude.toFloat())
            processAddressData(context, nuevoReporteVM, cameraPositionState)
        }
    }

    // Escuchar cuando la cámara deje de moverse (Equivalente a OnCameraIdle / dragend)
    LaunchedEffect(cameraPositionState.isMoving) {
        if (!cameraPositionState.isMoving) {
            onCameraIdle()
            if (cameraPositionState.cameraMoveStartedReason == CameraMoveStartedReason.GESTURE) {
                processAddressData(context, nuevoReporteVM, cameraPositionState)
            }
        }
    }

    Column {
        Text(
            text = buildAnnotatedString {
                append("Ubicación")
                withStyle(style = SpanStyle(color = Color.Red)) {
                    append(" *")
                }
            },
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Ubicación seleccionada",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = nuevoReporteState.reporte.direccion ?: "",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (nuevoReporteState.errors.contains(FormError.InvalidLocation)) {
            Log.i("Address", "Error found WITHIN Mapa.kt")
            dynamicModifier = modifier.border(
                width = 3.dp,
                color = MaterialTheme.colorScheme.error,
                shape = MaterialTheme.shapes.medium
            )
            Text(
                text = nuevoReporteState.errors[FormError.InvalidLocation]!!,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
        Box (modifier = dynamicModifier.fillMaxSize()) {
            GoogleMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .pointerInteropFilter { event ->
                        if (event.action == MotionEvent.ACTION_DOWN) {
                            onCameraMoved()
                        }
                        false
                    },
                cameraPositionState = cameraPositionState,
                properties = MapProperties(isMyLocationEnabled = true),
                onMapClick = { latLng ->
                    onUbicacionSelected(latLng.latitude.toFloat(), latLng.longitude.toFloat())
                    coroutineScope.launch {
                        cameraPositionState.animate(
                            update = CameraUpdateFactory.newLatLng(latLng),
                            durationMs = 1000
                        )
                    }
                }
            )
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Centro del mapa",
                tint = Color.Red,
                modifier = modifier
                    .padding(bottom = 35.dp)
                    .size(32.dp)
                    .align(Alignment.Center)
            )
        }
    }
}