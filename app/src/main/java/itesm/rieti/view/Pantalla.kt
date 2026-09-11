package itesm.rieti.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Pantalla (
    val ruta: String,
    val etiqueta: String,
    val icono: ImageVector
) {
    companion object {
        var listaPantallas = listOf(inicio, sipinna, historialReportes, cuenta)
        const val RUTA_INICIO = "Inicio"
        const val RUTA_SIPINNA = "SIPINNA"
        const val RUTA_HISTORIAL_REPORTES = "HistorialReportes"
        const val RUTA_CUENTA = "Cuenta"
    }

    private data object inicio:
            Pantalla(RUTA_INICIO, "Inicio", Icons.Default.Home)

    private data object sipinna:
            Pantalla(RUTA_SIPINNA, "SIPINNA", Icons.Default.Info)

    private data object historialReportes:
            Pantalla(RUTA_HISTORIAL_REPORTES, "Historial", Icons.Default.History)

    private data object cuenta:
            Pantalla(RUTA_CUENTA, "Cuenta", Icons.Default.AccountCircle)
}