package itesm.rieti.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AcercaDeApp(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Encabezado()
        Espacio(24.dp)
        CuerpoAcercaDeApp()
    }
}

@Composable
fun CuerpoAcercaDeApp(modifier: Modifier = Modifier) {

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
                    "Esta aplicación se hizo por alumnos del Tecnológico de Monterrey",
            fontSize = 15.sp,
            lineHeight = 22.sp,
            textAlign = TextAlign.Justify,
            fontWeight = FontWeight.Light
        )
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    AcercaDeApp()
}

