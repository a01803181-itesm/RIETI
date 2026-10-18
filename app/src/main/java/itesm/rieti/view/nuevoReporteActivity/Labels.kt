package itesm.rieti.view.nuevoReporteActivity

import android.graphics.Color.rgb
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import itesm.rieti.R
import itesm.rieti.viewModel.nuevoReporte.NuevoReporteVM


/**
 * Componente que muestra una alerta indicando que el dispositivo está sin conexión (modo offline).
 */
@Composable
fun OfflineLabel() {
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
fun DraftLabel(nuevoReporteVM: NuevoReporteVM, onBackFromBorrador: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(
            onClick = {
                nuevoReporteVM.setSelectedBorrador(null)
                nuevoReporteVM.resetForm()
                onBackFromBorrador()
            }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver al historial"
            )
        }
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Nuevo Reporte",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "(Restaurado desde borrador)",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic),
                modifier = Modifier.fillMaxWidth()
            )
        }
        IconButton(
            onClick = { nuevoReporteVM.setDisplayConfirmDraftDeletion(true) },
            shape = RectangleShape,
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.trash),
                contentDescription = "Eliminar borrador",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun InfoLabel() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            painter = painterResource(R.drawable.info),
            contentDescription = "Información",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = "Información",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(start = 8.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun WarningLabel() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            painter = painterResource(R.drawable.warning),
            contentDescription = "Advertencia",
            tint = Color(rgb(255, 192, 72)),
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = "Advertencia",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(start = 8.dp),
            color = Color(rgb(255, 192, 72))
        )
    }
}

@Composable
fun ErrorLabel() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            painter = painterResource(R.drawable.cross),
            contentDescription = "Error",
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = "Error",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(start = 8.dp),
            color = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
fun LoadingLabel() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            painter = painterResource(R.drawable.elipsis),
            contentDescription = "Cargando",
            tint = Color(rgb(30, 121, 164)),
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = "Cargando",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(start = 8.dp),
            color = Color(rgb(30, 121, 164))
        )
    }
}

@Composable
fun SuccessLabel() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            painter = painterResource(R.drawable.check),
            contentDescription = "Operación exitosa",
            tint = Color(rgb(84, 143, 88)),
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = "Operación exitosa",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(start = 8.dp),
            color = Color(rgb(84, 143, 88))
        )
    }
}