package itesm.rieti.viewModel.nuevoReporte

import itesm.rieti.model.api.FormError
import itesm.rieti.model.esquemas.Reporte

data class NuevoReporteState(
    val reporte: Reporte = Reporte(),
    val rawName: String = "",
    val errors: MutableList<FormError> = mutableListOf()
)