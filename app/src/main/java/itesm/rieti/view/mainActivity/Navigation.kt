package itesm.rieti.view.mainActivity

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import itesm.rieti.view.acercaDeActivity.AcercaDeApp
import itesm.rieti.view.configActivity.ConfigApp
import itesm.rieti.view.historialActivity.HistorialActivity
import itesm.rieti.view.nuevoReporteActivity.NuevoReporte
import itesm.rieti.viewModel.auth.AuthState
import itesm.rieti.viewModel.nuevoReporte.UbicacionVM
import itesm.rieti.viewModel.auth.AuthVM
import itesm.rieti.viewModel.history.HistorialState
import itesm.rieti.viewModel.history.HistorialVM

/**
 * Define el grafo de navegación principal de la aplicación, asociando rutas con sus
 * respectivas pantallas.
 *
 * @param navController Controlador de navegación.
 * @param authVM ViewModel de autenticación.
 * @param authState Estado de autenticación.
 * @param ubicacionVM ViewModel de ubicación.
 * @param historialVM ViewModel del historial.
 * @param historialState Estado del historial.
 * @param modifier Modificador para la vista.
 */
@Composable
fun AppNavHost(
    navController: NavHostController,
    authVM: AuthVM,
    authState: AuthState,
    ubicacionVM: UbicacionVM,
    historialVM: HistorialVM,
    historialState: HistorialState,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Pantalla.RUTA_INICIO,
        modifier = modifier.fillMaxSize()
    ) {
        composable(Pantalla.RUTA_INICIO) { NuevoReporte(authState, ubicacionVM) }
        composable(Pantalla.RUTA_SIPINNA) { AcercaDeApp() }
        composable(Pantalla.RUTA_HISTORIAL_REPORTES) { HistorialActivity(authState, historialVM, historialState) }
        composable(Pantalla.RUTA_CUENTA) { ConfigApp(authVM, historialVM) }
    }
}
