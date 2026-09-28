package itesm.rieti.view



import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ConfigApp(
    correo: String,
    contrasenia: String,
    actualizarContrasenia: (String) -> Unit,
    actualizarCorreo: (String) -> Unit,
    modifier: Modifier = Modifier) {
    val opciones: List<String> = listOf("Pequeña", "Mediana", "Grande")
    var seleccionado by remember { mutableIntStateOf(0) }
    Column (modifier = modifier
        .fillMaxSize()
        .padding((18.dp))) {
        Text("Configuración",  style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.ExtraBold )
        Espacio(12.dp)
        Apariencia()
        Espacio(12.dp)
        TamanioLetra(opciones, seleccionado, { seleccionado = it })
        Espacio(16.dp)
        Cuenta(correo = correo, actualizarCorreo = actualizarCorreo, contrasenia = contrasenia, actualizarContrasenia = actualizarContrasenia)

    }}


@Composable
fun Apariencia(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding((16.dp))
            .fillMaxWidth()
    )
    {
        Text("Apariencia", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Espacio(8.dp)
        Card(
            elevation = CardDefaults.cardElevation(4.dp),

            modifier = Modifier
                .padding()
                .fillMaxWidth()
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.outline,
                    shape = MaterialTheme.shapes.medium
                )
            )
         {
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Button(
                    onClick = { },
                    modifier = modifier.fillMaxWidth()
                ) {
                    Text("Claro",
                        textAlign = TextAlign.Left,
                        modifier = modifier.fillMaxWidth())
                }
                Button(
                    onClick = { },
                    modifier = modifier.fillMaxWidth()
                ) {
                    Text("Oscuro",
                        textAlign = TextAlign.Left,
                        modifier = modifier.fillMaxWidth())
                }
            }
        }
    }
}

@Composable
fun TamanioLetra(opciones: List<String>, seleccionado: Int, onTamanioLetra: (Int) -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text("Tamaño de Letra", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Espacio(8.dp)
        Card(
            elevation = CardDefaults.cardElevation(4.dp),

            modifier = Modifier
                .padding()
                .fillMaxWidth()
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.outline,
                    shape = MaterialTheme.shapes.medium
                )
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                SingleChoiceSegmentedButtonRow(modifier = modifier) {
                    opciones.forEachIndexed { numero, etiqueta ->
                        SegmentedButton(
                            selected = seleccionado == numero,
                            onClick = { onTamanioLetra(numero) },
                            shape = SegmentedButtonDefaults.itemShape(index = numero, count = opciones.size)
                        ) {
                            Text(text = etiqueta)
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun Cuenta(correo:String, actualizarCorreo: (String) -> Unit, contrasenia: String, actualizarContrasenia:(String)-> Unit, modifier: Modifier = Modifier) {
    Column() {
        Text( "Cuenta", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Espacio(8.dp)
        Card(
            elevation = CardDefaults.cardElevation(4.dp),

            modifier = Modifier
                .padding()
                .fillMaxWidth()
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.outline,
                    shape = MaterialTheme.shapes.medium
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = correo,
                        onValueChange = { actualizarCorreo(it) },
                        label = { Text("Correo Electronico", style = MaterialTheme.typography.titleMedium) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Espacio(8.dp)
                OutlinedTextField(
                    value = contrasenia,
                    onValueChange = { actualizarContrasenia(it) },
                    label = { Text("Contraseña", style = MaterialTheme.typography.titleMedium) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}





@Preview
    (showBackground = true)
@Composable
fun ConfigPreview() {
    ConfigApp(
        correo= "rieti@gmail.com",
        actualizarCorreo = {},
        contrasenia= "123455",
        actualizarContrasenia = {}
    )
}