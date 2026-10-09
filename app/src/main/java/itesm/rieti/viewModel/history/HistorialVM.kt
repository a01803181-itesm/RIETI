package itesm.rieti.viewModel.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import itesm.rieti.model.api.reportes.Manejador
import itesm.rieti.model.esquemas.Borrador
import itesm.rieti.model.esquemas.Reporte
import itesm.rieti.model.esquemas.Usuario
import itesm.rieti.view.mockupData.BorradorMockUps
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
                val response = handler.obtenerReportesDeUsuario(usuario)
                if (response.isSuccessful) {
                    if (response.body() != null && response.body()!!.isNotEmpty()) {
                        _state.value = _state.value.copy(reportes = response.body()!!)
                    }
                } else {
                    _state.value = _state.value.copy(errorReportes = "Error en la consulta al cargar los reportes: ${response.code()}. ${response.message()}")
                }
            } catch (e: Exception) {
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