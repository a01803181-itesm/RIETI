package itesm.rieti.viewModel.nuevoReporte

import android.location.Address
import android.net.Uri
import itesm.rieti.model.api.FormError
import itesm.rieti.model.esquemas.Reporte
import java.util.Date

/**
 * Representa el estado del formulario de creación de un nuevo reporte.
 *
 * @property reporte Entidad [Reporte] que está siendo conformada por el usuario.
 * @property rawName Texto en crudo con el nombre ingresado, previo a su procesamiento de validación.
 * @property date Fecha de inicio/creación de la captura del reporte.
 * @property imageUri URI de la imagen seleccionada como evidencia en el reporte.
 * @property errors Mapa con los diferentes [FormError] identificados en los campos del formulario.
 * @property rawAddress Objeto [Address] con la información de ubicación sin procesar aún.
 */
data class NuevoReporteState(
    val reporte: Reporte = Reporte(),
    val rawName: String = "",
    val date: Date = Date(),
    val imageUri: Uri? = null,
    val errors: Map<FormError,String> = emptyMap(),
    val rawAddress: Address? = null
)