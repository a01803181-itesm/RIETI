package itesm.rieti.view.nuevoReporteActivity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import itesm.rieti.model.preferences.UserPreferences
import itesm.rieti.viewModel.auth.AuthState
import itesm.rieti.viewModel.nuevoReporte.DraftDeletionStage
import itesm.rieti.viewModel.nuevoReporte.NuevoReporteState
import itesm.rieti.viewModel.nuevoReporte.NuevoReporteVM
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun AnonymousDialog(
    preferences: UserPreferences,
    authState: AuthState,
    nuevoReporteVM: NuevoReporteVM
) {
    val rememberCoroutineScope = rememberCoroutineScope()
    AlertDialog(
        modifier = Modifier
            .padding(16.dp)
            .wrapContentSize(),
        onDismissRequest = { nuevoReporteVM.setDisplayAnonymousDialog(false) },
        confirmButton = {
            Button(
                onClick = {
                    nuevoReporteVM.setDisplayAnonymousDialog(false)
                    rememberCoroutineScope.launch { preferences.saveAwareOfAnonymous(true) }
                    nuevoReporteVM.crearReporte(authState.usuario)
                }
            ) {
                Text(
                    text = "Aceptar",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    nuevoReporteVM.setDisplayAnonymousDialog(false)
                }
            ) {
                Text(
                    text = "Cancelar",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        text = {
            Text(
                text = "El reporte será enviado como anónimo.\n" +
                        "Considera que si envias el reporte como anónimo " +
                        "no podrás verlo reflejado en tu historial como usuario.",
                textAlign = TextAlign.Justify
            )
        },
        title = {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                WarningLabel()
                Text(
                    text = "Reporte Anónimo",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 8.dp),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    )
}

@Composable
fun DeleteDraftDialog(
    nuevoReporteVM: NuevoReporteVM,
    nuevoReporteState: NuevoReporteState,
    onBackFromBorrador: () -> Unit
) {
    LaunchedEffect(nuevoReporteState.draftDeletionStage) {
        if (nuevoReporteState.draftDeletionStage == DraftDeletionStage.SUCCESS) {
            delay(750.milliseconds)
            nuevoReporteVM.setDisplayConfirmDraftDeletion(false)
            nuevoReporteVM.resetForm()
            nuevoReporteVM.setSelectedBorrador(null)
            nuevoReporteVM.setDraftDeletionStage(DraftDeletionStage.IDLE)
            onBackFromBorrador()
        }
    }
    AlertDialog(
        onDismissRequest = { nuevoReporteVM.setDisplayConfirmDraftDeletion(false) },
        dismissButton = {
            if (nuevoReporteState.draftDeletionStage != DraftDeletionStage.SUCCESS) {
                Button(
                    onClick = { nuevoReporteVM.setDisplayConfirmDraftDeletion(false) },
                    enabled = nuevoReporteState.draftDeletionStage == DraftDeletionStage.IDLE
                ) {
                    Text(
                        text = "Cancelar",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        },
        confirmButton = {
            if (nuevoReporteState.draftDeletionStage != DraftDeletionStage.SUCCESS) {
                Button(
                    onClick = { nuevoReporteVM.borrarBorrador() },
                    enabled = nuevoReporteState.draftDeletionStage == DraftDeletionStage.IDLE
                ) {
                    if (nuevoReporteState.draftDeletionStage == DraftDeletionStage.LOADING) {
                        CircularProgressIndicator(modifier = Modifier.size(16.dp))
                    }
                    Text(
                        text = when (nuevoReporteState.draftDeletionStage) {
                            DraftDeletionStage.IDLE -> "Confirmar"
                            DraftDeletionStage.LOADING -> "Eliminando..."
                            DraftDeletionStage.SUCCESS -> "Eliminado!"
                        },
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        },
        title = {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                var description: String
                when (nuevoReporteState.draftDeletionStage) {
                    DraftDeletionStage.IDLE -> {
                        WarningLabel()
                        description = "Eliminar Borrador"
                    }
                    DraftDeletionStage.LOADING -> {
                        LoadingLabel()
                        description = "Eliminar Borrador"
                    }
                    DraftDeletionStage.SUCCESS -> {
                        SuccessLabel()
                        description = "Borrador eliminado"
                    }
                }
                Text(
                    text = description,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 8.dp),
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        text = {
            Text(
                text = when (nuevoReporteState.draftDeletionStage) {
                    DraftDeletionStage.IDLE -> "¿Estás seguro de que quieres eliminar este borrador?"
                    DraftDeletionStage.LOADING -> "Eliminando borrador..."
                    DraftDeletionStage.SUCCESS -> "¡El borrador ha sido eliminado!"
                },
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    )
}

@Composable
fun DetallesHelpDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = { },
        title = {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                InfoLabel()
                Text(
                    text = "Observaciones",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        text = {
            Text(
                text = "Aquí puedes incorporar detalles adicionales sobre el reporte, casos específicos o brindar puntos de referencia del lugar",
                textAlign = TextAlign.Justify,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth()
            )
        }
    )
}

@Composable
fun NombreCompletoHelpDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = { },
        title = {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                InfoLabel()
                Text(
                    text = "Nombre Completo (o Anónimo)",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        text = {
            Text(
                text = "En RIETI, nos preocupamos por la privacidad de los datos de nuestros usuarios;" +
                        " por ello, nuestra aplicación solo guarda su correo electrónico." +
                        "\n\n" +
                        "Tiene la libertad de decisión de mandar el reporte de manera anónima" +
                        " (dejar este campo vacío) o, en su caso, enviarlo con su nombre completo.",
                textAlign = TextAlign.Justify,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth()
            )
        }
    )
}