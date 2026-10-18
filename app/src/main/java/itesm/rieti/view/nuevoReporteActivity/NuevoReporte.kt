package itesm.rieti.view.nuevoReporteActivity

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import itesm.rieti.R
import itesm.rieti.model.api.FormError
import itesm.rieti.model.preferences.UserPreferences
import itesm.rieti.viewModel.auth.AuthState
import itesm.rieti.viewModel.nuevoReporte.UbicacionVM
import itesm.rieti.viewModel.network.NetworkVM
import itesm.rieti.viewModel.nuevoReporte.NuevoReporteState
import itesm.rieti.viewModel.nuevoReporte.NuevoReporteVM
import itesm.rieti.viewModel.nuevoReporte.SaveState
import itesm.rieti.viewModel.nuevoReporte.SendState
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import kotlin.time.Duration.Companion.milliseconds

/**
 * Pantalla principal para la creación de un nuevo reporte.
 * Contiene los campos necesarios como nombre, número de NNA, tipo de trabajo, detalles, foto y ubicación.
 *
 * @param authState Estado de autenticación.
 * @param ubicacionVM ViewModel de ubicación.
 * @param modifier Modificador para la vista.
 * @param networkVM ViewModel que verifica la conexión a red.
 */
@Composable
fun NuevoReporte(
    nuevoReporteVM: NuevoReporteVM,
    nuevoReporteState: NuevoReporteState,
    authState: AuthState,
    ubicacionVM: UbicacionVM,
    modifier: Modifier = Modifier,
    networkVM: NetworkVM = NetworkVM(LocalContext.current),
    onReporteCreado: () -> Unit,
    onBorradorGuardado: () -> Unit,
    onBackFromBorrador: () -> Unit
) {
    val networkConnectionState by networkVM.isNetworkAvailable.collectAsState()

    val context = LocalContext.current
    val preferences = UserPreferences(context)

    var enableScroll by remember { mutableStateOf(true) }

    BackHandler(enabled = nuevoReporteState.selectedBorrador != null) {
        nuevoReporteVM.setSelectedBorrador(null)
        nuevoReporteVM.resetForm()
        onBackFromBorrador()
    }

    LaunchedEffect(nuevoReporteState.selectedBorrador) {
        val draft = nuevoReporteState.selectedBorrador
        if (draft != null) {
            nuevoReporteVM.fillFieldsWithBorrador(draft)
        }
    }

    LaunchedEffect(Unit) {
        if (nuevoReporteState.selectedBorrador == null && nuevoReporteState.reporte.dia == null) {
            nuevoReporteVM.setHoraYFecha(LocalDateTime.now().toString())
        }
    }

    LaunchedEffect(nuevoReporteState.sendState) {
        when (nuevoReporteState.sendState) {
            SendState.SUCCESS -> {
                delay(1000.milliseconds)
                onReporteCreado()
                nuevoReporteVM.setSendState(SendState.IDLE)
                nuevoReporteVM.resetForm()
                nuevoReporteVM.setHoraYFecha(LocalDateTime.now().toString())
                val currentLoc = ubicacionVM.ubicacion.value
                if (currentLoc != null) {
                    nuevoReporteVM.setCoords(currentLoc.latitude.toFloat(), currentLoc.longitude.toFloat())
                }
            }
            SendState.SUCCESS_AS_ANONYMOUS -> {
                delay(2000.milliseconds)
                nuevoReporteVM.setSendState(SendState.IDLE)
                nuevoReporteVM.resetForm()
                nuevoReporteVM.setHoraYFecha(LocalDateTime.now().toString())
                val currentLoc = ubicacionVM.ubicacion.value
                if (currentLoc != null) {
                    nuevoReporteVM.setCoords(currentLoc.latitude.toFloat(), currentLoc.longitude.toFloat())
                }
            }
            else -> {}
        }
    }

    LaunchedEffect(nuevoReporteState.saveState) {
        if (nuevoReporteState.saveState == SaveState.SUCCESS) {
            delay(1000.milliseconds)
            onBorradorGuardado()
            nuevoReporteVM.setSaveState(SaveState.IDLE)
            nuevoReporteVM.resetForm()
            nuevoReporteVM.setHoraYFecha(LocalDateTime.now().toString())
            val currentLoc = ubicacionVM.ubicacion.value
            if (currentLoc != null) {
                nuevoReporteVM.setCoords(currentLoc.latitude.toFloat(), currentLoc.longitude.toFloat())
            }
        }
    }

    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }
            .padding(16.dp)
    ) {
        if (nuevoReporteState.selectedBorrador != null) {
            DraftLabel(nuevoReporteVM, onBackFromBorrador)
        } else {
            Text(
                text = "Nuevo Reporte",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (!networkConnectionState) {
            Spacer(modifier = Modifier.height(8.dp))
            OfflineLabel()
        }
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState(), enabled = enableScroll),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (nuevoReporteState.displayAnonymousDialog) {
                AnonymousDialog(preferences, authState, nuevoReporteVM)
            } else if (nuevoReporteState.displayConfirmDraftDeletion) {
                DeleteDraftDialog(nuevoReporteVM, nuevoReporteState, onBackFromBorrador)
            }
            NombreCompleto(nuevoReporteVM, nuevoReporteState)
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CantidadNNA(nuevoReporteVM, nuevoReporteState, modifier.weight(4f))
                Edad(nuevoReporteVM, nuevoReporteState, modifier.weight(5f))
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
                    .height(400.dp)
            ) {
                MapScreen(
                    context = context,
                    nuevoReporteVM = nuevoReporteVM,
                    nuevoReporteState = nuevoReporteState,
                    ubicacionVM = ubicacionVM,
                    onUbicacionSelected = { lat, lng ->
                        nuevoReporteVM.setCoords(lat, lng)
                    },
                    onCameraIdle = { enableScroll = true },
                    onCameraMoved = { enableScroll = false }
                )
            }

            if (nuevoReporteState.errors.isNotEmpty() || nuevoReporteState.sendState == SendState.SUCCESS_AS_ANONYMOUS) {
                HorizontalDivider()
                if (nuevoReporteState.errors.isNotEmpty()) {
                    Text(
                        text = if (nuevoReporteState.errors.contains(FormError.ServerError)) nuevoReporteState.errors[FormError.ServerError] ?: "Error en servidor" else "Hay campos con errores",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.fillMaxWidth()
                    )
                } else if (nuevoReporteState.sendState == SendState.SUCCESS_AS_ANONYMOUS) {
                    Text(
                        text = "¡Reporte enviado exitosamente como anónimo!",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFF2E7D32),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                BotonGuardarBorrador(authState, nuevoReporteState, nuevoReporteVM, Modifier.weight(1f))
                if (networkConnectionState) {
                    BotonEnviarReporte(preferences, authState, nuevoReporteVM, nuevoReporteState, Modifier.weight(1f))
                }
            }
        }
    }
}

