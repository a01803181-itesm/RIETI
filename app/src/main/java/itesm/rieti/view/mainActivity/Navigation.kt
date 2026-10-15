package itesm.rieti.view.mainActivity

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.room.RoomDatabase.Builder
import itesm.rieti.model.drafts.BorradorDB
import itesm.rieti.view.acercaDeActivity.AcercaDeApp
import itesm.rieti.view.configActivity.ConfigApp
import itesm.rieti.view.historialActivity.HistorialActivity
import itesm.rieti.view.nuevoReporteActivity.NuevoReporte
import itesm.rieti.viewModel.auth.AuthState
import itesm.rieti.viewModel.nuevoReporte.UbicacionVM
import itesm.rieti.viewModel.auth.AuthVM
import itesm.rieti.viewModel.history.HistorialState
import itesm.rieti.viewModel.history.HistorialVM
import itesm.rieti.viewModel.history.HistoryView

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
        composable(Pantalla.RUTA_INICIO) {
            NuevoReporte(
                authState = authState,
                ubicacionVM = ubicacionVM,
                onReporteCreado = {
                    historialVM.setView(HistoryView.REPORTES)
                    authState.usuario?.let { historialVM.cargarReportes(it) }
                    navController.navigate(Pantalla.RUTA_HISTORIAL_REPORTES) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = false
                            inclusive = false
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onBorradorGuardado = {
                    historialVM.setView(HistoryView.BORRADORES)
                    authState.usuario?.let { historialVM.cargarBorradores(it) }
                    navController.navigate(Pantalla.RUTA_HISTORIAL_REPORTES) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = false
                            inclusive = false
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
        composable(Pantalla.RUTA_SIPINNA) { AcercaDeApp() }
        composable(Pantalla.RUTA_HISTORIAL_REPORTES) { HistorialActivity(authState, historialVM, historialState) }
        composable(Pantalla.RUTA_CUENTA) { ConfigApp(authVM, historialVM) }
    }
}
