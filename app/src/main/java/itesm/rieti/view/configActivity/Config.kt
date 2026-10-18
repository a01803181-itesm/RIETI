package itesm.rieti.view.configActivity

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import itesm.rieti.R
import itesm.rieti.model.esquemas.Provider
import itesm.rieti.view.inicioActivity.OTPRecoverPasswordScreen
import itesm.rieti.viewModel.auth.AuthState
import itesm.rieti.viewModel.config.ConfigState
import itesm.rieti.viewModel.config.FontSize
import itesm.rieti.viewModel.config.Theme
import itesm.rieti.viewModel.auth.AuthVM
import itesm.rieti.viewModel.auth.CognitoVM
import itesm.rieti.viewModel.config.ConfigVM
import itesm.rieti.viewModel.history.HistorialVM
import itesm.rieti.viewModel.network.NetworkVM
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

/**
 * Pantalla de configuración principal de la aplicación.
 * Permite al usuario ver su perfil, cambiar la apariencia (tema y tamaño de letra),
 * y gestionar su cuenta.
 *
 * @param authVM ViewModel de autenticación.
 * @param historialVM ViewModel del historial de reportes.
 * @param modifier Modificador para la vista.
 */
@Composable
fun ConfigApp(
    cognitoVM: CognitoVM,
    authVM: AuthVM,
    authState: AuthState,
    historialVM: HistorialVM,
    modifier: Modifier = Modifier,
    networkVM: NetworkVM = NetworkVM(LocalContext.current)
) {
    val scrollState = rememberScrollState()
    val configVM: ConfigVM = viewModel(viewModelStoreOwner = LocalActivity.current as ComponentActivity)
    val configState by configVM.state.collectAsState()
    val cognitoState by cognitoVM.cognitoState.collectAsState()

    val networkConnection by networkVM.isNetworkAvailable.collectAsState()

    var displayPasswordChangedDialog by remember { mutableStateOf(false) }
    var displayConfirmSignOutDialog by remember { mutableStateOf(false) }

    LaunchedEffect(displayPasswordChangedDialog) {
        if (displayPasswordChangedDialog) {
            delay(3000.milliseconds)
            displayPasswordChangedDialog = false
        }
    }

    if (displayPasswordChangedDialog) PasswordChangedDialog()

    if (displayConfirmSignOutDialog) {
        ConfirmSignOutDialog(
            onDismiss = { displayConfirmSignOutDialog = false },
            onConfirm = { authVM.signOut { historialVM.flush() } }
        )
    }

    if (cognitoState.isRecoveringPassword) {
        ChangePassword(
            authVM,
            cognitoVM,
            historialVM,
        ) {
            displayPasswordChangedDialog = true
        }
    } else {
        ConfigContent(
            scrollState,
            networkConnection,
            configVM,
            configState,
            cognitoVM,
            authVM,
            authState,
            historialVM
        ) {
            displayConfirmSignOutDialog = true
        }
    }
}

@Composable
fun ChangePassword(
    authVM: AuthVM,
    cognitoVM: CognitoVM,
    historialVM: HistorialVM,
    onSuccess: () -> Unit
)
{
    OTPRecoverPasswordScreen(
        authVM,
        cognitoVM,
        onSuccess = {
            onSuccess()
            authVM.signOut { historialVM.flush() }
        }
    ) {
        BackHandler {
            cognitoVM.setRecoveringPassword(false)
            authVM.setError(null)
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { cognitoVM.setRecoveringPassword(false) }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver a configuración de cuenta"
                )
            }
            Text(
                text = "Volver a configuración de cuenta",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(64.dp))
    }
}