/**
 * Botón para enviar un reporte a los servidores.
 *
 * @param authState Estado de autenticación del usuario.
 * @param nuevoReporteVM ViewModel del nuevo reporte.
 * @param modifier Modificador para la vista.
 */
@Composable
fun BotonEnviarReporte(
    preferences: UserPreferences,
    authState: AuthState,
    nuevoReporteVM: NuevoReporteVM,
    nuevoReporteState: NuevoReporteState,
    modifier: Modifier = Modifier
) {
    val awareOfAnonymous by preferences.awareOfAnonymousFlow.collectAsStateWithLifecycle(initialValue = false)
    Button(
        onClick = {
            if (
                !awareOfAnonymous &&
                nuevoReporteState.rawName == ""
                && nuevoReporteVM.assertFieldsCompletion()
            ) {
                nuevoReporteVM.setDisplayAnonymousDialog(true)
            } else {
                nuevoReporteVM.crearReporte(usuario = authState.usuario)
            }
        },
        enabled = nuevoReporteState.sendState == SendState.IDLE && nuevoReporteState.saveState == SaveState.IDLE,
        colors = ButtonDefaults.buttonColors(
            containerColor = when (nuevoReporteState.sendState) {
                    SendState.IDLE -> MaterialTheme.colorScheme.primary
                    SendState.SENDING -> Color.DarkGray
                    SendState.SUCCESS, SendState.SUCCESS_AS_ANONYMOUS -> Color.Green
                    SendState.ERROR -> MaterialTheme.colorScheme.errorContainer
                },
            contentColor = when (nuevoReporteState.sendState) {
                SendState.IDLE -> MaterialTheme.colorScheme.onPrimary
                SendState.SENDING -> Color.White
                SendState.SUCCESS, SendState.SUCCESS_AS_ANONYMOUS -> Color.White
                SendState.ERROR -> MaterialTheme.colorScheme.onError
            }
        ),
        modifier = modifier
    ) {
        when (nuevoReporteState.sendState) {
            SendState.IDLE -> Text(text = "Enviar Reporte")
            SendState.SENDING -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp)
                )
                Text(text = "Enviando...")
            }
            SendState.SUCCESS, SendState.SUCCESS_AS_ANONYMOUS -> {
                Image(
                    painter = painterResource(R.drawable.check),
                    contentDescription = "Reporte enviado",
                    modifier = Modifier.size(16.dp),
                    colorFilter = ColorFilter.tint(Color.White)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "¡Reporte Enviado!")
            }
            SendState.ERROR -> {
                LaunchedEffect(Unit) {
                    delay(3000.milliseconds)
                    nuevoReporteVM.setSendState(SendState.IDLE)
                }
                Image(
                    painter = painterResource(R.drawable.cross),
                    contentDescription = "Error al enviar reporte",
                    modifier = Modifier.size(16.dp),
                    colorFilter = ColorFilter.tint(Color.White)
                )
                Text(text = "Error al enviar reporte")
            }
        }
    }
}
/**
 * Botón para guardar el reporte actual como borrador local.
 *
 * @param nuevoReporteVM ViewModel del nuevo reporte.
 * @param modifier Modificador para la vista.
 */
