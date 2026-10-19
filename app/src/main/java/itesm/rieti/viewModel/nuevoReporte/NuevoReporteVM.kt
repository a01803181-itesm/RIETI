package itesm.rieti.viewModel.nuevoReporte

import android.content.Context
import android.location.Address
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import itesm.rieti.model.api.FormError
import itesm.rieti.model.api.GeneradorFolio
import itesm.rieti.model.api.Validate
import itesm.rieti.model.api.reportes.Manejador
import itesm.rieti.model.drafts.Borrador
import itesm.rieti.model.drafts.BorradorDB
import itesm.rieti.model.enums.RangoEdad
import itesm.rieti.model.enums.TipoTrabajo
import itesm.rieti.model.esquemas.Usuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date
import itesm.rieti.model.api.evidencias.Evidencias
import itesm.rieti.model.api.evidencias.ManejadorEvidencia


/**
 * ViewModel encargado de la lógica y la validación del formulario de 
 * creación de un nuevo reporte de incidentes.
 */
class NuevoReporteVM : ViewModel() {
    /**
     * Interfaz con el manejador de la API de reportes.
     */
    private val reporteHandler = Manejador

    /**
     * Interfaz con el manejador de la API de evidencias.
     */
    private val evidenciaHandler = ManejadorEvidencia

    /**
     * Utilidad para preparar las fotografías antes de enviarlas al servidor.
     * Se convierten las imágenes a datos multipart.
     */
    private val evidencias = Evidencias()
    
    /**
     * Utilidad para generar los folios de los reportes.
     */
    private val generadorFolios = GeneradorFolio
    
    /**
     * Utilidad de validaciones de campos del formulario.
     */
    private val validate = Validate

    /**
     * Interfaz con el DAO de borradores.
     */
    private val borradorDAO by lazy { BorradorDB.getInstance().getBorradorDAO() }
    
    private val _state = MutableStateFlow(NuevoReporteState())
    
    /**
     * Estado observable del formulario del nuevo reporte.
     */
    val state: StateFlow<NuevoReporteState> = _state

    fun setDisplayAnonymousDialog(value: Boolean) {
        _state.value = _state.value.copy(displayAnonymousDialog = value)
    }

    /**
     * Actualiza el nombre capturado de forma cruda y limpia posibles errores previos de nombre.
     *
     * @param name Nombre completo ingresado en el campo del formulario.
     */
    fun setRawName(name: String) {
        _state.value = _state.value.copy(rawName = name)
        _state.value = _state.value.copy(errors = _state.value.errors - FormError.InvalidName)
    }

    /**
     * Actualiza la cantidad de Niños, Niñas y Adolescentes (NNA) involucrados, validando 
     * su formato entero.
     *
     * @param number Texto del número capturado en el formulario.
     */
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

    /**
     * Actualiza el rango de edad percibido de los menores involucrados.
     *
     * @param rangoEdad Opción [RangoEdad] seleccionada.
     */
    fun setRangoEdad(rangoEdad: RangoEdad) {
        _state.value = _state.value.copy(reporte = _state.value.reporte.copy(rangoEdad = rangoEdad))
        val newMap = _state.value.errors - FormError.AgeRangeMissing
        _state.value = _state.value.copy(errors = newMap)
    }

