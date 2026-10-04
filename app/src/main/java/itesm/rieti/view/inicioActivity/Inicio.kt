package itesm.rieti.view.inicioActivity

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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import itesm.rieti.R
import itesm.rieti.model.esquemas.Provider
import itesm.rieti.viewModel.auth.AuthState
import itesm.rieti.viewModel.auth.AuthVM
import itesm.rieti.viewModel.auth.CognitoState
import itesm.rieti.viewModel.auth.CognitoVM
import itesm.rieti.viewModel.auth.GoogleVM

//Contenedor principal
@Composable
fun RegistroApp(modifier: Modifier = Modifier) {
    val cognitoVM: CognitoVM = viewModel()
    val cognitoState by cognitoVM.cognitoState.collectAsState()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Encabezado()
        if (!cognitoState.otpSent) {
            CuerpoApp(cognitoVM)
        } else {
            OTPScreen(cognitoVM)
        }
    }
}

//Contenedor del cuerpo
@Composable
fun CuerpoApp(cognitoVM: CognitoVM, modifier: Modifier = Modifier) {
    val authVM: AuthVM = viewModel()
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
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.error,
                modifier = modifier.padding(16.dp)
            )
        }
        Espacio(24.dp)
        BotonRegistro(
            authState,
            cognitoState,
            authVM,
            onRegistro = {
                cognitoVM.authenticate(
                    email = authState.usuario?.correoU ?: "",
                    onSuccess = { sub ->
                        authVM.setLoggedIn(true)
                        authVM.setSUB(sub)
                        authVM.setEmail(authState.usuario?.correoU ?: "", authState.usuario?.proveedor ?: Provider.COGNITO)
                        authVM.setPictureURL(null)
                    },
                    onError = { authVM.setError(it) }
                )
            }
        )
        Espacio(24.dp)
        BotonGoogle()
    }
}

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

@Composable
fun BotonGoogle(modifier: Modifier = Modifier) {
    val googleVM: GoogleVM = viewModel()
    val authVM: AuthVM = viewModel()
    val activity = LocalActivity.current ?: return

    Button(
        onClick = {
            googleVM.authenticate(
                activity,
                { sub, pictureURL ->
                    authVM.setLoggedIn(true)
                    authVM.setSUB(sub)
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

//Header
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
            text = "Rieti",
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun TitulosLogin(modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = "Iniciar sesión",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "Ingresa tus datos para continuar",
            fontSize = 16.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun Espacio(distancia: Dp, modifier: Modifier = Modifier) {
    Spacer(
        modifier = modifier.height(distancia)
    )
}

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
            fontSize = 14.sp,
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

@Composable
fun Contrasena(
    contrasena: String,
    contrasenaChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "Contraseña",
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        OutlinedTextField(
            value = contrasena,
            onValueChange = contrasenaChange,
            placeholder = { Text("••••••••", color = Color.Gray) },
            modifier = modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainAppPreview() {
    RegistroApp()
}