package itesm.rieti.view.mainActivity

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Clase sellada que representa una pantalla en la navegación de la aplicación.
 *
 * @property ruta Ruta única que identifica la pantalla.
 * @property etiqueta Nombre legible de la pantalla.
 * @property icono Icono asociado a la pantalla.
 */
sealed class Pantalla (
    val ruta: String,
    val etiqueta: String,
    val icono: ImageVector
) {
    /**
         * Objeto que agrupa las constantes de rutas y la lista de pantallas disponibles.
         */
        companion object {
        var listaPantallas = listOf(Inicio, SIPINNA, HistorialReportes, Cuenta)
        const val RUTA_INICIO = "Inicio"
        const val RUTA_SIPINNA = "SIPINNA"
        const val RUTA_HISTORIAL_REPORTES = "HistorialReportes"
        const val RUTA_CUENTA = "Cuenta"
    }

    private data object Inicio:
            Pantalla(RUTA_INICIO, "Inicio", Icons.Default.Home)

    private data object SIPINNA:
            Pantalla(RUTA_SIPINNA, "SIPINNA", Icons.Default.Info)

    private data object HistorialReportes:
            Pantalla(RUTA_HISTORIAL_REPORTES, "Historial", Icons.Default.History)

    private data object Cuenta:
            Pantalla(RUTA_CUENTA, "Cuenta", Icons.Default.AccountCircle)
}