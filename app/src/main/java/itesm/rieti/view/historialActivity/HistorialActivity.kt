package itesm.rieti.view.historialActivity

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import itesm.rieti.R
import itesm.rieti.viewModel.auth.AuthState
import itesm.rieti.viewModel.history.HistorialState
import itesm.rieti.viewModel.history.HistorialVM
import itesm.rieti.viewModel.history.HistoryView

@Composable
fun HistorialActivity(
    authState: AuthState,
    historialVM: HistorialVM,
    historialState: HistorialState,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(historialState.reportes) {
        if (historialState.reportes.isEmpty()) {
            historialVM.cargarReportes(authState.usuario!!)
        }
    }

    if (historialState.selectedReporte == null) {
        MuestraHistorial(
            historialVM = historialVM,
            historialState = historialState,
            modifier = modifier
        )
    } else {
        DetallesReporteActivity(historialVM, historialState)
    }
}

@Composable
fun MuestraHistorial(
    historialVM: HistorialVM,
    historialState: HistorialState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(all = 14.dp)
            .fillMaxSize()
            .semantics { testTagsAsResourceId = true },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Historial",
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            modifier = Modifier.testTag("historyTitle")
        )
        Spacer(Modifier.height(12.dp))
        GrillaKPIs(historialState)
        Spacer(Modifier.height(12.dp))
        BarraToggle(historialVM, historialState)
        Spacer(Modifier.height(6.dp))
        when (historialState.selectedView) {
            HistoryView.REPORTES -> {
                if (historialState.reportes.isEmpty()) {
                    EmptyLayout("No hay reportes")
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(historialState.reportes) { reporte ->
                            Log.i("Reporte", "Reporte data: $reporte")
                            TarjetaReporte(historialVM, reporte)
                        }
                    }
                }
            }
            HistoryView.BORRADORES -> {
                if (historialState.borradores.isEmpty()) {
                    EmptyLayout("No hay borradores")
                } else {
                    LazyColumn {
                        items(historialState.borradores) { borrador ->
                            TarjetaBorrador(historialVM, borrador)
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun EmptyLayout(desc: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.no_records),
            contentDescription = desc,
            modifier = Modifier.height(200.dp).offset(x = 20.dp),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = desc,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun BarraToggle(
    historialVM: HistorialVM,
    historialState: HistorialState,
    modifier: Modifier = Modifier
) {
    SingleChoiceSegmentedButtonRow(modifier = modifier) {
        HistoryView.entries.forEachIndexed { numero, view ->
            SegmentedButton(
                selected = historialState.selectedView == view,
                onClick = { historialVM.setView(view) },
                shape = SegmentedButtonDefaults.itemShape(index = numero, count = HistoryView.entries.size)
            ) {
                Text(text = view.desc)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HistorialPreview(historialVM: HistorialVM = HistorialVM()) {
    HistorialActivity(
        authState = AuthState(),
        historialVM = historialVM,
        historialState = HistorialState()
    )
}

@Preview(showBackground = true)
@Composable
fun EmptyHeaderPreview() {
    EmptyLayout("No hay reportes")
}