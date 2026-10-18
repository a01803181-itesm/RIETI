package itesm.rieti.view.acercaDeActivity

import android.content.ClipData
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import itesm.rieti.R
import itesm.rieti.view.inicioActivity.Encabezado
import androidx.core.net.toUri
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import kotlinx.coroutines.launch

/**
 * Pantalla de la aplicación que muestra la información de Acerca De.
 * Muestra un encabezado y un cuerpo de texto con información sobre SIPINNA y RIETI.
 *
 * @param modifier Modificador para la vista.
 */
@Composable
fun AcercaDeApp(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Encabezado()
        Spacer(modifier = Modifier.height(24.dp))
        CuerpoAcercaDeApp()
    }
}

/**
 * Componente que muestra el texto descriptivo de la pantalla Acerca De.
 *
 * @param modifier Modificador para la vista.
 */
@Composable
fun CuerpoAcercaDeApp(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        Text(
            text = "La SIPINNA es el Sistema Nacional de Protección Integral de Niñas, Niños y Adolescentes, fue mandatado por la Ley General de Derechos de Niñas, Niños y Adolescentes de 2014 a efecto de que el Estado (en sus tres órdenes de gobierno) cumpla con su responsabilidad de garantizar la protección, prevención y restitución integrales de los derechos humanos de niñas, niños y adolescentes que hayan sido vulnerados.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Justify,
            fontWeight = FontWeight.Light,
        )
        Spacer(modifier = Modifier.height(50.dp))
        Text(
            text = "REITI es una forma de estrategia colaborativa de  diversos municipios mexiquenses para conjuntar esfuerzos institucionales para la detección y registro de casos de trabajo infantil y proteger los derechos de la niñez.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Justify,
            fontWeight = FontWeight.Light
        )
        Spacer(modifier = Modifier.height(50.dp))
        Text(
            text = "Glosario de Términos en la aplicación",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Left,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "NNA: Niños, Niñas y Adolescentes." +
            "\nReportes en seguimiento: son reportes con los que RIETI ya se encuentra revisando." +
            "\nReportes completados: son reportes con los que RIETI ya dio solución y ha marcado como cerrados." +
            "\nReportes pendientes: son reportes con los que RIETI aún no ha revisado su contenido y tampoco ha comenzado su seguimiento (investigación)",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Justify,
            fontWeight = FontWeight.Light
        )
        Spacer(modifier = Modifier.height(50.dp))
        SocialMedia(modifier)
        Spacer(modifier = Modifier.height(50.dp))
        AboutDevTeam()
    }
}

@Composable
fun SocialMedia(modifier: Modifier = Modifier, context: Context = LocalContext.current) {
    Text(
        text = "¡Sigue a RIETI y SIPINNA en sus Redes Sociales!",
        style = MaterialTheme.typography.titleLarge,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold,
        modifier = modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(30.dp))
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        SocialMedium(
            R.drawable.instagram,
            "Instagram",
            "sipinnaatizapan\ndezaragoza",
            {
                val appIntent = Intent(
                    Intent.ACTION_VIEW,
                    "instagram://user?username=sipinnaatizapandezaragoza".toUri()
                )
                if (appIntent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(appIntent)
                } else {
                    context.startActivity(Intent(Intent.ACTION_VIEW, "https://www.instagram.com/sipinnaatizapandezaragoza".toUri()))
                }
            }
        )
        SocialMedium(
            R.drawable.facebook,
            "Facebook",
            "SIPINNAMX",
            {
                val appIntent = Intent(
                    Intent.ACTION_VIEW,
                    "fb://facewebmodal/f?href=https://facebook.com/SIPINNAMX".toUri()
                )
                if (appIntent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(appIntent)
                } else {
                    context.startActivity(Intent(Intent.ACTION_VIEW, "https://www.facebook.com/SIPINNAMX".toUri()))
                }
            }
        )
        SocialMedium(
            R.drawable.tiktok,
            "TikTok",
            "@sipinna_mx",
            {
            val appIntent = Intent(
                Intent.ACTION_VIEW,
                "https://www.tiktok.com/@sipinna_mx".toUri()
            )
            context.startActivity(appIntent)
        })
    }
}