@Composable
fun BotonGuardarBorrador(
    authState: AuthState,
    nuevoReporteState: NuevoReporteState,
    nuevoReporteVM: NuevoReporteVM,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Button(
        onClick = {
            nuevoReporteVM.guardarComoBorrador(context, authState.usuario?.correoU ?: "anonymous@nowhere.com")
        },
        modifier = modifier,
        enabled = nuevoReporteState.saveState == SaveState.IDLE,
        colors = ButtonDefaults.buttonColors(
            containerColor = when (nuevoReporteState.saveState) {
                SaveState.IDLE -> MaterialTheme.colorScheme.primary
                SaveState.SENDING -> Color.DarkGray
                SaveState.SUCCESS -> Color.Green
                SaveState.ERROR -> MaterialTheme.colorScheme.errorContainer
            },
            contentColor = when (nuevoReporteState.saveState) {
                SaveState.IDLE -> MaterialTheme.colorScheme.onPrimary
                SaveState.SENDING -> Color.White
                SaveState.SUCCESS -> Color.White
                SaveState.ERROR -> MaterialTheme.colorScheme.onError
            }
        )
    ) {
        when (nuevoReporteState.saveState) {
            SaveState.SENDING -> {
                CircularProgressIndicator()
                Text(text = "Guardando...")
            }
            SaveState.IDLE -> Text(text = "Guardar borrador")
            SaveState.SUCCESS -> {
                Image(
                    painter = painterResource(R.drawable.check),
                    contentDescription = "¡Borrador guardado!",
                    modifier = Modifier.size(16.dp),
                    colorFilter = ColorFilter.tint(Color.White)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "¡Borrador guardado!")
            }
            SaveState.ERROR -> {
                Image(
                    painter = painterResource(R.drawable.cross),
                    contentDescription = "Error al guardar borrador",
                    modifier = Modifier.size(16.dp),
                    colorFilter = ColorFilter.tint(Color.White)
                )
                Text(text = "Error al guardar borrador")
            }
        }
    }
}

/**
 * Vista previa de la pantalla de nuevo reporte.
 *
 * @param authState Estado de autenticación.
 * @param ubicacionVM ViewModel de ubicación.
 */
@Preview(showBackground = true)
@Composable
fun ReportePreview(
    authState: AuthState = AuthState(),
    ubicacionVM: UbicacionVM = UbicacionVM(),
    nuevoReporteVM: NuevoReporteVM = NuevoReporteVM(),
    nuevoReporteState: NuevoReporteState = NuevoReporteState(),
    networkVM: NetworkVM = NetworkVM(LocalContext.current)
) {
    NuevoReporte(
        nuevoReporteVM,
        nuevoReporteState,
        authState,
        ubicacionVM,
        Modifier,
        networkVM,
        {},
        {},
        {}
    )
}