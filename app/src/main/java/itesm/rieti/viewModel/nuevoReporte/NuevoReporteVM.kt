package itesm.rieti.viewModel.nuevoReporte

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import itesm.rieti.model.api.FormError
import itesm.rieti.model.api.Validate
import itesm.rieti.model.api.reportes.Manejador
import itesm.rieti.model.enums.TipoTrabajo
import itesm.rieti.model.esquemas.Reporte
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NuevoReporteVM : ViewModel() {
    val reporteHandler = Manejador
    val validate = Validate
    val _state = MutableStateFlow(NuevoReporteState())
    val state: StateFlow<NuevoReporteState> = _state
    private fun assertFieldsCompletion(): Boolean {
        when {
            _state.value.rawName.isNotEmpty() -> {
                var assert = false
                validate.fullName(
                    _state.value.rawName,
                    { nombre, apPaterno, apMaterno ->
                        _state.value = _state.value.copy(
                            reporte = _state.value.reporte.copy(
                                nombre = nombre,
                                ap_paterno = apPaterno,
                                ap_materno = apMaterno
                            )
                        )
                        assert = true
                    },
                    {
                        addError(it)
                        assert = false
                    }
                )
                return assert
            }
            _state.value.reporte.tipoTrabajo == null -> {
                addError(FormError.WorkTypeMissing)
                return false
            }
            _state.value.reporte.dia == null -> {
                addError(FormError.DateTimeMissing)
                return false
            }
            else -> {
                return true
            }
        }
    }
    fun addError(error: FormError) { _state.value.errors.add(error) }
    fun popError(error: FormError) { _state.value.errors.remove(error) }
    fun setNombreCompleto(nombre: String) {
        _state.value = _state.value.copy(rawName = nombre)
    }
    fun setTipoTrabajo(tipoTrabajo: TipoTrabajo) {
        _state.value = _state.value.copy(reporte = _state.value.reporte.copy(tipoTrabajo = tipoTrabajo))
    }
    fun crearReporte() {
        if (assertFieldsCompletion()) {
            viewModelScope.launch {
                val response = reporteHandler.crearReporte(_state.value.reporte)
                if (response.isSuccessful) {
                    _state.value = _state.value.copy(reporte = Reporte())
                } else {
                    addError(FormError.ServerError)
                }
            }
        }
    }
}