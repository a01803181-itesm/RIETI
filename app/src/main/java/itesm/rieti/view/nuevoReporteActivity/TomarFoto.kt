package itesm.rieti.view.nuevoReporteActivity

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil3.compose.AsyncImage
import java.io.File

/**
 * Crea un URI temporal para guardar una imagen capturada por la cámara.
 *
 * @param context Contexto de la aplicación.
 * @return Un Uri apuntando al archivo de imagen temporal.
 */
fun createTempImageURI(context: Context): Uri {
    val tempFileDir = File(context.cacheDir, "images").apply { mkdirs() }
    val tempFile = File.createTempFile("evidencia_", ".jpg", tempFileDir)
    return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", tempFile)
}

/**
 * Componente visual que permite capturar una foto o visualizar la foto ya tomada.
 *
 * @param fotoUri URI de la foto capturada, o null si no se ha tomado.
 * @param onFotoCaptured Callback ejecutado cuando se toma una foto exitosamente.
 * @param modifier Modificador para la vista.
 */
@Composable
fun CameraCaptureField(
    fotoUri: Uri?,
    onFotoCaptured: (Uri) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var pendingUri by remember { mutableStateOf<Uri?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess && pendingUri != null) {
            onFotoCaptured(pendingUri!!)
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable {
                val newUri = createTempImageURI(context)
                pendingUri = newUri
                cameraLauncher.launch(newUri)
            }
    ) {
        if (fotoUri != null) {
            AsyncImage(
                model = fotoUri,
                contentDescription = "Evidencia del reporte",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Tomar foto",
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Tomar una foto como evidencia",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}