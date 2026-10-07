package itesm.rieti.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import itesm.rieti.view.historialActivity.HistorialActivity
import itesm.rieti.viewModel.auth.AuthVM

@Composable
fun AppNavHost(
    navController: NavHostController,
    authVM: AuthVM = viewModel(),
    modifier: Modifier = Modifier
) {
    val authState by authVM.authState.collectAsState()
    val correoUsuario: String? = authState.usuario?.correoU

    NavHost(
        navController = navController,
        startDestination = Pantalla.RUTA_INICIO,
        modifier = modifier.fillMaxSize()
    )
    {
        composable(Pantalla.RUTA_INICIO) { NuevoReporte() }
        composable(Pantalla.RUTA_SIPINNA) { AcercaDeApp() }
        composable(Pantalla.RUTA_HISTORIAL_REPORTES) {
            var mostrarHistorial by remember { mutableStateOf(true) }

            if (mostrarHistorial)
            {
                HistorialActivity(
                    correoUsuario = correoUsuario,
                    onEditar = { mostrarHistorial = false }
                )
            } else {
                NuevoReporte()
            }
        }
        composable(Pantalla.RUTA_CUENTA) { ConfigApp() }
    }
}
