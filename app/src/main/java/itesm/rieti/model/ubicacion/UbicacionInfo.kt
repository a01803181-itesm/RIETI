package itesm.rieti.model.ubicacion

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.location.Location
import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberUpdatedMarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import itesm.rieti.viewModel.UbicacionVM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

/**
 * Composable principal que muestra los detalles de la ubicación actual,
 * un mapa interactivo embebido, un botón para abrir el mapa externo y la sección de geodecodificación.
 *
 * @param viewModel El [UbicacionVM] de donde se observa el estado de la ubicación.
 * @param modifier Modificador para personalizar la presentación y diseño del contenedor.
 */
@Composable
fun UbicacionInfo(viewModel: UbicacionVM, modifier: Modifier = Modifier) {
    val ubicacion by viewModel.ubicacion.collectAsState()
    val contexto = LocalContext.current

    val posicionInicial = LatLng(19.4326, -99.1332)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(posicionInicial, 15f)
    }

    LaunchedEffect(ubicacion) {
        ubicacion?.let {
            val nuevaLatLng = LatLng(it.latitude, it.longitude)
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(nuevaLatLng, 16f),
                durationMs = 1000
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "GPS en Compose",
            style = MaterialTheme.typography.headlineLarge
        )
        Text(
            text = "Ubicacion: ${ubicacion?.let { "${it.latitude}, ${it.longitude}" } ?: "Desconocida"}",
            modifier = Modifier.padding(vertical = 8.dp)
        )

        GoogleMap(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(vertical = 8.dp),
            cameraPositionState = cameraPositionState
        ) {
            ubicacion?.let {
                Marker(
                    state = rememberUpdatedMarkerState(position = LatLng(it.latitude, it.longitude)),
                    title = "Ubicación Actual"
                )
            }
        }

        ElevatedButton(
            onClick = {
                mostrarMapa(ubicacion, contexto)
            }
        ) {
            Text("Ver en App de Mapas")
        }
        GeodecodificarCoordenadas(ubicacion)
    }
}


/**
 * Composable encargado de permitir la geodecodificación inversa de las coordenadas
 * de una [Location] para obtener una dirección legible.
 *
 * @param ubicacion La ubicación actual ([Location]) del dispositivo, o `null` si no se encuentra disponible.
 */
@Composable
fun GeodecodificarCoordenadas(ubicacion: Location?) {
    if (ubicacion == null) {
        return
    }

    val context = LocalContext.current
    var direccionTexto by remember { mutableStateOf("Presiona el botón para buscar dirección") }

    // Coordenadas de la ubicación
    val latitud = ubicacion.latitude
    val longitud = ubicacion.longitude
    // Coroutine Scope para lanzar la búsqueda en el hilo IO al hacer clic
    val coroutineScope = rememberCoroutineScope()

    Column {
        Button(onClick = {
            direccionTexto = "Buscando..."
            // Búsqueda en segundo plano
            coroutineScope.launch {
                direccionTexto = obtenerDireccion(context, latitud, longitud)
            }
        }) {
            Text("Obtener Dirección")
        }

        Text(text = "Resultado: $direccionTexto")
    }
}


/**
 * Funktion suspendida encargada de realizar la geodecodificación inversa de coordenadas
 * en un hilo secundario ([Dispatchers.IO]) utilizando [Geocoder].
 *
 * @param context El [Context] de Android necesario para instanciar [Geocoder].
 * @param latitud La latitud de la ubicación.
 * @param longitud La longitud de la ubicación.
 * @return La dirección formateada en texto o un mensaje descriptivo en caso de no encontrarse o fallar.
 */
suspend fun obtenerDireccion(context: Context, latitud: Double, longitud: Double): String {
    return withContext(Dispatchers.IO) {
        if (!Geocoder.isPresent()) {
            return@withContext "El servicio de Geocoder no está disponible en este dispositivo."
        }

        val geocoder = Geocoder(context, Locale.getDefault())

        try {
            // Manejo de compatibilidad para Android 13+ (API 33) que usa funciones Callback modernas
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // En API 33+ se recomienda usar la versión asíncrona del sistema o emularla.
                // Para mantener el código simple y compatible en una sola función suspendida:
                @Suppress("DEPRECATION")
                val direcciones = geocoder.getFromLocation(latitud, longitud, 1)
                if (!direcciones.isNullOrEmpty()) {
                    val direccion = direcciones[0]
                    // getAddressLine(0) devuelve la dirección completa formateada
                    return@withContext direccion.getAddressLine(0)
                }
            } else {
                // Versión tradicional (Deprecada en API 33 pero necesaria para versiones anteriores)
                @Suppress("DEPRECATION")
                val direcciones = geocoder.getFromLocation(latitud, longitud, 1)
                if (!direcciones.isNullOrEmpty()) {
                    return@withContext direcciones[0].getAddressLine(0)
                }
            }
            "No se encontró ninguna dirección para estas coordenadas."
        } catch (e: Exception) {
            e.printStackTrace()
            "Error al obtener la dirección: ${e.localizedMessage}"
        }
    }
}


/**
 * Abre la ubicación en una aplicación externa de mapas (por ejemplo, Google Maps) mediante un [Intent].
 *
 * @param ubicacion La [Location] a visualizar en el mapa. Si es `null`, no realiza ninguna acción.
 * @param context El [Context] de la aplicación utilizado para iniciar la actividad del Intent.
 */
@SuppressLint("QueryPermissionsNeeded")
fun mostrarMapa(ubicacion: Location?, context: Context) {
    if (ubicacion == null) {
        return
    }

    // 1. Obtener las coordenadas de la ubicación
    val latitud = ubicacion.latitude
    val longitud = ubicacion.longitude
    val zoom = "18" // Nivel de zoom de 0 a 21

    // 2. Construir la URI con el formato geo:lat,lng?z=zoom
    // También puedes usar "geo:0,0?q=$latitud,$longitud(Nombre Marcador)" para poner un pin
    val mapUri = "geo:$latitud,$longitud?z=$zoom".toUri()

    // 3. Crear el Intent de vista (ACTION_VIEW)
    val mapIntent = Intent(Intent.ACTION_VIEW, mapUri).apply {
        // Asegura que se intente abrir directamente en la app de Google Maps
        setPackage("com.google.android.apps.maps")
    }

    // 4. Validar si hay alguna app instalada que pueda manejar el intent antes de iniciarla
    if (mapIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(mapIntent)
    } else {
        // Si el dispositivo no tiene Google Maps, remueve el paquete para abrir el navegador u otra app de mapas
        mapIntent.setPackage(null)
        context.startActivity(mapIntent)
    }
}
