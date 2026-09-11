package itesm.rieti.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import itesm.rieti.model.esquemas.Borrador
import itesm.rieti.view.mockupData.ReporteMockups
import itesm.rieti.model.esquemas.Reporte
import itesm.rieti.view.HistorialActivity.GrillaKPIs
import itesm.rieti.view.HistorialActivity.TarjetaBorrador
import itesm.rieti.view.HistorialActivity.TarjetaReporte
import itesm.rieti.view.mockupData.BorradorMockUps

@Composable
fun HistorialActivity(mockupReportes: List<Reporte>, mockupBorradores: List<Borrador>, onEditar: () -> Unit, modifier: Modifier = Modifier) {
    var reporteSeleccionado by remember { mutableStateOf<Reporte?>(null) }
    var opcionSeleccionada by remember { mutableStateOf(0) }
    val opciones = listOf("Reportes", "Borradores")
    if (reporteSeleccionado == null) {
        MuestraHistorial(mockupReportes, mockupBorradores, opciones, opcionSeleccionada, { opcionSeleccionada = it }, reporteSeleccionado, { reporteSeleccionado = it }, onEditar, modifier)
    } else {
        DetallesReporteActivity(onClose = { reporteSeleccionado = null }, reporte = reporteSeleccionado!!)
    }
}

@Composable
fun MuestraHistorial(mockupReportes: List<Reporte>, mockupBorradores: List<Borrador>, opciones: List<String>, opcionSeleccionada: Int, onOpcion: (Int) -> Unit, reporteSeleccionado: Reporte?, onReporte: (Reporte) -> Unit, onEditar: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(all = 14.dp).fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Historial",
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
        )
        Spacer(Modifier.height(12.dp))
        GrillaKPIs()
        Spacer(Modifier.height(12.dp))
        BarraToggle(opciones, opcionSeleccionada, { onOpcion(it) })
        Spacer(Modifier.height(6.dp))
        LazyColumn {
            when (opcionSeleccionada) {
                0 -> {
                    items(mockupReportes) { reporte ->
                        TarjetaReporte(reporte, onReporte)
                    }
                }
                1 -> {
                    items(mockupBorradores) { borrador ->
                        TarjetaBorrador(borrador, onEditar)
                    }
                }
            }
        }
    }
}

@Composable
fun BarraToggle(opciones: List<String>, seleccionado: Int, onClick: (Int) -> Unit, modifier: Modifier = Modifier) {
    SingleChoiceSegmentedButtonRow(modifier = modifier) {
        opciones.forEachIndexed { numero, etiqueta ->
            SegmentedButton(
                selected = seleccionado == numero,
                onClick = { onClick(numero) },
                shape = SegmentedButtonDefaults.itemShape(index = numero, count = opciones.size)
            ) {
                Text(text = etiqueta)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TarjetaReportePreview() {
    val mockupReportes: List<Reporte> = ReporteMockups().values.toList()
    val mockupBorradores: List<Borrador> = BorradorMockUps().values.toList()
    HistorialActivity(mockupReportes, mockupBorradores, {})
}