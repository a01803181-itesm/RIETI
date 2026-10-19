package itesm.rieti.model.enums

import android.graphics.Color.rgb
import androidx.compose.ui.graphics.Color

/**
 * Enumeración que representa el estado actual de un expediente.
 */
enum class Status(val desc: String, val color: Color) {
    /** Expediente recién registrado. */
    REGISTRADO("Registrado", Color(rgb(116, 253, 182))),
    /** Expediente en proceso de revisión. */
    EN_REVISION("En revisión", Color(rgb(249, 104, 104))),
    /** Expediente con seguimiento activo. */
    EN_SEGUIMIENTO("En seguimiento", Color(rgb(252, 221, 97))),
    /** Expediente canalizado a las instituciones correspondientes. */
    CANALIZADO("Canalizado", Color(rgb(134, 199, 243))),
    /** Expediente cuyo proceso ha finalizado con éxito. */
    CONCLUIDO("Concluido", Color(rgb(194, 163, 255))),
    /** Expediente almacenado y archivado. */
    ARCHIVADO("Archivado", Color(rgb(255, 197, 140))),
    /** Expediente de un NNA reincidente. */
    REINCIDENTE("Reincidente", Color(rgb(255, 151, 227)))
}