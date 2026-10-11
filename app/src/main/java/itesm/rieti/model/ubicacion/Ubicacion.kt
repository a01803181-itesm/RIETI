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
import itesm.rieti.viewModel.nuevoReporte.UbicacionVM

/**
 * Clase que encapsula la lógica de obtención y monitoreo de la ubicación geográfica del usuario.
 *
 * @param activity La actividad principal desde donde se requieren los permisos y los servicios de ubicación.
 * @param viewModel El ViewModel asociado donde se actualizarán los datos de ubicación.
 */
class Ubicacion(
    private val activity: ComponentActivity,
    private val viewModel: UbicacionVM
) {
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(activity)
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
     * Inicia la recolección de actualizaciones de ubicación si se cuenta con permisos. 
     * Si no, solicita los permisos al usuario.
     */
    fun iniciarActualizaciones() {
        if (tienePermisoUbicacion()) {
            obtenerUltimaUbicacion()
            iniciarActualizacionesUbicacion()
        } else {
            solicitarPermisoUbicacion()
        }
    }
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
    private fun solicitarPermisoUbicacion() {
        locationPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }
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
     * Detiene las actualizaciones continuas de ubicación, liberando así los recursos del sistema.
     */
    fun detenerActualizaciones() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
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