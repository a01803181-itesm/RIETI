package itesm.rieti.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import itesm.rieti.model.esquemas.Borrador
import itesm.rieti.model.esquemas.Reporte
import itesm.rieti.view.mockupData.BorradorMockUps
import itesm.rieti.view.mockupData.ReporteMockups

@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Pantalla.RUTA_INICIO,
        modifier = modifier.fillMaxSize()
    ) {
        composable(Pantalla.RUTA_INICIO) { NuevoReporte() }
        composable(Pantalla.RUTA_SIPINNA) { AcercaDeApp() }
        composable(Pantalla.RUTA_HISTORIAL_REPORTES) {
            var mostrarHistorial by remember { mutableStateOf(true) }
            val reporteMockups: List<Reporte> = ReporteMockups().values.toList()
            val borradorMockups: List<Borrador> = BorradorMockUps().values.toList()
            if (mostrarHistorial) HistorialActivity(reporteMockups, borradorMockups, {
                mostrarHistorial = false
            }) else NuevoReporte()
        }
        composable(Pantalla.RUTA_CUENTA) {
            ConfigApp(correo= "rieti@gmail.com",
                actualizarCorreo = {},
                contrasenia= "123455",
                actualizarContrasenia = {})
        }
    }
}