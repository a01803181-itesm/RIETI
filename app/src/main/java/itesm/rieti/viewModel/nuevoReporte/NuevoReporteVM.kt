package itesm.rieti.viewModel.nuevoReporte

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
import java.time.LocalDateTime

class NuevoReporteVM : ViewModel()
{
    val reporteHandler = Manejador
    val validate = Validate
    val _state = MutableStateFlow(NuevoReporteState())
    val state: StateFlow<NuevoReporteState> = _state

    private fun assertFieldsCompletion(): Boolean {
        // Limpiar errores previos
        _state.value.errors.clear()

        val rawName = _state.value.rawName
        val reporte = _state.value.reporte
        var hasMissingField = false

        // 1. Validar primero si falta alguno de los campos obligatorios:
        // rawName, ubicación (latitud/longitud o dirección), fecha (día), numNiños, tipoTrabajo
        if (rawName.isBlank()) {
            addError(FormError.SurnameMissing)
            hasMissingField = true
        }
        if (reporte.numNinios == null) {
            addError(FormError.NumN)
            hasMissingField = true
        }
        if (reporte.tipoTrabajo == null) {
            addError(FormError.WorkTypeMissing)
            hasMissingField = true
        }
        if (reporte.dia == null) {
            addError(FormError.DateTimeMissing)
            hasMissingField = true
        }
        if (reporte.direccion == null && (reporte.latitud == null || reporte.longitud == null)) {
            addError(FormError.LocationMissing)
            hasMissingField = true
        }

        // Si falta alguno de los campos obligatorios, se detiene y lanza el/los error(es)
        if (hasMissingField) {
            return false
        }

        // 2. Si no falta ningún campo obligatorio, se procede a dividir y validar el nombre completo en apellidos y nombre
        var isNameValid = false
        validate.fullName(
            rawName,
            { nombre, apPaterno, apMaterno ->
                _state.value = _state.value.copy(
                    reporte = _state.value.reporte.copy(
                        nombre = nombre,
                        ap_paterno = apPaterno,
                        ap_materno = apMaterno
                    )
                )
                isNameValid = true
            },
            {
                addError(it)
                isNameValid = false
            }
        )

        return isNameValid
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

    fun setTipoTrabajoStr(texto: String) {
        val match = TipoTrabajo.entries.find { it.name.equals(texto, ignoreCase = true) }
            ?: if (texto.isNotBlank()) TipoTrabajo.OTRO else null
        setTipoTrabajo(match)
    }

    fun setDetalles(detalles: String) {
        _state.value = _state.value.copy(reporte = _state.value.reporte.copy(detalles_adicionales = detalles))
    }

    fun setUbicacion(lat: Float, lng: Float, direccion: String? = null) {
        _state.value = _state.value.copy(
            reporte = _state.value.reporte.copy(
                latitud = lat,
                longitud = lng,
                direccion = direccion ?: "Lat: $lat, Lng: $lng"
            )
        )
    }

    fun setDia(dia: LocalDateTime?) {
        _state.value = _state.value.copy(reporte = _state.value.reporte.copy(dia = dia))
    }

    fun crearReporte() {
        if (assertFieldsCompletion()) {
            viewModelScope.launch {
                val response = reporteHandler.crearReporte(_state.value.reporte)
                if (response.isSuccessful) {
                    _state.value = _state.value.copy(reporte = Reporte(), rawName = "")
                } else {
                    addError(FormError.ServerError)
                }
            }
        }
    }
}
