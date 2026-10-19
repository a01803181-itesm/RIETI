package itesm.rieti.model.enums

import android.graphics.Color.rgb
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.gson.annotations.SerializedName

/**
 * Enumeración que representa el estado actual de un expediente.
 */
enum class Status(val desc: String, val color: Color) {
    /** Expediente recién registrado. */
    @SerializedName("1_Registrado")
    REGISTRADO("Registrado", Color(rgb(116, 253, 182))),
    /** Expediente en proceso de revisión. */
    @SerializedName("2_En_revision")
    EN_REVISION("En revisión", Color(rgb(249, 104, 104))),
    /** Expediente con seguimiento activo. */
    @SerializedName("3_En_seguimiento")
    EN_SEGUIMIENTO("En seguimiento", Color(rgb(252, 221, 97))),
    /** Expediente canalizado a las instituciones correspondientes. */
    @SerializedName("4_Canalizado")
    CANALIZADO("Canalizado", Color(rgb(134, 199, 243))),
    /** Expediente cuyo proceso ha finalizado con éxito. */
    @SerializedName("5_Concluido")
    CONCLUIDO("Concluido", Color(rgb(194, 163, 255))),
    /** Expediente almacenado y archivado. */
    @SerializedName("6_Archivado")
    ARCHIVADO("Archivado", Color(rgb(255, 197, 140))),
    @SerializedName("7_Cancelado")
    CANCELADO("Cancelado", Color(rgb(255, 62, 68))),
    /** Expediente de un NNA reincidente. */
    @SerializedName("8_Reincidente")
    REINCIDENTE("Reincidente", Color(rgb(255, 151, 227)))
}