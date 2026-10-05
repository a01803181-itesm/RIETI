package itesm.rieti.model.ubicacion

import android.Manifest
import android.content.pm.PackageManager
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import itesm.rieti.viewModel.UbicacionVM

/**
 * Clase encargada de obtener la ubicacion del dispositivo validando los permisos
 * y actualizando cada cierto tiempo la ubicacion.
 *
 * @author César Ariel Rodríguez Sandoval
 * @date 2026-09
 *
 * @property activity La actividad que contiene el ViewModel.
 * @property viewModel El viewmodel que gestiona la ubicación del dispositivo.
 */

class Ubicacion(
    private val activity: ComponentActivity,
    private val viewModel: UbicacionVM
) {

    /** Cliente de proveedor de ubicación fusionada para consultar la ubicación del dispositivo. */
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(activity)

    /** Lanzador para solicitar permisos de ubicación en tiempo de ejecución. */
    private val locationPermissionLauncher: ActivityResultLauncher<Array<String>> =
        activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val fineLocation = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
            val coarseLocation = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

            if (fineLocation || coarseLocation) {
                obtenerUltimaUbicacion()
                iniciarActualizacionesUbicacion()
            } else {
                println("El usuario rechazó el permiso de ubicación")
            }
        }

    /**
     * Inicia el proceso de actualización de ubicación.
     * Verifica los permisos necesarios; si están concedidos, obtiene la última ubicación e inicia
     * las actualizaciones periódicas. De lo contrario, solicita los permisos correspondientes.
     */
    fun iniciarActualizaciones() {
        if (tienePermisoUbicacion()) {
            obtenerUltimaUbicacion()
            iniciarActualizacionesUbicacion()
        } else {
            solicitarPermisoUbicacion()
        }
    }

    /**
     * Comprueba si la aplicación tiene concedido al menos uno de los permisos de ubicación
     * ([Manifest.permission.ACCESS_FINE_LOCATION] o [Manifest.permission.ACCESS_COARSE_LOCATION]).
     *
     * @return `true` si se cuenta con al menos un permiso de ubicación, `false` en caso contrario.
     */
    private fun tienePermisoUbicacion(): Boolean {
        return ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Lanza la solicitud interactiva para que el usuario conceda los permisos de ubicación
     * precisa y aproximada.
     */
    private fun solicitarPermisoUbicacion() {
        locationPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    /**
     * Consulta la última ubicación conocida del dispositivo de forma asíncrona.
     * Si la consulta es exitosa, notifica al [viewModel] con la nueva ubicación.
     */
    private fun obtenerUltimaUbicacion() {
        if (!tienePermisoUbicacion()) {
            println("No tiene permiso de acceder a la última ubicación")
            return
        }
        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    println("Última ubicación: $it")
                    viewModel.actualizarUbicacion(it)
                }
            }
        } catch (e: SecurityException) {
            println("Error al obtener la última ubicación: ${e.message}")
        }
    }

    /**
     * Inicia la recepción periódica de actualizaciones de ubicación utilizando
     * [LocationRequest] con alta precisión.
     */
    private fun iniciarActualizacionesUbicacion() {
        if (!tienePermisoUbicacion()){
            println("No tiene permiso de actualizar ubicación")
            return
        }

        val request = LocationRequest.Builder(
             Priority.PRIORITY_HIGH_ACCURACY,
            5000L
        )
            .setMinUpdateIntervalMillis(2000L)
            .build()

        try {
            fusedLocationClient.requestLocationUpdates(
                request,
                locationCallback,
                Looper.getMainLooper()
            )
        } catch (e: SecurityException) {
            println("Error al solicitar actualizaciones de ubicación: ${e.message}")
        }
    }

    /**
     * Detiene la recepción de actualizaciones periódicas de ubicación para liberar recursos.
     */
    fun detenerActualizaciones() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    /**
     * Callback invocado por [FusedLocationProviderClient] cuando hay nuevos resultados de ubicación.
     */
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            //for (location in result.locations) {
            if (result.locations.last() != null ) {
                // println("Nueva ubicación: ${result.locations.last()}")
                viewModel.actualizarUbicacion(result.locations.last())
            }
            //}
        }
    }
}