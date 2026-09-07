package itesm.rieti.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun NuevoReporte(modifier: Modifier = Modifier)
{
    val ancho = 180.dp
    val altura = 90.dp
    val pad = 16.dp
    val tamLetra = 20.sp

    var error by remember { mutableStateOf(false) }
    var ubicacion by remember { mutableStateOf<LatLng?>(null) }

    // Columna principal
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(pad)
    ) {
        // Nombre
        Row(modifier = Modifier.fillMaxWidth())
        {
            var nombre by remember { mutableStateOf(value = "") }

            OutlinedTextField(
                value = nombre,
                onValueChange = {
                    try {
                        nombre = it
                        error = false
                    } catch (e: NumberFormatException) {
                        error = true
                    }
                },
                label = { Text("Nombre Completo",
                    style = MaterialTheme.typography.titleMedium) },
                textStyle = TextStyle(fontSize = tamLetra, fontWeight = FontWeight.ExtraBold),
                isError = error,
                supportingText = {
                    if (error)
                    {
                        Text("Campo Incorrecto")
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = modifier.fillMaxWidth()
            )
        }

        // Cantidad y edad ni;os
        Row(horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth())
        {
            var numNinos by remember { mutableStateOf(value = "") }
            var edadNinos by remember { mutableStateOf(value = "") }

            // # ni;os
            OutlinedTextField(
                value = numNinos,
                onValueChange = {
                    try {
                        numNinos = it
                        error = false
                    } catch (e: NumberFormatException) {
                        error = true
                    }
                },
                label = { Text("Cantidad Niños",
                    style = MaterialTheme.typography.titleMedium) },
                textStyle = TextStyle(fontSize = tamLetra, fontWeight = FontWeight.ExtraBold),
                isError = error,
                supportingText = {
                    if (error)
                    {
                        Text("Campo Incorrecto")
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = modifier
                    .width(ancho)
                    .height(altura)
                    .padding(end = pad)
            )

            // edad
            OutlinedTextField(
                value = edadNinos,
                onValueChange = {
                    try {
                        edadNinos = it
                        error = false
                    } catch (e: NumberFormatException) {
                        error = true
                    }
                },
                label = { Text("Edad de los Niños",
                    style = MaterialTheme.typography.titleMedium) },
                textStyle = TextStyle(fontSize = tamLetra, fontWeight = FontWeight.ExtraBold),
                isError = error,
                supportingText = {
                    if (error)
                    {
                        Text("Campo Incorrecto")
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = modifier
                    .width(ancho)
                    .height(altura)
                    .padding(start = pad)
            )
        }

        // tipo de tabajo y horario
        Row(horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth())
        {
            var numNinos by remember { mutableStateOf(value = "") }
            var edadNinos by remember { mutableStateOf(value = "") }

            // tipo trabajo
            OutlinedTextField(
                value = numNinos,
                onValueChange = {
                    try {
                        numNinos = it
                        error = false
                    } catch (e: NumberFormatException) {
                        error = true
                    }
                },
                label = { Text("Tipo Trabajo",
                    style = MaterialTheme.typography.titleMedium) },
                textStyle = TextStyle(fontSize = tamLetra, fontWeight = FontWeight.ExtraBold),
                isError = error,
                supportingText = {
                    if (error)
                    {
                        Text("Campo Incorrecto")
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = modifier
                    .width(ancho)
                    .height(altura)
                    .padding(end = pad)
            )

            // horario
            OutlinedTextField(
                value = edadNinos,
                onValueChange = {
                    try {
                        edadNinos = it
                        error = false
                    } catch (e: NumberFormatException) {
                        error = true
                    }
                },
                label = { Text("Horario",
                    style = MaterialTheme.typography.titleMedium) },
                textStyle = TextStyle(fontSize = tamLetra, fontWeight = FontWeight.ExtraBold),
                isError = error,
                supportingText = {
                    if (error)
                    {
                        Text("Campo Incorrecto")
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = modifier
                    .width(ancho)
                    .height(altura)
                    .padding(start = pad)
            )
        }

        // detalles
        Row(modifier = Modifier.fillMaxWidth())
        {
            var detalles by remember { mutableStateOf(value = "") }

            OutlinedTextField(
                value = detalles,
                onValueChange = {
                    try {
                        detalles = it
                        error = false
                    } catch (e: NumberFormatException) {
                        error = true
                    }
                },
                label = { Text("Detalles",
                    style = MaterialTheme.typography.titleMedium) },
                textStyle = TextStyle(fontSize = tamLetra, fontWeight = FontWeight.ExtraBold),
                isError = error,
                supportingText = {
                    if (error)
                    {
                        Text("Campo Incorrecto")
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = modifier
                    .fillMaxWidth()
                    .height(altura*2)
            )
        }

        // fotos
        Row(modifier = Modifier.fillMaxWidth())
        {
            var foto by remember { mutableStateOf(value = "") }

            OutlinedTextField(
                value = foto,
                onValueChange = {
                    try {
                        foto = it
                        error = false
                    } catch (e: NumberFormatException) {
                        error = true
                    }
                },
                label = { Text("Foto",
                    style = MaterialTheme.typography.titleMedium) },
                textStyle = TextStyle(fontSize = tamLetra, fontWeight = FontWeight.ExtraBold),
                isError = error,
                supportingText = {
                    if (error)
                    {
                        Text("Campo Incorrecto")
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = modifier
                    .fillMaxWidth()
                    .height(altura*2)
            )
        }

        // ubicacion
        Row(modifier = Modifier.fillMaxWidth())
        {
            Ubicacion(
                modifier = Modifier.fillMaxWidth(),
                onUbicacionSeleccionada = { latLng ->
                    ubicacion = latLng
                }
            )
        }
    }
}

@Composable
fun Ubicacion(
    modifier: Modifier = Modifier,
    posicionInicial: LatLng = LatLng(19.4326, -99.1332), // CDMX
    onUbicacionSeleccionada: (LatLng) -> Unit
)
{
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(posicionInicial, 16f)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false,
                myLocationButtonEnabled = false
            )
        )

        // Pin fijo en el centro
        Icon(
            imageVector = Icons.Filled.LocationOn,
            contentDescription = "Marcador de ubicación",
            tint = Color.Red,
            modifier = Modifier
                .align(Alignment.Center)
                .size(48.dp)
                .padding(bottom = 24.dp) // ajusta esto para que la PUNTA del pin quede exacto en el centro
        )
    }

    // Se dispara cada vez que el usuario suelta el mapa
    LaunchedEffect(cameraPositionState.isMoving)
    {
        if (!cameraPositionState.isMoving)
        {
            onUbicacionSeleccionada(cameraPositionState.position.target)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReportePreview()
{
    NuevoReporte()
}