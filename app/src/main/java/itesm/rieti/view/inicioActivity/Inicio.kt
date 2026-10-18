package itesm.rieti.view.inicioActivity

import android.util.Log
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import itesm.rieti.R
import itesm.rieti.model.esquemas.Provider
import itesm.rieti.viewModel.auth.AuthStage
import itesm.rieti.viewModel.auth.AuthState
import itesm.rieti.viewModel.auth.AuthVM
import itesm.rieti.viewModel.auth.CognitoState
import itesm.rieti.viewModel.auth.CognitoVM
import itesm.rieti.viewModel.auth.GoogleVM

/**
 * Pantalla principal de registro e inicio de sesión de la aplicación.
 *
 * @param modifier Modificador para la vista.
 */
@Composable
fun RegistroApp(cognitoVM: CognitoVM, modifier: Modifier = Modifier) {
    val authVM: AuthVM = viewModel()
    val cognitoState by cognitoVM.cognitoState.collectAsState()
    val authState by authVM.authState.collectAsState()

    LaunchedEffect(authState.loggedIn) {
        if (!authState.loggedIn) {
            cognitoVM.resetState()
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Encabezado()
        when {
            cognitoState.isRecoveringPassword -> {
                OTPRecoverPasswordScreen(authVM, cognitoVM)
            }
            cognitoState.otpSent && authState.usuario != null -> {
                OTPSignUpScreen(cognitoVM)
            }
            else -> {
                CuerpoApp(authVM, cognitoVM)
            }
        }
    }
}

/**
 * Componente que muestra los campos y botones principales para iniciar sesión o registrarse.
 *
 * @param authVM ViewModel de autenticación.
 * @param cognitoVM ViewModel de AWS Cognito.
 * @param modifier Modificador para la vista.
 */
@Composable
fun CuerpoApp(authVM: AuthVM, cognitoVM: CognitoVM, modifier: Modifier = Modifier) {
    val authState by authVM.authState.collectAsState()
    val cognitoState by cognitoVM.cognitoState.collectAsState()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(16.dp)
    ) {
        TitulosLogin()
        Espacio(24.dp)
        Correo(
            correo = authState.usuario?.correoU ?: "",
            onCorreoChange = { authVM.setEmail(it, Provider.COGNITO) }
        )
        Espacio(24.dp)
        Contrasena(
            contrasena = cognitoState.password,
            contrasenaChange = { cognitoVM.setPassword(it) }
        )
        if (authState.error != null) {
            Text(
                text = authState.error!!,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error,
                modifier = modifier.padding(16.dp)
            )
        }
        if (authState.stage == AuthStage.LOADING) {
            Spacer(modifier = Modifier.height(32.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(modifier = Modifier.height(32.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Cargando...",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        Espacio(24.dp)
        TextButton(
            onClick = {
                cognitoVM.resetPassword(
                    email = authState.usuario?.correoU ?: "",
                    onSuccess = {
                        cognitoVM.setRecoveringPassword(true)
                        authVM.setError(null)
                    },
                    onError = { authVM.setError(it) }
                )
            },
            modifier = Modifier.align(Alignment.End),
            enabled = authState.usuario?.correoU?.isNotEmpty() == true
        ) {
            Text(
                text = "¿Olvidaste tu contraseña?",
                color = MaterialTheme.colorScheme.primary
            )
        }
        BotonRegistro(
            authState,
            cognitoState,
            authVM,
            onRegistro = {
                cognitoVM.authenticate(
                    onLoading = { authVM.setAuthStage(AuthStage.LOADING) },
                    email = authState.usuario?.correoU ?: "",
                    onSuccess = { sub ->
                        Log.i("AUTH", "Authentication successful")
                        authVM.setLoggedIn(true)
                        authVM.setSUB(sub)
                        authVM.setEmail(authState.usuario?.correoU ?: "", authState.usuario?.proveedor ?: Provider.COGNITO)
                        authVM.setPictureURL(null)
                        authVM.setAuthStage(AuthStage.IDLE)
                    },
                    onError = {
                        Log.e("AUTH", "Authentication failed: $it")
                        authVM.setError(it)
                        authVM.setAuthStage(AuthStage.IDLE)
                    }
                )
            }
        )
        Espacio(24.dp)
        BotonGoogle(authVM)
    }
}

/**
 * Botón para realizar la acción de inicio de sesión o registro.
 *
 * @param authState Estado de la autenticación.
 * @param cognitoState Estado de AWS Cognito.
 * @param authVM ViewModel de autenticación.
 * @param onRegistro Callback que se ejecuta al presionar el botón y estar los datos validados.
 * @param modifier Modificador para la vista.
 */
@Composable
fun BotonRegistro(
    authState: AuthState,
    cognitoState: CognitoState,
    authVM: AuthVM,
    onRegistro: () -> Unit,
    modifier: Modifier = Modifier
) {
    val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
    Button(
        onClick = {
            if (authState.usuario?.correoU?.isNotEmpty() == true && !emailRegex.matches(authState.usuario.correoU)) {
                authVM.setError("Correo electrónico inválido")
            } else {
                onRegistro()
            }
        },
        enabled = authState.usuario != null && authState.usuario.correoU.isNotEmpty() && cognitoState.password.isNotEmpty(),
        modifier = modifier.fillMaxWidth()
    ) {
        Text("Iniciar Sesión / Registrarse")
    }
}

/**
 * Botón para iniciar sesión utilizando Google.
 *
 * @param authVM ViewModel de autenticación.
 * @param modifier Modificador para la vista.
 */
@Composable
fun BotonGoogle(authVM: AuthVM, modifier: Modifier = Modifier) {
    val googleVM: GoogleVM = viewModel()
    val activity = LocalActivity.current ?: return

    Button(
        onClick = {
            googleVM.authenticate(
                activity,
                { user, sub, pictureURL ->
                    authVM.setLoggedIn(true)
                    authVM.setSUB(sub)
                    authVM.setEmail(user.correoU, user.proveedor)
                    authVM.setPictureURL(pictureURL)
                },
                { authVM.setError(it) }
            )
        },
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = modifier.wrapContentWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.google),
                contentDescription = "Google Login",
                modifier = modifier.size(18.dp)
            )
            Text(text = "Continuar con Google")
        }
    }
}

/**
 * Encabezado de la pantalla de inicio con el logotipo de RIETI.
 *
 * @param modifier Modificador para la vista.
 */
@Composable
fun Encabezado(modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "RIETI",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Image(
            painter = painterResource(R.drawable.logo_rieti_only_vectors),
            contentDescription = "RIETI",
            modifier = modifier.size(100.dp)
        )
    }
}

/**
 * Textos de título para la pantalla de inicio de sesión.
 *
 * @param modifier Modificador para la vista.
 */
@Composable
fun TitulosLogin(modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = "Iniciar sesión",
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "Ingresa tus datos para continuar",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray
        )
    }
}