    /**
     * Actualiza la fecha y la hora del reporte capturado por el usuario.
     *
     * @param datetime Cadena con formato de fecha y hora.
     */
    fun setHoraYFecha(datetime: String) {
        val dateObj = try {
            val ldt = LocalDateTime.parse(datetime)
            Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant())
        } catch (_: Exception) {
            Date()
        }
        _state.value = _state.value.copy(
            reporte = _state.value.reporte.copy(dia = datetime),
            date = dateObj,
            errors = _state.value.errors - FormError.DateTimeMissing
        )
    }

    /**
     * Guarda las coordenadas (latitud y longitud) obtenidas de la ubicación actual.
     *
     * @param latitude Valor numérico de latitud.
     * @param longitude Valor numérico de longitud.
     */
    fun setCoords(latitude: Float, longitude: Float) {
        _state.value = _state.value.copy(reporte = _state.value.reporte.copy(latitud = latitude))
        _state.value = _state.value.copy(reporte = _state.value.reporte.copy(longitud = longitude))
    }

    /**
     * Actualiza la dirección literal (calle, número) resuelta a partir de las coordenadas.
     *
     * @param address Línea de la dirección.
     */
    fun setLocation(address: String) {
        _state.value = _state.value.copy(reporte = _state.value.reporte.copy(direccion = address))
        _state.value = _state.value.copy(errors = _state.value.errors - FormError.InvalidLocation)
    }

    /**
     * Actualiza el municipio asociado al incidente reportado.
     *
     * @param municipio Nombre del municipio.
     */
    fun setMunicipio(municipio: String) {
        _state.value = _state.value.copy(reporte = _state.value.reporte.copy(municipio = municipio))
        _state.value = _state.value.copy(errors = _state.value.errors - FormError.InvalidLocation)
    }

    /**
     * Configura el tipo de trabajo reportado en el incidente.
     *
     * @param workType Clasificación del [TipoTrabajo].
     */
    fun setWorkType(workType: TipoTrabajo) {
        _state.value = _state.value.copy(reporte = _state.value.reporte.copy(tipoTrabajo = workType))
        _state.value = _state.value.copy(errors = _state.value.errors - FormError.WorkTypeMissing)
    }

    /**
     * Agrega detalles o descripciones adicionales proporcionadas por el usuario.
     *
     * @param details Información extra.
     */
    fun setDetails(details: String) {
        _state.value = _state.value.copy(reporte = _state.value.reporte.copy(detallesAdicionales = details))
    }

    /**
     * Adjunta la imagen fotográfica o evidencia a enviar en el reporte.
     *
     * @param uri [Uri] correspondiente al recurso de la imagen en el dispositivo.
     */
    fun setImageUri(uri: Uri) {
        _state.value = _state.value.copy(imageUri = uri)
    }

    fun setSendState(state: SendState) {
        _state.value = _state.value.copy(sendState = state)
    }

    fun setSaveState(state: SaveState) {
        _state.value = _state.value.copy(saveState = state)
    }

    fun setSelectedBorrador(borrador: Borrador?) {
        _state.value = _state.value.copy(selectedBorrador = borrador)
    }

    fun setDisplayConfirmDraftDeletion(value: Boolean) {
        _state.value = _state.value.copy(displayConfirmDraftDeletion = value)
    }

    fun fillFieldsWithBorrador(borrador: Borrador) {
        _state.value = _state.value.copy(errors = emptyMap())

        val parsedRangoEdad = borrador.rangoEdad?.let { str ->
            RangoEdad.entries.firstOrNull { it.name.equals(str, ignoreCase = true) || it.desc.equals(str, ignoreCase = true) }
        }
        val parsedTipoTrabajo = borrador.tipoTrabajo?.let { str ->
            TipoTrabajo.entries.firstOrNull { it.name.equals(str, ignoreCase = true) || it.desc.equals(str, ignoreCase = true) }
        }

        val isoDia = try {
            borrador.date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().toString()
        } catch (_: Exception) {
            LocalDateTime.now().toString()
        }

        _state.value = _state.value.copy(
            reporte = _state.value.reporte.copy(
                rangoEdad = parsedRangoEdad,
                tipoTrabajo = parsedTipoTrabajo,
                detallesAdicionales = borrador.detallesAdicionales,
                numNinios = borrador.numNinios,
                direccion = borrador.direccion,
                municipio = borrador.municipio,
                latitud = borrador.latitud,
                longitud = borrador.longitud,
                dia = isoDia
            ),
            rawName = borrador.rawName,
            date = borrador.date,
            imageUri = borrador.imageUri,
            rawAddress = borrador.rawAddress,
            selectedBorrador = borrador
        )
    }

    /**
     * Analiza el objeto `Address` para asegurar que el evento se reporta dentro de México, 
     * extrayendo su municipio y la dirección literal si es válido, o registrando errores en caso opuesto.
     */
    fun validateAddress() {
        val newErrors = _state.value.errors.toMutableMap()
        val rawAddress = _state.value.rawAddress
        if (rawAddress != null) {
            if (rawAddress.countryName != "Mexico") {
                newErrors[FormError.InvalidLocation] = "La dirección debe ser dentro de México"
                Log.e("Address", "Address is not in Mexico")
            } else {
                Log.i("Address", "Country: ${rawAddress.countryName}")
                if (rawAddress.subAdminArea != null) {
                    setMunicipio(rawAddress.subAdminArea)
                    newErrors.remove(FormError.InvalidLocation)
                } else {
                    Log.w("Address", "Address does not include a subAdminArea, relying on Locality")
                    if (rawAddress.locality != null) {
                        setMunicipio(rawAddress.locality)
                        newErrors.remove(FormError.InvalidLocation)
                    } else {
                        newErrors[FormError.InvalidLocation] = "La dirección no tiene un municipio asignado"
                    }
                }
                rawAddress.getAddressLine(0)?.let { setLocation(it) }
            }
        } else {
            if (_state.value.reporte.municipio.isNullOrBlank()) {
                newErrors[FormError.InvalidLocation] = "Municipio no especificado"
            } else {
                newErrors.remove(FormError.InvalidLocation)
            }
        }
        _state.value = _state.value.copy(errors = newErrors)
    }

    /**
     * Establece el objeto `Address` completo obtenido a través de los servicios de ubicación,
     * invocando su validación de forma automática.
     *
     * @param address Dirección cruda proporcionada por la geocodificación.
     */
    fun setAddress(address: Address?) {
        _state.value = _state.value.copy(rawAddress = address)
        validateAddress()
    }

    /**
     * Comprueba si todos los campos requeridos en el reporte están completos y son consistentes.
     * Actualiza el estado con los errores detectados en caso de haber omisiones o fallas lógicas.
     *
     * @return `true` si el reporte no contiene errores, `false` en caso contrario.
     */
    fun assertFieldsCompletion(): Boolean {
        val newMap = mutableMapOf<FormError, String>()
        if (_state.value.rawName != "") {
            validate.fullName(
                _state.value.rawName,
                { nombre, apPaterno, apMaterno ->
                    _state.value = _state.value.copy(reporte = _state.value.reporte.copy(nombre = nombre))
                    _state.value = _state.value.copy(reporte = _state.value.reporte.copy(apPaterno = apPaterno))
                    _state.value = _state.value.copy(reporte = _state.value.reporte.copy(apMaterno = apMaterno))
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
        if (_state.value.reporte.dia.isNullOrBlank()) {
            val nowStr = LocalDateTime.now().toString()
            _state.value = _state.value.copy(reporte = _state.value.reporte.copy(dia = nowStr))
        }
        if (_state.value.reporte.municipio.isNullOrBlank()) {
            newMap[FormError.InvalidLocation] = "Municipio no especificado"
        }
        validateAddress()
        _state.value.errors[FormError.InvalidLocation]?.let { locationError ->
            newMap[FormError.InvalidLocation] = locationError
        }
        _state.value = _state.value.copy(errors = newMap)
        Log.e("Reporte", "Errors from VM: ${_state.value.errors}")
        return newMap.isEmpty()
    }

    /**
     * Valida el formulario y en caso de éxito genera un folio único y envía 
     * el reporte al servidor a través del API.
     *
     * @param usuario El [Usuario] actual que envía el reporte, usado para relacionarlo por su correo.
     */
    fun crearReporte(context: Context, usuario: Usuario? = null) {
        setSendState(SendState.SENDING)
        if (assertFieldsCompletion()) {
            viewModelScope.launch {
                val municipio = _state.value.reporte.municipio ?: "Desconocido"
                if (_state.value.reporte.municipio == "Ciudad López Mateos" || _state.value.reporte.municipio == "Cdad. López Mateos")
                {
                    _state.value = _state.value.copy(reporte = _state.value.reporte.copy(municipio = "ATIZAPAN"))
                }
                val dia = _state.value.reporte.dia ?: LocalDateTime.now().toString()
                val folio = generadorFolios.reporte(municipio, dia)
                val correoU = if (_state.value.rawName != "" && usuario != null) {
                    usuario.correoU
                } else {
                    "anonymous@nowhere.com"
                }
                _state.value = _state.value.copy(
                    reporte = _state.value.reporte.copy(
                        folio = folio,
                        dia = dia,
                        municipio = municipio,
                        correoU = correoU
                    )
                )
                val response = reporteHandler.crearReporte(_state.value.reporte)
                Log.i("Reporte", "REPORTE: ${_state.value.reporte}")
                Log.i("Reporte", "HTTP CODE: ${response.code()}")
                Log.i("Reporte", "ERROR BODY: ${response.errorBody()?.string()}")
                if (response.isSuccessful) {
                    Log.i("Reporte", "Reporte creado exitosamente")
                    val fotoUri = _state.value.imageUri
                    if (fotoUri != null) {
                        try {
                            val foto = withContext(Dispatchers.IO) {
                                evidencias.prepararEvidencia(context, fotoUri)
                            }
                            val respuestaEvidencia = evidenciaHandler.subirEvidencia(folio, foto)

                            if(respuestaEvidencia.isSuccessful){
                                Log.i("Evidencia", "Evidencia enviada exitosamente")
                            } else {
                                Log.e("Evidencia", "Error al enviar evidencia: ${respuestaEvidencia.code()}. ${respuestaEvidencia.message()}")
                            }
                        } catch (e: Exception) {
                            Log.e("Evidencia", "Error al subir evidencia: ${e.message}")
                        }
                    }
                    val currentSelectedBorrador = _state.value.selectedBorrador
                    if (currentSelectedBorrador != null) {
                        try {
                            borradorDAO.eliminarBorrador(currentSelectedBorrador)
                        } catch (e: Exception) {
                            Log.e("Draft", "Error deleting draft after creating report: ${e.message}")
                        }
                    }
                    setSendState(if (correoU == "anonymous@nowhere.com") SendState.SUCCESS_AS_ANONYMOUS else SendState.SUCCESS)
                } else {
                    val newMap = _state.value.errors.toMutableMap()
                    newMap[FormError.ServerError] = "Error en el servidor: ${response.code()}. ${response.message()}"
                    _state.value = _state.value.copy(errors = newMap)
                    setSendState(SendState.ERROR)
                }
            }
        } else {
            setSendState(SendState.ERROR)
        }
    }

    private fun persistDraftImage(context: Context, cachedUri: Uri): String? {
        return try {
            context.contentResolver.openInputStream(cachedUri)?.use { inputStream ->
                val permanentDir = File(context.filesDir, "rieti_draft_images").apply { mkdirs() }
                val permanentFile = File(permanentDir, "draft_${System.currentTimeMillis()}.jpg")
                permanentFile.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
                permanentFile.absolutePath
            }
        } catch (e: Exception) {
            Log.e("Draft", "Error persisting draft image: ${e.message}")
            null
        }
    }

    /**
     * Mantiene los datos capturados y los guarda localmente como un borrador 
     * para reanudar el proceso en otro momento.
     */
    fun guardarComoBorrador(context: Context, userEmail: String) {
        setSaveState(SaveState.SENDING)
        viewModelScope.launch {
            try {
                val permanentImagePath = withContext(Dispatchers.IO) {
                    _state.value.imageUri?.let { persistDraftImage(context.applicationContext, it) }
                }

                val currentSelectedBorrador = _state.value.selectedBorrador
                val draftDate = _state.value.reporte.dia?.let { diaStr ->
                    try {
                        val ldt = LocalDateTime.parse(diaStr)
                        Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant())
                    } catch (_: Exception) { null }
                } ?: _state.value.date

                val draft = Borrador(
                    idBorrador = currentSelectedBorrador?.idBorrador ?: 0,
                    correoU = userEmail,
                    rangoEdad = _state.value.reporte.rangoEdad?.name,
                    detallesAdicionales = _state.value.reporte.detallesAdicionales,
                    tipoTrabajo = _state.value.reporte.tipoTrabajo?.name,
                    numNinios = _state.value.reporte.numNinios,
                    direccion = _state.value.reporte.direccion,
                    municipio = _state.value.reporte.municipio,
                    latitud = _state.value.reporte.latitud,
                    longitud = _state.value.reporte.longitud,
                    rawName = _state.value.rawName,
                    date = draftDate,
                    imageUri = _state.value.imageUri,
                    rawAddress = _state.value.rawAddress,
                    persistentImagePath = permanentImagePath ?: currentSelectedBorrador?.persistentImagePath
                )

                if (currentSelectedBorrador != null) {
                    borradorDAO.actualizarBorrador(draft)
                } else {
                    borradorDAO.insertBorrador(draft)
                }
                setSaveState(SaveState.SUCCESS)
            } catch (e: Exception) {
                Log.e("Draft", "Error saving draft: ${e.message}")
                setSaveState(SaveState.ERROR)
            }
        }
    }

    fun borrarBorrador() {
        _state.value = _state.value.copy(draftDeletionStage = DraftDeletionStage.LOADING)
        viewModelScope.launch {
            borradorDAO.eliminarBorrador(_state.value.selectedBorrador!!)
            _state.value = _state.value.copy(draftDeletionStage = DraftDeletionStage.SUCCESS)
        }
    }

    fun setDraftDeletionStage(stage: DraftDeletionStage) {
        _state.value = _state.value.copy(draftDeletionStage = stage)
    }

    /**
     * Resetea el formulario a su estado inicial por defecto.
     */
    fun resetForm() {
        _state.value = NuevoReporteState()
    }
}