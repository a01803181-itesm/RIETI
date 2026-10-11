package itesm.rieti.view.mainActivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.core.Amplify
import itesm.rieti.ui.theme.RIETITheme
import itesm.rieti.view.inicioActivity.RegistroApp
import itesm.rieti.viewModel.auth.AuthState
import itesm.rieti.viewModel.nuevoReporte.UbicacionVM
import itesm.rieti.viewModel.auth.AuthVM
import itesm.rieti.viewModel.history.HistorialVM

/**
 * Actividad principal de la aplicación.
 * Configura la inyección de dependencias, AWS Cognito y el contenido principal de Compose.
 */
class MainActivity : ComponentActivity()
{
    private val ubicacionVM: UbicacionVM by viewModels()
    private val authVM: AuthVM by viewModels()
    private val historialVM: HistorialVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        ubicacionVM.crearAdministradorUbicacion(this)

        try {
            Amplify.addPlugin(AWSCognitoAuthPlugin())
            Amplify.configure(applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        enableEdgeToEdge()
        setContent {
            val authState by authVM.authState.collectAsState()
            RIETITheme {
                if (!authState.loggedIn) RegistroApp() else RIETIApp(authVM, authState, ubicacionVM, historialVM)
            }
        }
    }

    override fun onStart()
    {
        super.onStart()
    }

    override fun onStop()
    {
        super.onStop()
        ubicacionVM.detenerActualizaciones()
    }
}
/**
 * Componente principal que define la estructura básica de la app con un Scaffold,
 * navegación y barra inferior.
 *
 * @param authVM ViewModel de autenticación.
 * @param authState Estado de la autenticación.
 * @param ubicacionVM ViewModel de ubicación.
 * @param historialVM ViewModel del historial.
 * @param modifier Modificador para la vista.
 */
@Composable
fun RIETIApp(
    authVM: AuthVM,
    authState: AuthState,
    ubicacionVM: UbicacionVM,
    historialVM: HistorialVM,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    ubicacionVM.iniciarActualizaciones()
    historialVM.cargarReportes(authState.usuario!!)
    val historialState by historialVM.state.collectAsState()

    Scaffold(
        content = { innerPadding ->
            AppNavHost(
                navController = navController,
                authVM = authVM,
                authState = authState,
                historialVM = historialVM,
                historialState = historialState,
                ubicacionVM = ubicacionVM,
                modifier = modifier.padding(innerPadding)
            )
        },
        bottomBar = { RIETIBottomBar(navController) }
    )
}

/**
 * Barra de navegación inferior (BottomAppBar) de la aplicación.
 *
 * @param navController Controlador de navegación para cambiar de pantalla.
 * @param modifier Modificador para la vista.
 */
@Composable
fun RIETIBottomBar(navController: NavController, modifier: Modifier = Modifier) {
    BottomAppBar(modifier = modifier) {
        val pilaNavegacion by navController.currentBackStackEntryAsState()
        val pantallaActual = pilaNavegacion?.destination

        Pantalla.listaPantallas.forEach { pantalla ->
            NavigationBarItem(
                selected = pantallaActual?.route == pantalla.ruta,
                onClick = {
                    navController.navigate(pantalla.ruta) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = false
                            inclusive = false
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                label = { Text(pantalla.etiqueta) },
                icon = {
                    Icon(
                        pantalla.icono,
                        pantalla.etiqueta
                    )
                },
                alwaysShowLabel = true
            )
        }
    }
}

/**
 * Vista previa de la aplicación completa de RIETI.
 *
 * @param authVM ViewModel de autenticación.
 * @param authState Estado de autenticación.
 * @param historialVM ViewModel del historial.
 * @param ubicacionVM ViewModel de ubicación.
 */
@Preview(showBackground = true)
@Composable
fun GreetingPreview(
    authVM: AuthVM = AuthVM(),
    authState: AuthState = AuthState(),
    historialVM: HistorialVM = HistorialVM(),
    ubicacionVM: UbicacionVM = UbicacionVM()
) {
    RIETITheme {
        RIETIApp(
            authVM,
            authState,
            ubicacionVM,
            historialVM,
        )
    }
}