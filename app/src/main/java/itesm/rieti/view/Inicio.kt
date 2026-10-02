package itesm.rieti.view

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
import androidx.compose.material3.CircularProgressIndicator
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
import itesm.rieti.model.cognito.SignIn
import itesm.rieti.viewModel.InicioVM
import itesm.rieti.viewModel.api.UsuariosVM

//Contenedor principal
@Composable
fun RegistroApp(modifier: Modifier = Modifier) {
    val usuariosVM: UsuariosVM = viewModel()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Encabezado()
        CuerpoApp(usuariosVM)
    }
}

//Contenedor del cuerpo
@Composable
fun CuerpoApp(usuariosVM: UsuariosVM, modifier: Modifier = Modifier) {
    val inicioVM: InicioVM = viewModel()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(16.dp)
    ) {
        TitulosLogin()
        Espacio(24.dp)
        Correo(
            correo = inicioVM.correo,
            onCorreoChange = { inicioVM.CorreoCambiado(it) }
        )
        Espacio(24.dp)
        Contrasena(
            contrasena = inicioVM.contrasenia,
            contrasenaChange = { inicioVM.ContrasenaCambiada(it) }
        )
        Espacio(24.dp)
        BotonRegistro(
            usuariosVM = usuariosVM,
            onRegistro = {
                usuariosVM.obtenerUsuario(inicioVM.correo)
            }
        )
        Espacio(24.dp)
        BotonGoogle()
    }
}

@Composable
fun BotonRegistro(
    usuariosVM: UsuariosVM,
    onRegistro: () -> Unit,
    modifier: Modifier = Modifier
) {
    val usuario by usuariosVM.usuarioActual.collectAsState()
    val esperando by usuariosVM.esperando.collectAsState()

    Button(
        onClick = { onRegistro() },
        modifier = modifier.fillMaxWidth()
    ) {
        if (esperando) CircularProgressIndicator() else Text("Iniciar Sesión / Registro")
    }
    if (usuario != null) {
        Text(text = "Bienvenido ${usuario!!.correoU}. Proveedor: ${usuario!!.proveedor}")
    }
}

@Composable
fun BotonGoogle(modifier: Modifier = Modifier) {
    val activity = LocalActivity.current ?: return

    Button(
        onClick = { SignIn.withGoogle(activity) },
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
            text = "Iniciar sesion",
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