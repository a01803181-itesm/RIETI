package itesm.rieti.viewModel.nuevoReporte

import android.net.Uri
import android.util.Log
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.rememberDatePickerState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import itesm.rieti.model.api.FormError
import itesm.rieti.model.api.GeneradorFolio
import itesm.rieti.model.api.Validate
import itesm.rieti.model.api.reportes.Manejador
import itesm.rieti.model.enums.RangoEdad
import itesm.rieti.model.enums.TipoTrabajo
import itesm.rieti.model.esquemas.Reporte
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NuevoReporteVM : ViewModel()
{
    val reporteHandler = Manejador
    val generadorFolios = GeneradorFolio
    val validate = Validate
    val _state = MutableStateFlow(NuevoReporteState())
    val state: StateFlow<NuevoReporteState> = _state
    fun setRawName(name: String) {
        _state.value = _state.value.copy(rawName = name)
        _state.value = _state.value.copy(errors = _state.value.errors - FormError.InvalidName)
    }
    fun setNNAs(number: String) {
        try {
            _state.value = _state.value.copy(reporte = _state.value.reporte.copy(numNinios = number.toInt()))
            _state.value = _state.value.copy(errors = _state.value.errors - FormError.NNAMissing)
        } catch (_: NumberFormatException) {
            val newMap = _state.value.errors.toMutableMap()
            newMap[FormError.NNAMissing] = "El campo debe ser un número entero positivo"
            _state.value = _state.value.copy(errors = newMap)
        }
    }
    fun setRangoEdad(rangoEdad: RangoEdad) {
        _state.value = _state.value.copy(reporte = _state.value.reporte.copy(rangoEdad = rangoEdad))
        val newMap = _state.value.errors - FormError.AgeRangeMissing
        _state.value = _state.value.copy(errors = newMap)
    }
    fun setHoraYFecha(datetime: String) {
        _state.value = _state.value.copy(reporte = _state.value.reporte.copy(dia = datetime))
        _state.value = _state.value.copy(errors = _state.value.errors - FormError.DateTimeMissing)
    }
    fun setCoords(latitude: Float, Longitude: Float) {
        _state.value = _state.value.copy(reporte = _state.value.reporte.copy(latitud = latitude))
        _state.value = _state.value.copy(reporte = _state.value.reporte.copy(longitud = Longitude))
    }
    fun setLocation(address: String) {
        _state.value = _state.value.copy(reporte = _state.value.reporte.copy(direccion = address))
        _state.value = _state.value.copy(errors = _state.value.errors - FormError.AddressMissing)
    }
    fun setWorkType(workType: TipoTrabajo) {
        _state.value = _state.value.copy(reporte = _state.value.reporte.copy(tipoTrabajo = workType))
        _state.value = _state.value.copy(errors = _state.value.errors - FormError.WorkTypeMissing)
    }
    fun setDetails(details: String) {
        _state.value = _state.value.copy(reporte = _state.value.reporte.copy(detalles_adicionales = details))
    }
    fun setImageUri(uri: Uri) {
        _state.value = _state.value.copy(imageUri = uri)
    }
    fun assertFieldsCompletion(): Boolean {
        val newMap = mutableMapOf<FormError,String>()
        if (_state.value.rawName != "") {
            validate.fullName(
                _state.value.rawName,
                { nombre, apPaterno, apMaterno ->
                    _state.value = _state.value.copy(reporte = _state.value.reporte.copy(nombre = nombre))
                    _state.value = _state.value.copy(reporte = _state.value.reporte.copy(ap_paterno = apPaterno))
                    _state.value = _state.value.copy(reporte = _state.value.reporte.copy(ap_materno = apMaterno))
                },
                { newMap[FormError.InvalidName] = it }
            )
        }
        if (_state.value.reporte.numNinios < 1) {
            newMap[FormError.NNAMissing] = "Número de NNAs debe ser mayor a 0"
        }
        if (_state.value.reporte.tipoTrabajo == null) {
            newMap[FormError.WorkTypeMissing] = "Tipo de trabajo no especificado"
        }
        if (_state.value.reporte.rangoEdad == null) {
            newMap[FormError.AgeRangeMissing] = "Rango de edad no especificado"
        }
        if (_state.value.reporte.municipio == null) {
            newMap[FormError.LocationMissing] = "Municipio no especificado"
        }
        _state.value = _state.value.copy(errors = newMap)
        return newMap.isEmpty()
    }
    fun crearReporte() {
        if (assertFieldsCompletion()) {
            viewModelScope.launch {
                val folio = generadorFolios.reporte(
                    _state.value.reporte.municipio!!,
                    _state.value.reporte.dia!!.split("T")[0]
                )
                _state.value = _state.value.copy(
                    reporte = _state.value.reporte.copy(
                        folio = folio
                    )
                )

                val response = reporteHandler.crearReporte(_state.value.reporte)
                Log.i("Reporte", "REPORTE: ${_state.value.reporte}")
                Log.i("Reporte", "HTTP CODE: ${response.code()}")
                Log.i("Reporte", "ERROR BODY: ${response.errorBody()?.string()}")
                if (response.isSuccessful) {
                    _state.value = _state.value.copy(reporte = Reporte(), rawName = "")
                } else {
                    val newMap = _state.value.errors.toMutableMap()
                    newMap[FormError.ServerError] = "Error en el servidor: ${response.code()}. ${response.message()}"
                    _state.value = _state.value.copy(errors = newMap)
                }
            }
        }
    }
    fun guardarComoBorrador() {
        // TODO: Guardar como borrador
    }
}