@Composable
fun ConfigContent(
    scrollState: ScrollState,
    networkConnection: Boolean,
    configVM: ConfigVM,
    configState: ConfigState,
    cognitoVM: CognitoVM,
    authVM: AuthVM,
    authState: AuthState,
    historialVM: HistorialVM,
    modifier: Modifier = Modifier,
    onAttemptSignOut: () -> Unit
) {

    Column (modifier = modifier
        .fillMaxSize()
        .padding((18.dp))
        .verticalScroll(scrollState)
    ) {
        Text("Configuración",  style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.ExtraBold )
        Spacer(modifier = Modifier.height(12.dp))
        Perfil(authVM)
        Spacer(modifier = Modifier.height(12.dp))
        Apariencia(configVM, configState)
        Spacer(modifier = Modifier.height(12.dp))
        TamanioLetra(configVM, configState)
        Spacer(modifier = Modifier.height(16.dp))
        Cuenta(networkConnection, cognitoVM, historialVM, authVM, authState) { onAttemptSignOut() }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

/**
 * Componente que muestra la imagen de perfil y el correo del usuario actual.
 *
 * @param authVM ViewModel de autenticación.
 * @param modifier Modificador para la vista.
 */
@Composable
fun Perfil(authVM: AuthVM, modifier: Modifier = Modifier) {
    val authState by authVM.authState.collectAsState()
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = modifier.padding(bottom = 16.dp)
        ) {
            if (authState.pictureURL != null) {
                val highPictureURL = authState.pictureURL!!.replace(Regex("s\\d+-c"), "s400-c")
                AsyncImage(
                    model = highPictureURL,
                    contentDescription = "Imagen de perfil de ${authState.usuario?.correoU}",
                    error = painterResource(R.drawable.user),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(175.dp)
                        .clip(CircleShape)
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.user),
                    contentDescription = "Imagen de perfil de ${authState.usuario?.correoU}",
                    modifier = Modifier
                        .size(175.dp)
                        .clip(CircleShape)
                )
            }
            if (authState.usuario?.proveedor == Provider.GOOGLE) {
                Image(
                    painter = painterResource(R.drawable.google),
                    contentDescription = "Logueado con Google",
                    modifier = Modifier
                        .size(32.dp)
                        .offset(y = 16.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = authState.usuario?.correoU ?: "anonymous",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
/**
 * Componente que permite seleccionar el tema (apariencia) de la aplicación.
 *
 * @param configVM ViewModel de la configuración.
 * @param configState Estado de la configuración actual.
 * @param modifier Modificador para la vista.
 */
@Composable
fun Apariencia(
    configVM: ConfigVM,
    configState: ConfigState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding((16.dp))
            .fillMaxWidth()
    ) {
        Text("Apariencia", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
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
                SingleChoiceSegmentedButtonRow(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Theme.entries.forEachIndexed { index, theme ->
                        SegmentedButton(
                            selected = configState.theme == theme,
                            onClick = { configVM.setTheme(theme) },
                            shape = SegmentedButtonDefaults.itemShape(index = index, count = Theme.entries.size)
                        ) {
                            Text(
                                text = theme.desc
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Componente que permite seleccionar el tamaño de letra de la aplicación.
 *
 * @param configVM ViewModel de la configuración.
 * @param configState Estado de la configuración actual.
 * @param modifier Modificador para la vista.
 */
@Composable
fun TamanioLetra(
    configVM: ConfigVM,
    configState: ConfigState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text("Tamaño de Letra", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
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
                    FontSize.entries.forEachIndexed { index, fontSize ->
                        SegmentedButton(
                            selected = configState.fontSize == fontSize,
                            onClick = { configVM.setFontSize(fontSize) },
                            shape = SegmentedButtonDefaults.itemShape(index = index, count = FontSize.entries.size)
                        ) {
                            Text(text = fontSize.desc)
                        }
                    }
                }
            }
        }
    }
}


/**
 * Componente que muestra los detalles de la cuenta y la opción de cerrar sesión.
 *
 * @param historialVM ViewModel del historial de reportes.
 * @param authVM ViewModel de autenticación.
 * @param modifier Modificador para la vista.
 */
@Composable
fun Cuenta(
    networkConnection: Boolean,
    cognitoVM: CognitoVM,
    historialVM: HistorialVM,
    authVM: AuthVM,
    authState: AuthState,
    modifier: Modifier = Modifier,
    onAttemptSignOut: () -> Unit
) {
    var displayChangePasswordUnavailableDialog by remember { mutableStateOf(false) }

    if (displayChangePasswordUnavailableDialog) {
        ChangePasswordUnavailableDialog { displayChangePasswordUnavailableDialog = false }
    }

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "Cuenta",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = modifier.fillMaxWidth(),
            textAlign = TextAlign.Left
        )
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            elevation = CardDefaults.cardElevation(4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.outline,
                    shape = MaterialTheme.shapes.medium
                )
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .wrapContentHeight()
            ) {
                if (authState.usuario!!.proveedor == Provider.COGNITO) {
                    Image(
                        painter = painterResource(R.drawable.padlock),
                        contentDescription = "Cambiar Contraseña",
                        modifier = Modifier
                            .size(50.dp)
                            .clickable {
                                if (!networkConnection) {
                                    displayChangePasswordUnavailableDialog = true
                                } else {
                                    displayChangePasswordUnavailableDialog = false
                                    cognitoVM.resetPassword(
                                        email = authState.usuario.correoU,
                                        onSuccess = {
                                            cognitoVM.setRecoveringPassword(true)
                                            authVM.setError(null)
                                        },
                                        onError = { authVM.setError(it) }
                                    )
                                }
                            },
                        contentScale = ContentScale.Crop,
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                }
                Image(
                    painter = painterResource(R.drawable.out),
                    contentDescription = "Cerrar Sesión",
                    modifier = Modifier
                        .size(50.dp)
                        .clickable { onAttemptSignOut() },
                    contentScale = ContentScale.Crop,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant)
                )
            }
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                if (authState.usuario!!.proveedor == Provider.COGNITO) {
                    Text(
                        text = "Cambiar\nContraseña",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        modifier = modifier
                            .weight(1f)
                            .clickable {
                                cognitoVM.resetPassword(
                                    email = authState.usuario.correoU,
                                    onSuccess = {
                                        cognitoVM.setRecoveringPassword(true)
                                        authVM.setError(null)
                                    },
                                    onError = { authVM.setError(it) }
                                )
                            }
                    )
                }
                Text(
                    text = "Cerrar Sesión",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    modifier = modifier
                        .weight(1f)
                        .clickable { onAttemptSignOut() }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

/**
 * Vista previa de la pantalla de configuración.
 *
 * @param authVM ViewModel de autenticación.
 * @param historialVM ViewModel del historial de reportes.
 */
@Preview(showBackground = true)
@Composable
fun ConfigPreview(
    cognitoVM: CognitoVM = viewModel(),
    authVM: AuthVM = viewModel(),
    authState: AuthState = AuthState(),
    historialVM: HistorialVM = viewModel()
) {
    ConfigApp(
        cognitoVM,
        authVM,
        authState,
        historialVM
    )
}