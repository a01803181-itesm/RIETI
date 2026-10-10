package itesm.rieti.viewModel.nuevoReporte

import android.location.Location
import androidx.lifecycle.ViewModel
import itesm.rieti.model.ubicacion.Ubicacion
import itesm.rieti.view.mainActivity.MainActivity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * ViewModel encargado de gestionar y exponer el estado de la ubicación actual del dispositivo
 * de forma reactiva utilizando Coroutine Flows.
 *
 * @author Roberto Martínez
 * @date 2026
 */
class UbicacionVM: ViewModel()
{
    /** Estado mutable interno que almacena la última ubicación obtenida. */
    private val _ubicacion = MutableStateFlow<Location?>(null)

    /** Administrador encargado de la lógica de permisos y peticiones de ubicación. */
    private lateinit var administradorUbicacion: Ubicacion

    /** Estado observable públicamente que emite los cambios de ubicación a la UI. */
    val ubicacion: StateFlow<Location?> = _ubicacion

    /**
     * Actualiza el valor actual de la ubicación con una nueva instancia de [Location].
     *
     * @param nuevaUbicacion La nueva ubicación obtenida por el proveedor de GPS.
     */
    fun actualizarUbicacion(nuevaUbicacion: Location) {
        _ubicacion.value = nuevaUbicacion
    }

    /**
     * Inicializa la instancia de [Ubicacion] vinculada a la actividad principal.
     *
     * @param activity La [MainActivity] que alojará los launchers de permisos de ubicación.
     */
    fun crearAdministradorUbicacion(activity: MainActivity) {
        administradorUbicacion = Ubicacion(activity, this)
    }

    /**
     * Inicia la gestión de permisos y las actualizaciones continuas de ubicación a través de [administradorUbicacion].
     */
    fun iniciarActualizaciones() {
        administradorUbicacion.iniciarActualizaciones()
    }

    /**
     * Detiene la recepción de actualizaciones de ubicación a través de [administradorUbicacion].
     */
    fun detenerActualizaciones() {
        administradorUbicacion.detenerActualizaciones()
    }
}