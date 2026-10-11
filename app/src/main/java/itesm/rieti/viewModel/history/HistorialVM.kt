package itesm.rieti.viewModel.history

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import itesm.rieti.model.api.reportes.Manejador
import itesm.rieti.model.esquemas.Borrador
import itesm.rieti.model.esquemas.Reporte
import itesm.rieti.model.esquemas.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel encargado de la lógica y la manipulación de datos en la sección del historial 
 * del usuario (reportes finalizados y borradores).
 */
class HistorialVM : ViewModel()
{
    private val handler = Manejador
    private val _state = MutableStateFlow(HistorialState())
    
    /**
     * Estado observable del historial (contiene la lista de reportes, borradores y opciones seleccionadas).
     */
    val state: StateFlow<HistorialState> = _state

    /**
     * Carga de manera asíncrona los reportes enviados por el usuario desde el servidor.
     *
     * @param usuario El [Usuario] activo, necesario para obtener sus reportes asociados.
     */
    fun cargarReportes(usuario: Usuario) {
        viewModelScope.launch {
            try {
                Log.i("Reportes", "Cargando reportes")
                val response = handler.obtenerReportesDeUsuario(usuario)
                if (response.isSuccessful) {
                    if (response.body() != null && response.body()!!.isNotEmpty()) {
                        Log.i("Reportes", "Reportes cargados: ${response.body()}")
                        _state.value = _state.value.copy(reportes = response.body()!!)
                    } else {
                        Log.i("Reportes", "No hay reportes")
                        _state.value = _state.value.copy(reportes = emptyList())
                    }
                } else {
                    Log.e("Reportes", "Error en la consulta al cargar los reportes: ${response.code()}. ${response.message()}")
                    _state.value = _state.value.copy(errorReportes = "Error en la consulta al cargar los reportes: ${response.code()}. ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("Reportes", "Error de conexión con el servidor: ${e.message}")
                _state.value = _state.value.copy(errorReportes = "Error de conexión con el servidor: ${e.message}")
            }
        }
    }

    /**
     * Establece el reporte seleccionado actualmente para visualizar sus detalles.
     *
     * @param reporte Objeto [Reporte] elegido o `null` para deseleccionar.
     */
    fun setReporteSeleccionado(reporte: Reporte?) {
        _state.value = _state.value.copy(selectedReporte = reporte)
    }

    /**
     * Establece el borrador seleccionado actualmente para continuar su edición o ver detalles.
     *
     * @param borrador Objeto [Borrador] elegido o `null` para deseleccionar.
     */
    fun setBorradorSeleccionado(borrador: Borrador?) {
        _state.value = _state.value.copy(selectedBorrador = borrador)
    }

    /**
     * Cambia la vista actual en la pantalla de historial (por ejemplo, entre reportes y borradores).
     *
     * @param view Nueva [HistoryView] a visualizar.
     */
    fun setView(view: HistoryView) {
        _state.value = _state.value.copy(selectedView = view)
    }

    /**
     * Limpia completamente el estado actual del historial, devolviéndolo a su forma inicial por defecto.
     */
    fun flush() {
        _state.value = HistorialState()
    }
}
