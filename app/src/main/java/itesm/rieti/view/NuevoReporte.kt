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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraMoveStartedReason
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import itesm.rieti.model.api.FormError
import itesm.rieti.model.enums.TipoTrabajo
import itesm.rieti.viewModel.nuevoReporte.NuevoReporteState
import itesm.rieti.viewModel.nuevoReporte.NuevoReporteVM
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@Composable
fun NuevoReporte(modifier: Modifier = Modifier)
{
    val nuevoReporteVM: NuevoReporteVM = viewModel()
    val nuevoReporteState by nuevoReporteVM.state.collectAsState()
    val altura = 90.dp
    val pad = 16.dp
    val tamLetra = 20.sp

    val estadoScroll = rememberScrollState()

    var horarioTexto by remember { mutableStateOf("") }
    var fotoTexto by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        if (nuevoReporteState.reporte.dia == null) {
            nuevoReporteVM.setDia(LocalDateTime.now().toString())
        }
    }

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
            NombreCompleto(nuevoReporteVM, nuevoReporteState)

            // Cantidad y edad niños
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            )
            {
                // # niños
                OutlinedTextField(
                    value = nuevoReporteState.reporte.numNinios?.toString() ?: "",
                    onValueChange = {
                        nuevoReporteVM.setNumNinos(it)
                    },
                    label = {
                        Text(
                            "Cantidad Niños",
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    textStyle = TextStyle(fontSize = tamLetra, fontWeight = FontWeight.ExtraBold),
                    isError = nuevoReporteState.errors.contains(FormError.NumN),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .weight(1f)
                        .height(altura)
                )

                // edad
                OutlinedTextField(
                    value = nuevoReporteState.reporte.edad?.toString() ?: "",
                    onValueChange = {
                        nuevoReporteVM.setEdad(it)
                    },
                    label = {
                        Text(
                            "Edad de los Niños",
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    textStyle = TextStyle(fontSize = tamLetra, fontWeight = FontWeight.ExtraBold),
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
                TipoTrabajoDropdown(nuevoReporteVM, nuevoReporteState)
                // Horario
                OutlinedTextField(
                    value = horarioTexto,
                    onValueChange = {
                        horarioTexto = it
                        if (it.isNotBlank()) {
                            nuevoReporteVM.setDia(LocalDateTime.now().toString())
                        }
                    },
                    label = {
                        Text(
                            "Horario",
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    textStyle = TextStyle(fontSize = tamLetra, fontWeight = FontWeight.ExtraBold),
                    isError = nuevoReporteState.errors.contains(FormError.DateTimeMissing),
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
                OutlinedTextField(
                    value = nuevoReporteState.reporte.detalles_adicionales ?: "",
                    onValueChange = {
                        nuevoReporteVM.setDetalles(it)
                    },
                    label = {
                        Text(
                            "Detalles",
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    textStyle = TextStyle(fontSize = tamLetra, fontWeight = FontWeight.ExtraBold),
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
                OutlinedTextField(
                    value = fotoTexto,
                    onValueChange = {
                        fotoTexto = it
                    },
                    label = {
                        Text(
                            "Foto",
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    textStyle = TextStyle(fontSize = tamLetra, fontWeight = FontWeight.ExtraBold),
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
                MapScreen(
                    onUbicacionSelected = { lat, lng ->
                        nuevoReporteVM.setUbicacion(lat, lng)
                    }
                )
            }

            if (nuevoReporteState.errors.isNotEmpty()) {
                Text(
                    text = nuevoReporteState.errors[0].toString(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.error
                )
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
                Button(onClick = { nuevoReporteVM.crearReporte() }) { Text("Enviar Reporte") }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TipoTrabajoDropdown(
    nuevoReporteVM: NuevoReporteVM,
    nuevoReporteState: NuevoReporteState
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        OutlinedTextField(
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth(),
            readOnly = true,
            value = nuevoReporteVM.state.value.reporte.tipoTrabajo?.desc ?: "Tipo Trabajo",
            onValueChange = {  },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            isError = nuevoReporteState.errors.contains(FormError.WorkTypeMissing),
            supportingText = {
                if (nuevoReporteState.errors.contains(FormError.WorkTypeMissing)) {
                    Text(text = FormError.WorkTypeMissing.desc)
                }
            },
            shape = RoundedCornerShape(12.dp)
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            TipoTrabajo.entries.forEach {
                DropdownMenuItem(
                    text = { Text(it.desc) },
                    onClick = {
                        nuevoReporteVM.setTipoTrabajo(it)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    onUbicacionSelected: (Float, Float) -> Unit = { _, _ -> }
)
{
    val ubicacionInicial = LatLng(19.55310179726687, -99.28478736430407)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(ubicacionInicial, 15f)
    }

    val coroutineScope = rememberCoroutineScope()

    // Establecer la ubicación inicial por defecto
    LaunchedEffect(Unit) {
        onUbicacionSelected(ubicacionInicial.latitude.toFloat(), ubicacionInicial.longitude.toFloat())
    }

    // Escuchar cuando la cámara deje de moverse (Equivalente a OnCameraIdle / dragend)
    LaunchedEffect(cameraPositionState.isMoving)
    {
        if (!cameraPositionState.isMoving)
        {
            if (cameraPositionState.cameraMoveStartedReason == CameraMoveStartedReason.GESTURE)
            {
                val centroActual = cameraPositionState.position.target
                onUbicacionSelected(centroActual.latitude.toFloat(), centroActual.longitude.toFloat())
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
                onUbicacionSelected(latLng.latitude.toFloat(), latLng.longitude.toFloat())
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

@Composable
fun NombreCompleto(nuevoReporteVM: NuevoReporteVM, nuevoReporteState: NuevoReporteState, modifier: Modifier = Modifier)
{
    OutlinedTextField(
        value = nuevoReporteState.rawName,
        onValueChange = {
            nuevoReporteVM.setNombreCompleto(it)
        },
        label = {
            Text(
                "Nombre Completo",
                style = MaterialTheme.typography.titleMedium
            )
        },
        textStyle = TextStyle(fontSize = 18.sp),
        isError = nuevoReporteState.errors.contains(FormError.SurnameMissing) || nuevoReporteState.errors.contains(FormError.SurnameIncomplete),
        shape = RoundedCornerShape(12.dp),
        supportingText = {
            if (nuevoReporteState.errors.contains(FormError.SurnameIncomplete)) {
                Text(text = FormError.SurnameIncomplete.desc)
            }
            if (nuevoReporteState.errors.contains(FormError.SurnameMissing)) {
                Text(text = FormError.SurnameMissing.desc)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
fun ReportePreview()
{
    NuevoReporte()
}
