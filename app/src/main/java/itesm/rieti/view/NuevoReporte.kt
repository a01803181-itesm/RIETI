package itesm.rieti.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraMoveStartedReason
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

@Composable
fun NuevoReporte(modifier: Modifier = Modifier) {
    val altura = 90.dp
    val pad = 16.dp
    val tamLetra = 20.sp

    val estadoScroll = rememberScrollState()

    var error by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(pad)
    )
    {
        Text(
            text = "Nuevo Reporte",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(pad))
        
        // Columna principal
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(estadoScroll),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        )
        {
            // Nombre
            Row(modifier = Modifier.fillMaxWidth())
            {
                var nombre by remember { mutableStateOf("") }

                OutlinedTextField(
                    value = nombre,
                    onValueChange = {
                        nombre = it
                    },
                    label = {
                        Text(
                            "Nombre Completo",
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    textStyle = TextStyle(fontSize = tamLetra, fontWeight = FontWeight.ExtraBold),
                    isError = error,
                    shape = RoundedCornerShape(12.dp),
                    supportingText = {
                        if (error) {
                            Text("Campo Incorrecto")
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Cantidad y edad ni;os
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            )
            {
                var numNinos by remember { mutableStateOf("") }
                var edadNinos by remember { mutableStateOf("") }

                // # ni;os
                OutlinedTextField(
                    value = numNinos,
                    onValueChange = {
                        numNinos = it
                    },
                    label = {
                        Text(
                            "Cantidad Niños",
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    textStyle = TextStyle(fontSize = tamLetra, fontWeight = FontWeight.ExtraBold),
                    isError = error,
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .weight(1f)
                        .height(altura)
                )

                // edad
                OutlinedTextField(
                    value = edadNinos,
                    onValueChange = {
                        edadNinos = it
                    },
                    label = {
                        Text(
                            "Edad de los Niños",
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    textStyle = TextStyle(fontSize = tamLetra, fontWeight = FontWeight.ExtraBold),
                    isError = error,
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .weight(1f)
                        .height(altura)
                )
            }

            // Tipo de trabajo y horario
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            )
            {
                var tipoTrabajo by remember { mutableStateOf("") }
                var horario by remember { mutableStateOf("") }

                // Tipo trabajo
                OutlinedTextField(
                    value = tipoTrabajo,
                    onValueChange = {
                        tipoTrabajo = it
                    },
                    label = {
                        Text(
                            "Tipo Trabajo",
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    textStyle = TextStyle(fontSize = tamLetra, fontWeight = FontWeight.ExtraBold),
                    isError = error,
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    modifier = Modifier
                        .weight(1f)
                        .height(altura)
                )

                // Horario
                OutlinedTextField(
                    value = horario,
                    onValueChange = {
                        horario = it
                    },
                    label = {
                        Text(
                            "Horario",
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    textStyle = TextStyle(fontSize = tamLetra, fontWeight = FontWeight.ExtraBold),
                    isError = error,
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    modifier = Modifier
                        .weight(1f)
                        .height(altura)
                )
            }

            // Detalles
            Row(modifier = Modifier.fillMaxWidth())
            {
                var detalles by remember { mutableStateOf("") }

                OutlinedTextField(
                    value = detalles,
                    onValueChange = {
                        detalles = it
                    },
                    label = {
                        Text(
                            "Detalles",
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    textStyle = TextStyle(fontSize = tamLetra, fontWeight = FontWeight.ExtraBold),
                    isError = error,
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(altura * 2)
                )
            }

            // Foto
            Row(modifier = Modifier.fillMaxWidth())
            {
                var foto by remember { mutableStateOf("") }

                OutlinedTextField(
                    value = foto,
                    onValueChange = {
                        foto = it
                    },
                    label = {
                        Text(
                            "Foto",
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    textStyle = TextStyle(fontSize = tamLetra, fontWeight = FontWeight.ExtraBold),
                    isError = error,
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(altura)
                )
            }

            // Ubicación / Mapa
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            )
            {
                MapScreen(modifier = Modifier.fillMaxSize())
            }

            // Guardar borrador o mandar reporte
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
            {
                Button(onClick = { }) { Text("Guardar Borrador") }
                Button(onClick = {

                }) { Text("Enviar Reporte") }
            }
        }
    }
}

@Composable
fun MapScreen(modifier: Modifier = Modifier)
{
    val ubicacionInicial = LatLng(19.553207953517877, -99.28483799099922)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(ubicacionInicial, 15f)
    }

    val coroutineScope = rememberCoroutineScope()

    // Escuchar cuando la cámara deje de moverse (Equivalente a OnCameraIdle / dragend)
    LaunchedEffect(cameraPositionState.isMoving)
    {
        if (!cameraPositionState.isMoving)
        {
            // Validar que el movimiento fue por un gesto del usuario
            if (cameraPositionState.cameraMoveStartedReason == CameraMoveStartedReason.GESTURE)
            {
                val centroActual = cameraPositionState.position.target
                val zoomActual = cameraPositionState.position.zoom

                println("El usuario movió el mapa a: ${centroActual.latitude}, ${centroActual.longitude}")
                // SOLICITAR LA DIRECCIÓN EN TEXTO PARA ACTUALIZARLA
                println("Zoom actual: $zoomActual")
            }
        }
    }

    Box (modifier = modifier.fillMaxSize())
    {
        GoogleMap(
            modifier = Modifier
                .fillMaxWidth(),
            cameraPositionState = cameraPositionState,
            onMapClick = { latLng ->
                println("Click en el mapa: ${latLng.latitude}, ${latLng.longitude}")
                // Mover la cámara de forma asíncrona hacia las nuevas coordenadas
                coroutineScope.launch {
                    cameraPositionState.animate(
                        update = CameraUpdateFactory.newLatLng(latLng),
                        durationMs = 1000
                    )
                }
            }
        )
        // Pin
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = "Centro del mapa",
            tint = Color.Black,
            modifier = modifier
                .size(16.dp)
                .align(Alignment.Center)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ReportePreview()
{
    NuevoReporte()
}
