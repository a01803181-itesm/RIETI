package itesm.rieti.viewModel.history

import itesm.rieti.model.esquemas.Borrador
import itesm.rieti.model.esquemas.Reporte

/**
 * Enumeración que define las posibles vistas disponibles en la pantalla de historial.
 *
 * @property desc Descripción visual de la vista.
 */
enum class HistoryView(val desc: String) {
    /** Vista de reportes enviados. */
    REPORTES("Reportes"),
    
    /** Vista de borradores guardados localmente. */
    BORRADORES("Borradores")
}

/**
 * Representa el estado de la pantalla de historial (reportes y borradores).
 *
 * @property reportes Lista de los [Reporte] recuperados del servidor.
 * @property selectedReporte Reporte que el usuario ha seleccionado para ver su detalle, si lo hay.
 * @property errorReportes Mensaje de error al cargar reportes, si lo hay.
 * @property borradores Lista de los [Borrador] guardados de manera local.
 * @property selectedBorrador Borrador que el usuario ha seleccionado, si lo hay.
 * @property selectedView La pestaña actual que el usuario está visualizando (Reportes o Borradores).
 * @property errorBorradores Mensaje de error relacionado a la carga de borradores, si lo hay.
 */
data class HistorialState(
    val reportes: List<Reporte> = emptyList(),
    val selectedReporte: Reporte? = null,
    val errorReportes: String? = null,
    val borradores: List<Borrador> = emptyList(),
    val selectedBorrador: Borrador? = null,
    val selectedView: HistoryView = HistoryView.REPORTES,
    val errorBorradores: String? = null
)