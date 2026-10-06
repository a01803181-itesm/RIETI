package itesm.rieti.view

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
import itesm.rieti.R
import itesm.rieti.model.api.FormError
import itesm.rieti.model.enums.TipoTrabajo
import itesm.rieti.viewModel.ConnectionVM
import itesm.rieti.viewModel.nuevoReporte.NuevoReporteState
import itesm.rieti.viewModel.nuevoReporte.NuevoReporteVM
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@Composable
fun NuevoReporte(modifier: Modifier = Modifier)
{
    val nuevoReporteVM: NuevoReporteVM = viewModel(viewModelStoreOwner = LocalActivity.current as ComponentActivity)
    val nuevoReporteState by nuevoReporteVM.state.collectAsState()
    val connectionVM: ConnectionVM = viewModel(viewModelStoreOwner = LocalActivity.current as ComponentActivity)
    val connectionState by connectionVM.state.collectAsState()
    val estadoScroll = rememberScrollState()
    LaunchedEffect(Unit) {
        if (nuevoReporteState.reporte.dia == null) {
            nuevoReporteVM.setDia(LocalDateTime.now().toString())
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    )
    {
        Text(
            text = "Nuevo Reporte",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier.fillMaxWidth()
        )
        if (!connectionState.internetConnection) {
            Spacer(modifier = Modifier.height(8.dp))
            OfflineHeader()
        }
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(estadoScroll),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        )
        {
            NombreCompleto(nuevoReporteVM, nuevoReporteState)
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            )
            {
                CantidadNNA(nuevoReporteVM, nuevoReporteState, modifier.weight(1f))
                Edad(nuevoReporteVM, nuevoReporteState, modifier.weight(1f))
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            )
            {
                TipoTrabajoDropdown(nuevoReporteVM, nuevoReporteState, Modifier.weight(1f))
                Horario(nuevoReporteVM, nuevoReporteState, Modifier.weight(1f))
            }
            Detalles(nuevoReporteVM, nuevoReporteState)
            TomarFoto(nuevoReporteVM, nuevoReporteState)
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
                    text = "Faltan campos por completar",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                BotonGuardarBorrador(nuevoReporteVM, nuevoReporteState, Modifier.weight(1f))
                if (connectionState.internetConnection) {
                    BotonEnviarReporte(nuevoReporteVM, nuevoReporteState, Modifier.weight(1f))
                }
            }
        }
    }
}
@Composable
fun OfflineHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Estás en modo offline",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Solo puedes guardar reportes como borradores",
                style = MaterialTheme.typography.bodySmall
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Image(
            painter = painterResource(R.drawable.offline),
            contentDescription = "Modo Offline",
            modifier = Modifier.size(25.dp),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant)
        )
    }
}
@Composable
fun TomarFoto(
    nuevoReporteVM: NuevoReporteVM,
    nuevoReporteState: NuevoReporteState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Tomar foto",
            style = MaterialTheme.typography.titleMedium
        )
        IconButton(
            onClick = { },
            shape = RectangleShape
        ) {
            Image(
                painter = painterResource(R.drawable.camera),
                contentDescription = "Tomar Foto",
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant),
                contentScale = ContentScale.Crop
            )
        }
    }
}
@Composable
fun CantidadNNA(
    nuevoReporteVM: NuevoReporteVM,
    nuevoReporteState: NuevoReporteState,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = nuevoReporteState.reporte.numNinios?.toString() ?: "",
        onValueChange = {
            nuevoReporteVM.setNumNinos(it)
            nuevoReporteVM.popError(FormError.NumN)
        },
        label = {
            Text(
                text = "Cantidad Niños",
                style = MaterialTheme.typography.titleMedium
            )
        },
        isError = nuevoReporteState.errors.contains(FormError.NumN),
        supportingText = {
            if (nuevoReporteState.errors.contains(FormError.NumN)) {
                Text(
                    text = FormError.NumN.desc,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = modifier.fillMaxHeight()
    )
}

@Composable
fun Edad(
    nuevoReporteVM: NuevoReporteVM,
    nuevoReporteState: NuevoReporteState,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = nuevoReporteState.reporte.edad?.toString() ?: "",
        onValueChange = { nuevoReporteVM.setEdad(it) },
        label = {
            Text(
                text = "Edad de los Niños",
                style = MaterialTheme.typography.titleMedium
            )
        },
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = modifier.fillMaxHeight()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TipoTrabajoDropdown(
    nuevoReporteVM: NuevoReporteVM,
    nuevoReporteState: NuevoReporteState,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth(),
            readOnly = true,
            value = nuevoReporteVM.state.value.reporte.tipoTrabajo?.desc ?: "Tipo Trabajo",
            onValueChange = { },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
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
                    text = { Text(text = it.desc) },
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
fun Horario(
    nuevoReporteVM: NuevoReporteVM,
    nuevoReporteState: NuevoReporteState,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = nuevoReporteState.reporte.dia ?: "",
        onValueChange = {
            nuevoReporteVM.setDia(it)
            if (it == "") {
                nuevoReporteVM.setDia(LocalDateTime.now().toString())
            }
        },
        label = {
            Text(
                text = "Horario",
                style = MaterialTheme.typography.titleMedium
            )
        },
        isError = nuevoReporteState.errors.contains(FormError.DateTimeMissing),
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        modifier = modifier.height(90.dp)
    )
}

@Composable
fun Detalles(
    nuevoReporteVM: NuevoReporteVM,
    nuevoReporteState: NuevoReporteState,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = nuevoReporteState.reporte.detalles_adicionales ?: "",
        onValueChange = { nuevoReporteVM.setDetalles(it) },
        label = {
            Text(
                text = "Detalles",
                style = MaterialTheme.typography.titleMedium
            )
        },
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp)
    )
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
            nuevoReporteVM.popError(FormError.SurnameIncomplete)
            nuevoReporteVM.popError(FormError.SurnameMissing)
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

@Composable
fun BotonEnviarReporte(
    nuevoReporteVM: NuevoReporteVM,
    nuevoReporteState: NuevoReporteState,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = {
            nuevoReporteVM.crearReporte()
        },
        modifier = modifier
    ) {
        Text(
            text = "Enviar Reporte",
        )
    }
}
@Composable
fun BotonGuardarBorrador(
    nuevoReporteVM: NuevoReporteVM,
    nuevoReporteState: NuevoReporteState,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = {},
        modifier = modifier
    ) {
        Text(
            text = "Guardar borrador"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ReportePreview()
{
    NuevoReporte()
}
