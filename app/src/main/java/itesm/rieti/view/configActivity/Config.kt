package itesm.rieti.view.configActivity

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import itesm.rieti.R
import itesm.rieti.model.esquemas.Provider
import itesm.rieti.viewModel.config.ConfigState
import itesm.rieti.viewModel.config.FontSize
import itesm.rieti.viewModel.config.Theme
import itesm.rieti.viewModel.auth.AuthVM
import itesm.rieti.viewModel.config.ConfigVM
import itesm.rieti.viewModel.history.HistorialVM

@Composable
fun ConfigApp(
    authVM: AuthVM,
    historialVM: HistorialVM,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val configVM: ConfigVM = viewModel(viewModelStoreOwner = LocalActivity.current as ComponentActivity)
    val configState by configVM.state.collectAsState()
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
        Cuenta(historialVM, authVM)
        Spacer(modifier = Modifier.height(16.dp))
    }
}
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


@Composable
fun Cuenta(historialVM: HistorialVM, authVM: AuthVM, modifier: Modifier = Modifier) {
    val authState by authVM.authState.collectAsState()
    val scrollState = rememberScrollState()
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
                .padding()
                .fillMaxWidth()
                .height(162.dp)
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.outline,
                    shape = MaterialTheme.shapes.medium
                )
                .verticalScroll(scrollState)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = authState.usuario?.correoU ?: "",
                    onValueChange = { },
                    label = { Text("Correo Electronico", style = MaterialTheme.typography.titleMedium) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = "",
                    onValueChange = { },
                    label = { Text("Contraseña", style = MaterialTheme.typography.titleMedium) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = authState.sub ?: "anonymous",
                    onValueChange = { },
                    label = { Text("SUB", style = MaterialTheme.typography.titleMedium) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        Image(
            painter = painterResource(R.drawable.out),
            contentDescription = "Cerrar Sesión",
            modifier = Modifier
                .size(50.dp)
                .clickable { authVM.signOut { historialVM.flush() } },
            contentScale = ContentScale.Crop,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant)
        )
        Text(
            text = "Cerrar Sesión",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ConfigPreview(authVM: AuthVM = AuthVM(), historialVM: HistorialVM = HistorialVM()) {
    ConfigApp(authVM, historialVM)
}