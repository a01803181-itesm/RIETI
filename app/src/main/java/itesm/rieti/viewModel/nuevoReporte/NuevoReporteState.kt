package itesm.rieti.viewModel.nuevoReporte

import android.location.Address
import android.net.Uri
import itesm.rieti.model.api.FormError
import itesm.rieti.model.esquemas.Reporte
import java.util.Date

data class NuevoReporteState(
    val reporte: Reporte = Reporte(),
    val rawName: String = "",
    val date: Date = Date(),
    val imageUri: Uri? = null,
    val errors: Map<FormError,String> = emptyMap(),
    val rawAddress: Address? = null
)