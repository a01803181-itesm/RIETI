package itesm.rieti.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import itesm.rieti.view.mockupData.Reporte
import itesm.rieti.view.mockupData.ReporteMockups

@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Pantalla.RUTA_INICIO,
        modifier = modifier.fillMaxSize()
    ) {
        composable(Pantalla.RUTA_INICIO) { NuevoReporte() }
        composable(Pantalla.RUTA_SIPINNA) {  }
        composable(Pantalla.RUTA_HISTORIAL_REPORTES) {
            val mockItems: List<Reporte> = ReporteMockups().values.toList()
            HistorialActivity(mockItems)
        }
    }
}