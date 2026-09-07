package itesm.rieti.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Text


@Composable
fun registroApp(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
//        Titulo("Registro / Inicio de sesion")
        correo()
        contrasenia()
        botonRegistro()
    }

}

@Composable
//fun Titulo(texto : string, modifier: Modifier = Modifier) {


}
@Composable
fun correo(modifier: Modifier = Modifier) {
    OutlinedTextField(
        value ="",
        onValueChange = {},
        label = {Text("Correo")}
    )
}

@Composable
fun contrasenia(modifier: Modifier = Modifier) {
    OutlinedTextField(
        value ="",
        onValueChange = {},
        label = {Text("Contraseña")}
    )

}

@Composable
fun botonRegistro(modifier: Modifier = Modifier) {
    Button(
        onClick = {},
        modifier = modifier

    ) { Text("Registro") }
    
}

@Preview(showBackground = true)
@Composable
fun RegistroAppPreview() {
    registroApp()
}