/**
 * Componente auxiliar para agregar espacio vertical.
 *
 * @param distancia Altura del espacio en Dp.
 * @param modifier Modificador para la vista.
 */
@Composable
fun Espacio(distancia: Dp, modifier: Modifier = Modifier) {
    Spacer(
        modifier = modifier.height(distancia)
    )
}

/**
 * Campo de texto para ingresar el correo electrónico.
 *
 * @param correo Correo electrónico actual.
 * @param onCorreoChange Callback que se ejecuta cuando el texto cambia.
 * @param modifier Modificador para la vista.
 */
@Composable
fun Correo(
    correo: String,
    onCorreoChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "Correo electrónico",
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        OutlinedTextField(
            value = correo,
            onValueChange = onCorreoChange,
            placeholder = { Text("ejemplo@correo.com", color = Color.Gray) },
            modifier = modifier.fillMaxWidth()
        )
    }
}

/**
 * Campo de texto para ingresar la contraseña.
 *
 * @param contrasena Contraseña actual.
 * @param contrasenaChange Callback que se ejecuta cuando el texto cambia.
 * @param modifier Modificador para la vista.
 */
@Composable
fun Contrasena(
    contrasena: String,
    contrasenaChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = "Contraseña",
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        OutlinedTextField(
            value = contrasena,
            onValueChange = contrasenaChange,
            placeholder = { Text("••••••••", color = Color.Gray) },
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                val description = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = description)
                }
            },
            modifier = modifier.fillMaxWidth()
        )
    }
}

/**
 * Vista previa de la pantalla principal de registro/inicio.
 */
@Preview(showBackground = true)
@Composable
fun MainAppPreview(cognitoVM: CognitoVM = viewModel()) {
    RegistroApp(cognitoVM)
}