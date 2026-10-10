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

class HistorialVM : ViewModel()
{
    private val handler = Manejador
    private val _state = MutableStateFlow(HistorialState())
    val state: StateFlow<HistorialState> = _state
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
    fun setReporteSeleccionado(reporte: Reporte?) {
        _state.value = _state.value.copy(selectedReporte = reporte)
    }
    fun setBorradorSeleccionado(borrador: Borrador?) {
        _state.value = _state.value.copy(selectedBorrador = borrador)
    }
    fun setView(view: HistoryView) {
        _state.value = _state.value.copy(selectedView = view)
    }
    fun flush() {
        _state.value = HistorialState()
    }
}