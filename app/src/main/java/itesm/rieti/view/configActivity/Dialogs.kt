package itesm.rieti.view.configActivity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import itesm.rieti.view.nuevoReporteActivity.ErrorLabel
import itesm.rieti.view.nuevoReporteActivity.SuccessLabel
import itesm.rieti.view.nuevoReporteActivity.WarningLabel
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun ChangePasswordUnavailableDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = { },
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                ErrorLabel()
                Text(
                    text = "Cambiar Contraseña",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 8.dp),
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        text = {
            Text(
                text = "Actualmente estás en modo offline, por lo que no es posible cambiar tu contraseña.\nConéctese a internet para continuar.",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge
            )
        },
    )
}

@Composable
fun PasswordChangedDialog() {
    AlertDialog(
        onDismissRequest = { },
        confirmButton = { },
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                SuccessLabel()
                Text(
                    text = "Contraseña cambiada",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 8.dp),
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        text = {
            Text(
                text = "Su contraseña ha sido cambiada exitosamente" +
                        "\n\n" +
                        "Regresando a la pantalla de inicio de sesión...",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge
            )
        },
    )
}

@Composable
fun ConfirmSignOutDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    var accepted by remember { mutableStateOf(false) }

    LaunchedEffect(accepted) {
        if (accepted) {
            delay(1000.milliseconds)
            onConfirm()
        }
    }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        dismissButton = {
            if (!accepted) {
                Button(
                    onClick = { onDismiss() },
                ) {
                    Text(
                        text = "Cancelar",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        },
        confirmButton = {
            if (!accepted) {
                Button(
                    onClick = { accepted = true },
                ) {
                    Text(
                        text = "Confirmar",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        },
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                if (!accepted) WarningLabel() else SuccessLabel()
                Text(
                    text = "Cerrar sesión",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 8.dp),
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        text = {
            Text(
                text = if (!accepted) "¿Estás segur@ de que quieres cerrar sesión?" else "Sesión cerrada exitosamente.\n¡Hasta pronto!",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth()
            )
        },
    )
}