package itesm.rieti.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController

@Composable
fun acercaDeApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    Scaffold(
        modifier = modifier,
        bottomBar = { RIETIBottomBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            encabezado()
            espacio(24.dp)
            cuerpoAcercaDeApp()
        }
    }
}

@Composable
fun cuerpoAcercaDeApp(modifier: Modifier = Modifier) {

        Text(
            text = "La SIPINNA es el Sistema Nacional de Protección Integral de Niñas, Niños y Adolescentes, fue mandatado por la Ley General de Derechos de Niñas, Niños y Adolescentes de 2014 a efecto de que el Estado (en sus tres órdenes de gobierno) cumpla con su responsabilidad de garantizar la protección, prevención y restitución integrales de los derechos humanos de niñas, niños y adolescentes que hayan sido vulnerados.\n" +
                    "\n" +
                    "REITI es una forma de estrategia colaborativa de  diversos municipios mexiquenses para conjuntar esfuerzos institucionales para la detección y registro de casos de trabajo infantil y proteger los derechos de la niñez.\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "Esta aplicación se hizo por alumnos del Tecnologico de Monterrey",
            fontSize = 15.sp,
            lineHeight = 22.sp,
            textAlign = TextAlign.Justify,
            color = Color.DarkGray,
        )



}

@Preview(showBackground = true)
@Composable
fun Preview() {
    acercaDeApp()
}

