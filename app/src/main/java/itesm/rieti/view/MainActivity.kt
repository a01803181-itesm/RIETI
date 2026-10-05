package itesm.rieti.view

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
import itesm.rieti.viewModel.UbicacionVM
import itesm.rieti.viewModel.auth.AuthVM

class MainActivity : ComponentActivity()
{
    /** ViewModel para gestionar el estado de la ubicación del dispositivo. */
    private val ubicacionVM: UbicacionVM by viewModels()
    private val authVM: AuthVM by viewModels()

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
                if (!authState.loggedIn) RegistroApp() else RIETIApp()
            }
        }
    }

    /**
     * Se ejecuta al iniciar la app e inicia la solicitud de actualizaciones de ubicación.
     */
    override fun onStart()
    {
        super.onStart()
        ubicacionVM.iniciarActualizaciones()
    }

    /**
     * Se ejecuta al detener la ap y detiene las actualizaciones de ubicación para
     * no consumir recursos del dispositivo.
     */
    override fun onStop()
    {
        super.onStop()
        ubicacionVM.detenerActualizaciones()
    }
}
@Composable
fun RIETIApp(modifier: Modifier = Modifier)
{
    val navController = rememberNavController()
    Scaffold(
        content = { innerPadding ->
            AppNavHost(
                navController = navController,
                modifier = modifier.padding(innerPadding)
            )
        },
        bottomBar = { RIETIBottomBar(navController) }
    )
}

@Composable
fun RIETIBottomBar(navController: NavController, modifier: Modifier = Modifier) {
    BottomAppBar {
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

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RIETITheme {
        RIETIApp()
    }
}