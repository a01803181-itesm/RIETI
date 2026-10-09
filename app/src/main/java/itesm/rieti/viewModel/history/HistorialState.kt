package itesm.rieti.viewModel.history

import itesm.rieti.model.esquemas.Borrador
import itesm.rieti.model.esquemas.Reporte

enum class HistoryView(val desc: String) {
    REPORTES("Reportes"),
    BORRADORES("Borradores")
}
data class HistorialState(
    val reportes: List<Reporte> = emptyList(),
    val selectedReporte: Reporte? = null,
    val errorReportes: String? = null,
    val borradores: List<Borrador> = emptyList(),
    val selectedBorrador: Borrador? = null,
    val selectedView: HistoryView = HistoryView.REPORTES,
    val errorBorradores: String? = null
)