package itesm.rieti.view

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


//Contenedor principal
@Composable
fun registroApp(modifier: Modifier = Modifier) {
    Column(

        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFE6F0FA))

    ) {
        header()
        mainApp()
    }

}

//Contenedor del cuerpo
@Composable
fun mainApp(modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(16.dp)
    ) {
        titulosLogin()
        espacio(24.dp)
        correo()
        espacio(24.dp)
        contrasenia()
        espacio(24.dp)
        botonRegistro()
        espacio(24.dp)
        botonGoogle()
    }

}

//Header
@Composable
fun header(modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)

    ) {
        Text(
            text = "Rieti",
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp
        )
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "Perfil",
            modifier = modifier.size(32.dp)
        )
    }

}

@Composable
fun titulosLogin(modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = "Iniciar sesion",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Ingresa tus datos para continuar",
            fontSize = 16.sp,
            color = Color.Gray

        )
    }
}

@Composable
fun espacio(distancia: Dp, modifier: Modifier = Modifier) {
    Spacer(
        modifier = modifier.height(distancia)
    )
}

// @Composable
//fun Titulo(texto : string, modifier: Modifier = Modifier) { }
@Composable
fun correo(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "Correo electrónico",
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        )
        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("ejemplo@correo.com", color = Color.Gray) },
            modifier = modifier.fillMaxWidth()
        )
    }
}


@Composable
fun contrasenia(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "Contraseña",
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        )
        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("••••••••", color = Color.Gray) },
            modifier = modifier.fillMaxWidth()
        )
    }


}

@Composable
fun botonRegistro(modifier: Modifier = Modifier) {
    Button(
        onClick = {},
        modifier = modifier.fillMaxWidth()

    ) { Text("Registro") }

}

@Composable
fun botonGoogle(modifier: Modifier = Modifier) {
    Button(
        onClick = {},
        modifier = modifier.fillMaxWidth()
    ) { Text("Continuar con Google") }

}

@Preview(showBackground = true)
@Composable
fun mainAppPreview() {
    registroApp()
}