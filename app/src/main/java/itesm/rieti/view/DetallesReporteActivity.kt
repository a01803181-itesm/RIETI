package itesm.rieti.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import itesm.rieti.R
import itesm.rieti.model.esquemas.Expediente
import itesm.rieti.model.esquemas.Reporte
import itesm.rieti.view.mockupData.ReporteMockups

@Composable
fun DetallesReporteActivity(onClose: () -> Unit, reporte: Reporte, modifier: Modifier = Modifier) {
    val estadoScroll = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(
                onClick = { onClose() },
                modifier = modifier.weight(1f).size(25.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.cross),
                    contentDescription = "Go back"
                )
            }
            Text(
                text = "Detalles Reporte",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = modifier.weight(3f),
                textAlign = TextAlign.Center
            )
            IconButton(
                onClick = { },
                modifier = modifier.weight(1f).size(25.dp),
                enabled = reporte.expediente == null
            ) {
                Icon(
                    painter = painterResource(R.drawable.pencil),
                    contentDescription = "Edit Report Details"
                )
            }
        }
        Spacer(modifier.height(14.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Folio: ${reporte.folioReporte}",
                fontWeight = FontWeight.Light,
                modifier = modifier.padding(end = 8.dp),
                textAlign = TextAlign.Center
            )
            IconButton(
                onClick = {},
                modifier = modifier.size(15.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.copy),
                    contentDescription = "Copy to clipboard",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        Column(
            modifier = modifier.verticalScroll(estadoScroll)
        ) {
            Spacer(modifier.height(14.dp))
            Text(
                text = reporte.descripcion,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
            )
            Spacer(modifier.height(14.dp))
            ElementoLista("Fecha:", reporte.fechaYHora.toString())
            ElementoLista("Número de NNA:", reporte.numeroNNA.toString())
            ElementoLista("Rango de edad:", reporte.rangoEdad)
            ElementoLista("Tipo de trabajo:", reporte.tipoTrabajo.name)
            if (reporte.expediente != null) DetallesExpediente(reporte.expediente)
            Spacer(modifier.height(14.dp))
            Text(
                text = "Ubicación",
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
            )
            Text(
                text = reporte.municipio.name,
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp
            )
            Spacer(modifier.height(8.dp))
            Image(
                painter = painterResource(R.drawable.map_preview),
                contentDescription = "Static Google Maps preview",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
fun DetallesExpediente(expediente: Expediente, modifier: Modifier = Modifier) {
    Spacer(modifier.height(14.dp))
    Text(
        text = "Detalles Expediente",
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp
    )
    ElementoLista("Folio Expediente:", expediente.folioExpediente)
    ElementoLista("Alimentador:", expediente.alimentador)
    ElementoLista("Status:", expediente.status.name)
    if (expediente.descripcion != null) ElementoLista("Descripción:", expediente.descripcion)
    ElementoLista("Última Actualización:", expediente.ultimaActualizacion.toString())
}

@Composable
fun ElementoLista(etiqueta: String, valor: String, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(
            text = etiqueta,
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp,
            modifier = modifier.weight(1f)
        )
        Text(
            text = valor,
            fontWeight = FontWeight.Normal,
            fontSize = 18.sp,
            modifier = modifier.weight(2f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DetallesReportePreview() {
    val reporte: Reporte = ReporteMockups().values.toList()[0]
    DetallesReporteActivity({}, reporte)
}