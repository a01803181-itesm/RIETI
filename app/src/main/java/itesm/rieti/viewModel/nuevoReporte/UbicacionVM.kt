package itesm.rieti.viewModel.nuevoReporte

import android.location.Location
import androidx.lifecycle.ViewModel
import itesm.rieti.model.ubicacion.Ubicacion
import itesm.rieti.view.mainActivity.MainActivity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * ViewModel que actúa como enlace para manejar y distribuir 
 * la ubicación en tiempo real del dispositivo en la interfaz.
 */
class UbicacionVM: ViewModel()
{
    private val _ubicacion = MutableStateFlow<Location?>(null)
    private lateinit var administradorUbicacion: Ubicacion
    
    /**
     * Estado observable con la ubicación actual obtenida por el dispositivo.
     */
    val ubicacion: StateFlow<Location?> = _ubicacion

    /**
     * Reemplaza la ubicación alojada en el estado por un nuevo valor de coordenadas.
     *
     * @param nuevaUbicacion Objeto [Location] proveído por los servicios de ubicación del sistema.
     */
    fun actualizarUbicacion(nuevaUbicacion: Location) {
        _ubicacion.value = nuevaUbicacion
    }

    /**
     * Instancia el objeto encargado de interactuar directamente con la API de ubicación del sistema,
     * relacionándolo con la Actividad actual y este ViewModel.
     *
     * @param activity Contexto principal (generalmente [MainActivity]) para gestionar permisos e instancias del sistema.
     */
    fun crearAdministradorUbicacion(activity: MainActivity) {
        administradorUbicacion = Ubicacion(activity, this)
    }

    /**
     * Solicita al administrador iniciar las peticiones constantes de actualizaciones de ubicación 
     * para rastrear el desplazamiento del dispositivo.
     */
    fun iniciarActualizaciones() {
        administradorUbicacion.iniciarActualizaciones()
    }

    /**
     * Instruye al administrador detener las solicitudes de actualizaciones de ubicación 
     * para ahorrar batería e interrumpir rastreos inactivos.
     */
    fun detenerActualizaciones() {
        administradorUbicacion.detenerActualizaciones()
    }
}