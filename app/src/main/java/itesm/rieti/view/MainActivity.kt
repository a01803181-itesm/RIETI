package itesm.rieti.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.twotone.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import itesm.rieti.ui.theme.RIETITheme
import itesm.rieti.viewModel.MainVM

class MainActivity : ComponentActivity()
{
    private val vm: MainVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RIETITheme {
                RIETIApp(vm)
            }
        }
    }
}

@Composable
fun RIETIApp(rietivm: MainVM, modifier: Modifier = Modifier)
{
    val estado by rietivm.estado.collectAsState() // Suscripcion al estado

    Scaffold(
        topBar = { PPTAppBar() },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Etiqueta("Jugador")
                    Marcador(estado.puntosJugador)
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Etiqueta("Android")
                    Marcador(estado.puntosAndroid)
                }
            }
            // Botones

            Row (
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                BotonElemento(Elemento.Piedra,
                    onClick = {
                        pptvm.actualizarElementoJugador(Elemento.Piedra)
                        pptvm.jugar()
                    }
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth()
            ) {
                BotonElemento(Elemento.Papel,
                    onClick = {
                        pptvm.actualizarElementoJugador(Elemento.Papel)
                        pptvm.jugar()
                    }
                )
                BotonElemento(Elemento.Tijeras,
                    onClick = {
                        pptvm.actualizarElementoJugador(Elemento.Tijeras)
                        pptvm.jugar()
                    }
                )
            }
        }
        // Resultado del juego individual
        if (estado.resultadoJuego != null)
        {
            val resultado = "${estado.elementoJugador} vs ${estado.elementoAndroid} -> ${estado.resultadoJuego}"
            Toast.makeText(LocalContext.current, resultado, Toast.LENGTH_SHORT).show()
        }

        // Resultado de la partida
        if (estado.resultadoPartida == GanadorPartida.Jugador ||
            estado.resultadoPartida == GanadorPartida.Android)
        {
            ResultadoPartida(estado.resultadoPartida)
            {
                pptvm.reset()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RIETITheme {
        Greeting("Android")
    }
}