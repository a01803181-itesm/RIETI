package itesm.rieti.view

import android.util.Log
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Remove
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amplifyframework.predictions.models.AgeRange
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraMoveStartedReason
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import itesm.rieti.R
import itesm.rieti.model.api.FormError
import itesm.rieti.model.enums.RangoEdad
import itesm.rieti.model.enums.TipoTrabajo
import itesm.rieti.view.nuevoReporte.CameraCaptureField
import itesm.rieti.view.nuevoReporte.SeleccionarHorario
import itesm.rieti.viewModel.network.NetworkVM
import itesm.rieti.viewModel.nuevoReporte.NuevoReporteState
import itesm.rieti.viewModel.nuevoReporte.NuevoReporteVM
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@Composable
fun NuevoReporte(modifier: Modifier = Modifier, networkVM: NetworkVM = NetworkVM(LocalContext.current))
{
    val nuevoReporteVM: NuevoReporteVM = viewModel(viewModelStoreOwner = LocalActivity.current as ComponentActivity)
    val nuevoReporteState by nuevoReporteVM.state.collectAsState()
    val networkConnectionState by networkVM.isNetworkAvailable.collectAsState()

    val estadoScroll = rememberScrollState()

    LaunchedEffect(Unit) {
        if (nuevoReporteState.reporte.dia == null) {
            nuevoReporteVM.setHoraYFecha(LocalDateTime.now().toString())
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
        if (!networkConnectionState) {
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
                modifier = modifier.fillMaxWidth().wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CantidadNNA(nuevoReporteVM, nuevoReporteState, modifier.weight(1f))
                Edad(nuevoReporteVM, nuevoReporteState, modifier.weight(1f))
            }
            TipoTrabajoDropdown(nuevoReporteVM, nuevoReporteState)
            SeleccionarHorario(nuevoReporteVM, nuevoReporteState)
            Detalles(nuevoReporteVM, nuevoReporteState)
            CameraCaptureField(
                fotoUri = nuevoReporteState.imageUri,
                onFotoCaptured = { nuevoReporteVM.setImageUri(it) },
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            ) {
                MapScreen(
                    onUbicacionSelected = { lat, lng ->
                        nuevoReporteVM.setCoords(lat, lng)
                    }
                )
            }

            if (nuevoReporteState.errors.isNotEmpty()) {
                Log.i("Form", "Missing fields")
                nuevoReporteState.errors.forEach { error, string ->
                    Log.i("Form", "$error: $string")
                }
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
                if (networkConnectionState) {
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
fun CantidadNNA(
    nuevoReporteVM: NuevoReporteVM,
    nuevoReporteState: NuevoReporteState,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = nuevoReporteState.reporte.numNinios.toString(),
        textStyle = TextStyle(textAlign = TextAlign.Center),
        onValueChange = { nuevoReporteVM.setNNAs(it) },
        label = {
            Text(
                text = buildAnnotatedString {
                    append("No. de NNAs")
                    withStyle(SpanStyle(color = Color.Red)) {
                        append(" *")
                    }
                },
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Left,
            )
        },
        leadingIcon = {
            IconButton(
                onClick = { if (nuevoReporteState.reporte.numNinios > 1) nuevoReporteVM.setNNAs((nuevoReporteState.reporte.numNinios - 1).toString()) },
                enabled = nuevoReporteState.reporte.numNinios > 1,
            ) {
                Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = "Restar",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        trailingIcon = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = modifier.padding(4.dp)
            ) {
                IconButton(
                    onClick = { nuevoReporteVM.setNNAs((nuevoReporteState.reporte.numNinios + 1).toString()) },
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Sumar",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        shape = RoundedCornerShape(12.dp),
        isError = nuevoReporteState.errors.contains(FormError.NNAMissing),
        supportingText = {
            if (nuevoReporteState.errors.contains(FormError.NNAMissing)) {
                Text(
                    text = nuevoReporteState.errors[FormError.NNAMissing]!!,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Edad(
    nuevoReporteVM: NuevoReporteVM,
    nuevoReporteState: NuevoReporteState,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier,
    ) {
        OutlinedTextField(
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth(),
            readOnly = true,
            value = nuevoReporteVM.state.value.reporte.rangoEdad?.desc ?: "",
            label = {
                Text(
                    text = buildAnnotatedString {
                        append("Rango de edades")
                        withStyle(SpanStyle(color = Color.Red)) {
                            append(" *")
                        }
                    },
                    style = MaterialTheme.typography.titleMedium
                )
            },
            onValueChange = { },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            isError = nuevoReporteState.errors.contains(FormError.AgeRangeMissing),
            supportingText = {
                if (nuevoReporteState.errors.contains(FormError.AgeRangeMissing)) {
                    Text(
                        text = nuevoReporteState.errors[FormError.AgeRangeMissing]!!,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            shape = RoundedCornerShape(12.dp)
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            RangoEdad.entries.forEach {
                DropdownMenuItem(
                    text = { Text(text = it.desc) },
                    onClick = {
                        nuevoReporteVM.setRangoEdad(it)
                        expanded = false
                    }
                )
            }
        }
    }
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
        modifier = modifier,
    ) {
        OutlinedTextField(
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth(),
            readOnly = true,
            value = nuevoReporteVM.state.value.reporte.tipoTrabajo?.desc ?: "",
            label = {
                Text(
                    text = buildAnnotatedString {
                        append("Tipo de trabajo")
                        withStyle(SpanStyle(color = Color.Red)) {
                            append(" *")
                        }
                    },
                    style = MaterialTheme.typography.titleMedium
                )
            },
            onValueChange = { },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            isError = nuevoReporteState.errors.contains(FormError.WorkTypeMissing),
            supportingText = {
                if (nuevoReporteState.errors.contains(FormError.WorkTypeMissing)) {
                    Text(
                        text = nuevoReporteState.errors[FormError.WorkTypeMissing]!!,
                        color = MaterialTheme.colorScheme.error
                    )
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
                        nuevoReporteVM.setWorkType(it)
                        expanded = false
                    }
                )
            }
        }
    }
}
@Composable
fun Detalles(
    nuevoReporteVM: NuevoReporteVM,
    nuevoReporteState: NuevoReporteState,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = nuevoReporteState.reporte.detalles_adicionales ?: "",
        onValueChange = { nuevoReporteVM.setDetails(it) },
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
    onUbicacionSelected: (Float, Float) -> Unit
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
    var showHelpDialog by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = nuevoReporteState.rawName,
        onValueChange = { nuevoReporteVM.setRawName(it) },
        label = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Nombre Completo (o Anónimo)",
                    style = MaterialTheme.typography.titleMedium
                )
                IconButton(
                    onClick = { showHelpDialog = true }
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Ayuda",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        textStyle = TextStyle(fontSize = 18.sp),
        isError = nuevoReporteState.errors.contains(FormError.InvalidName),
        shape = RoundedCornerShape(12.dp),
        supportingText = {
            if (nuevoReporteState.errors.contains(FormError.InvalidName)) {
                Text(
                    text = nuevoReporteState.errors[FormError.InvalidName]!!,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
    )
    if (showHelpDialog) {
        Dialog(onDismissRequest = { showHelpDialog = false }) {
            Column(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Nombre Completo (o Anónimo)",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "En RIETI, nos preocupamos por la privacidad de los datos de nuestros usuarios;" +
                            " nuestra aplicación solo guarda el correo electrónico de nuestros usuarios." +
                            "\nPor lo que tiene la libertad de mandar el reporte de manera anónima" +
                            " (dejar este campo vacío) o, en su caso, enviarlo con su nombre completo.",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun BotonEnviarReporte(
    nuevoReporteVM: NuevoReporteVM,
    nuevoReporteState: NuevoReporteState,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = { nuevoReporteVM.crearReporte() },
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
        onClick = { nuevoReporteVM.guardarComoBorrador() },
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
