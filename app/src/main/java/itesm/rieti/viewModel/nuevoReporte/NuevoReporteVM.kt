package itesm.rieti.viewModel.nuevoReporte

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import itesm.rieti.model.api.FormError
import itesm.rieti.model.api.Validate
import itesm.rieti.model.api.reportes.Manejador
import itesm.rieti.model.enums.Municipio
import itesm.rieti.model.enums.TipoTrabajo
import itesm.rieti.model.esquemas.Reporte
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NuevoReporteVM : ViewModel()
{
    val reporteHandler = Manejador
    val validate = Validate
    val _state = MutableStateFlow(NuevoReporteState())
    val state: StateFlow<NuevoReporteState> = _state
    fun hasErrors(status: Boolean) { _state.value = _state.value.copy(hasErrors = status) }
    private fun assertFieldsCompletion(): Boolean {
        _state.value.errors.clear()
        if (_state.value.rawName.isBlank()) {
            addError(FormError.SurnameMissing)
            hasErrors(true)
        }
        if (_state.value.reporte.numNinios == null) {
            addError(FormError.NumN)
            hasErrors(true)
        }
        if (_state.value.reporte.tipoTrabajo == null) {
            addError(FormError.WorkTypeMissing)
            hasErrors(true)
        }
        if (_state.value.reporte.dia == null) {
            addError(FormError.DateTimeMissing)
            hasErrors(true)
        }
        if (_state.value.reporte.direccion == null
            && (_state.value.reporte.latitud == null
                    || _state.value.reporte.longitud == null)
            ) {
            addError(FormError.LocationMissing)
            hasErrors(true)
        }

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
            },
            {
                addError(it)
                hasErrors(true)
            }
        )
        return _state.value.hasErrors
    }
    fun addError(error: FormError) {
        if (!_state.value.errors.contains(error)) {
            _state.value.errors.add(error)
        }
    }
    fun popError(error: FormError) { _state.value.errors.remove(error) }
    fun setNombreCompleto(nombre: String) {
        _state.value = _state.value.copy(rawName = nombre)
    }
    fun setNumNinos(numStr: String) {
        val num = numStr.toIntOrNull()
        _state.value = _state.value.copy(reporte = _state.value.reporte.copy(numNinios = num))
    }
    fun setEdad(edadStr: String) {
        val edad = edadStr.toIntOrNull()
        _state.value = _state.value.copy(reporte = _state.value.reporte.copy(edad = edad))
    }
    fun setTipoTrabajo(tipoTrabajo: TipoTrabajo?) {
        _state.value = _state.value.copy(reporte = _state.value.reporte.copy(tipoTrabajo = tipoTrabajo))
    }
    fun setDetalles(detalles: String) {
        _state.value = _state.value.copy(reporte = _state.value.reporte.copy(detalles_adicionales = detalles))
    }
    fun setUbicacion(lat: Float, lng: Float, direccion: String? = null) {
        _state.value = _state.value.copy(
            reporte = _state.value.reporte.copy(
                latitud = lat,
                longitud = lng,
                municipio = Municipio.NAUCALPAN,
                direccion = direccion ?: "Lat: $lat, Lng: $lng"
            )
        )
    }
    fun setDia(dia: String?) {
        _state.value = _state.value.copy(reporte = _state.value.reporte.copy(dia = dia))
    }
    fun crearReporte() {
        viewModelScope.launch {
            if (assertFieldsCompletion()) {
                _state.value = _state.value.copy(
                    reporte = _state.value.reporte.copy(
                        folio = "PRUEBA001"
                    )
                )

                val response = reporteHandler.crearReporte(_state.value.reporte)
                println("REPORTE: ${_state.value.reporte}")
                println("HTTP CODE: ${response.code()}")
                println("ERROR BODY: ${response.errorBody()?.string()}")
                println("=========================")
                if (response.isSuccessful) {
                    _state.value = _state.value.copy(reporte = Reporte(), rawName = "")
                } else {
                    addError(FormError.ServerError)
                }
            }
        }
    }
}