@Composable
fun SocialMedium(
    resource: Int,
    desc: String,
    account: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Image(
            painter = painterResource(resource),
            contentDescription = desc,
            modifier = Modifier
                .size(50.dp)
                .clickable(
                onClick = { onClick() }
            ),
            contentScale = ContentScale.Crop,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant),
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = account,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun AboutDevTeam() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "¡Conoce al equipo de desarrolladores!",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(32.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth()
        ) {
            TeamMember(
                R.drawable.dev_cesar,
                "César Ariel Rodríguez Sandoval",
                "Mobile App developer\nUI/UX designer\nBackend developer",
                "A01802924@tec.mx",
                Modifier.weight(1f)
            )
            TeamMember(
                R.drawable.dev_cass,
                "Cassandra Vanessa Padilla Barovier",
                "Backend developer\nCloud deployment engineer\nDB architect",
                "A01753547@tec.mx",
                Modifier.weight(1f)
            )
            TeamMember(
                R.drawable.dev_alex,
                "Alexander Mejia Tovar",
                "Mobile App developer\nUI/UX designer\nCybersecurity engineer",
                "A01803181@tec.mx",
                Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            TeamMember(
                R.drawable.dev_santi,
                "Santiago Abraham Ríos Palacios",
                "Web developer\nBackend developer\nDB architect",
                "A01802727@tec.mx",
                Modifier.weight(1f)
            )
            TeamMember(
                R.drawable.dev_gian,
                "Giancarlo Moreno Vázquez",
                "Web developer\nBackend developer\nUI/UX designer",
                "A01803172@tec.mx",
                Modifier.weight(1f)
            )
        }
        Image(
            painter = painterResource(R.drawable.itesm_logo),
            contentDescription = "Logo de ITESM",
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            contentScale = ContentScale.Crop
        )
        Text(
            text = "Esta aplicación se hizo por alumnos del Tecnológico de Monterrey para RIETI",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Justify,
            fontWeight = FontWeight.Light,
        )
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun TeamMember(
    photoID: Int,
    fullName: String,
    role: String,
    email: String,
    modifier: Modifier = Modifier
) {
    var showFurtherInfoDialog by remember { mutableStateOf(false) }

    if (showFurtherInfoDialog) {
        TeamMemberFurtherInfoDialog(
            photoID,
            fullName,
            role,
            email,
            { showFurtherInfoDialog = false },
            modifier
        )
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Image(
            painter = painterResource(photoID),
            contentDescription = "Foto de $fullName",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .clickable(
                    onClick = { showFurtherInfoDialog = true }
                ),
            contentScale = ContentScale.Crop
        )
        Text(
            text = fullName,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(modifier = Modifier.height(8.dp))
        EmailText(email, MaterialTheme.typography.labelSmall.copy(fontStyle = FontStyle.Italic))
    }
}

@Composable
fun EmailText(email: String, style: TextStyle) {
    val clipboard = LocalClipboard.current
    val scope = rememberCoroutineScope()
    Text(
        text = buildAnnotatedString {
            withStyle(
                SpanStyle(
                    color = MaterialTheme.colorScheme.primary,
                    textDecoration = TextDecoration.Underline
                )
            ) {
                append(email)
            }
        },
        style = style,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Light,
        modifier = Modifier
            .clickable(
                onClick = {
                    scope.launch {
                        clipboard.setClipEntry(ClipEntry(ClipData.newPlainText("Correo copiado ($email)", email)))
                    }
                }
            )
    )
}

@Composable
fun TeamMemberFurtherInfoDialog(
    photoID: Int,
    fullName: String,
    role: String,
    email: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        modifier = modifier.wrapContentSize(),
        onDismissRequest = { onDismiss() },
        confirmButton = {},
        title = {
            Text(
                text = fullName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(photoID),
                    contentDescription = "Foto de $fullName",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = role,
                        style = MaterialTheme.typography.bodySmall.copy(fontStyle = FontStyle.Italic),
                        fontWeight = FontWeight.SemiBold,
                    )
                    EmailText(email, MaterialTheme.typography.bodySmall.copy(fontStyle = FontStyle.Italic))
                }
            }
        },
    )
}

/**
 * Vista previa de la pantalla Acerca De.
 */
@Preview(showBackground = true)
@Composable
fun Preview() {
    AcercaDeApp()
}