package itesm.rieti.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import itesm.rieti.model.api.reportes.Manejador
import itesm.rieti.model.esquemas.Borrador
import itesm.rieti.model.esquemas.Reporte
import itesm.rieti.view.mockupData.BorradorMockUps
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HistorialVM : ViewModel()
{
    private val _reportes = MutableStateFlow<List<Reporte>>(emptyList())
    val reportes: StateFlow<List<Reporte>> = _reportes

    private val _borradores = MutableStateFlow<List<Borrador>>(BorradorMockUps().values.toList())
    val borradores: StateFlow<List<Borrador>> = _borradores

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando

    fun cargarReportes(correoUsuario: String?) {
        if (correoUsuario.isNullOrBlank()) return

        viewModelScope.launch {
            _cargando.value = true
            try
            {
                val response = Manejador.obtenerReportesDeUsuario(correoUsuario)
                if (response.isSuccessful && response.body() != null)
                {
                    _reportes.value = response.body()!!
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _cargando.value = false
            }
        }
    }
}
