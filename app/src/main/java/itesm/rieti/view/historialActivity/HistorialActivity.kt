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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import itesm.rieti.R
import itesm.rieti.viewModel.auth.AuthState
import itesm.rieti.viewModel.history.HistorialState
import itesm.rieti.viewModel.history.HistorialVM
import itesm.rieti.viewModel.history.HistoryView
import itesm.rieti.viewModel.history.HistorialStage
import itesm.rieti.viewModel.network.NetworkVM
import itesm.rieti.viewModel.nuevoReporte.NuevoReporteVM
import java.time.LocalDateTime

/**
 * Actividad principal del historial que determina si mostrar la lista de reportes
 * o los detalles de un reporte seleccionado.
 *
 * @param authState Estado de autenticación del usuario.
 * @param historialVM ViewModel del historial.
 * @param historialState Estado del historial.
 * @param modifier Modificador para la vista.
 */
@Composable
fun HistorialActivity(
    navController: NavHostController,
    nuevoReporteVM: NuevoReporteVM,
    authState: AuthState,
    historialVM: HistorialVM,
    historialState: HistorialState,
    modifier: Modifier = Modifier,
    networkVM: NetworkVM = NetworkVM(LocalContext.current)
) {
    val networkConnectionState by networkVM.isNetworkAvailable.collectAsState()

    BackHandler(enabled = historialState.selectedReporte != null) {
        historialVM.setReporteSeleccionado(null)
    }

    LaunchedEffect(historialState.reportes, authState.usuario) {
        if (historialState.reportes.isEmpty() && authState.usuario != null) {
            historialVM.cargarReportes(authState.usuario)
        }
    }
    Log.i("Detalles", "Selected reporte: $historialState.selectedReporte")
    if (historialState.selectedReporte == null) {
        MuestraHistorial(
            navController = navController,
            nuevoReporteVM = nuevoReporteVM,
            networkConnection = networkConnectionState,
            historialVM = historialVM,
            historialState = historialState,
            modifier = modifier
        )
    } else {
        Log.i("Detalles", "Calling DetallesReporteActivity function...")
        DetallesReporteActivity(
            historialVM = historialVM,
            historialState = historialState,
            reporteResponse = historialState.selectedReporte,
            modifier = modifier
        )
    }
}

/**
 * Componente que muestra la lista principal del historial, incluyendo KPIs,
 * botones de alternancia y la lista de reportes o borradores.
 *
 * @param historialVM ViewModel del historial.
 * @param historialState Estado del historial.
 * @param modifier Modificador para la vista.
 */
@Composable
fun MuestraHistorial(
    navController: NavHostController,
    nuevoReporteVM: NuevoReporteVM,
    networkConnection: Boolean,
    historialVM: HistorialVM,
    historialState: HistorialState,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(
        historialState.selectedView,
        networkConnection,
        historialState.reportes,
        historialState.borradores,
        historialState.stage
    ) {
        when (historialState.selectedView) {
            HistoryView.REPORTES -> {
                if (!networkConnection) {
                    if (historialState.stage != HistorialStage.OFFLINE) {
                        historialVM.setLoadingState(HistorialStage.OFFLINE)
                    }
                } else if (historialState.stage == HistorialStage.LOADING_REPORTS || historialState.stage == HistorialStage.ERROR_IN_REPORTS) {
                    // Retain loading or error stage while async task runs or fails
                } else if (historialState.reportes.isEmpty()) {
                    if (historialState.stage != HistorialStage.EMPTY_REPORTS) {
                        historialVM.setLoadingState(HistorialStage.EMPTY_REPORTS)
                    }
                } else {
                    if (historialState.stage != HistorialStage.NON_EMPTY_REPORTS) {
                        historialVM.setLoadingState(HistorialStage.NON_EMPTY_REPORTS)
                    }
                }
            }
            HistoryView.BORRADORES -> {
                if (historialState.borradores.isEmpty()) {
                    if (historialState.stage != HistorialStage.EMPTY_DRAFTS) {
                        historialVM.setLoadingState(HistorialStage.EMPTY_DRAFTS)
                    }
                } else {
                    if (historialState.stage != HistorialStage.NON_EMPTY_DRAFTS) {
                        historialVM.setLoadingState(HistorialStage.NON_EMPTY_DRAFTS)
                    }
                }
            }
        }
    }

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
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.testTag("historyTitle")
        )
        Spacer(Modifier.height(12.dp))
        GrillaKPIs(historialState)
        Spacer(Modifier.height(12.dp))
        BarraToggle(historialVM, historialState)
        Spacer(Modifier.height(6.dp))
        ReportesContent(
            nuevoReporteVM,
            navController,
            historialVM,
            historialState
        )
    }
}
/**
 * Pantalla que se muestra cuando no hay elementos en la lista (vacía).
 *
 * @param desc Descripción a mostrar cuando no hay elementos.
 * @param modifier Modificador para la vista.
 */
@Composable
fun ReportesContent(
    nuevoReporteVM: NuevoReporteVM,
    navController: NavHostController,
    historialVM: HistorialVM,
    historialState: HistorialState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (historialState.stage) {
            HistorialStage.LOADING_REPORTS -> {
                Log.i("ReporteStage", "Loading")
                CircularProgressIndicator(modifier = modifier.size(200.dp))
                Text(
                    text = "Cargando datos...",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
            }
            HistorialStage.ERROR_IN_REPORTS -> {
                Log.i("ReporteStage", "Error")
                Image(
                    painter = painterResource(R.drawable.server_error),
                    contentDescription = "Error de conexión",
                    modifier = Modifier.size(200.dp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant)
                )
                Text(
                    text = "Error de conexión",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
            }
            HistorialStage.EMPTY_REPORTS -> {
                Log.i("ReporteStage", "Empty")
                Image(
                    painter = painterResource(R.drawable.no_records),
                    contentDescription = "Sin datos",
                    modifier = Modifier.height(200.dp).offset(x = 20.dp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Aún sin reportes",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
            }
            HistorialStage.OFFLINE -> {
                if (historialState.reportes.isEmpty()) {
                    Log.i("ReporteStage", "Offline")
                    Image(
                        painter = painterResource(R.drawable.offline),
                        contentDescription = "Sin conexión",
                        modifier = Modifier.size(200.dp),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                    Text(
                        text = "Sin conexión",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(historialState.reportes.sortedBy{ LocalDateTime.parse(it.reporte.dia) }.asReversed()) { reporte ->
                            Log.i("ReporteStage", "Reporte data: $reporte")
                            TarjetaReporte(historialVM, reporte)
                        }
                    }
                }
            }
            HistorialStage.NON_EMPTY_REPORTS -> {
                Log.i("ReporteStage", "Reporte data: ${historialState.reportes}")
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(historialState.reportes.sortedBy{ LocalDateTime.parse(it.reporte.dia) }.asReversed()) { reporte ->
                        Log.i("ReporteStage", "Reporte data: $reporte")
                        TarjetaReporte(historialVM, reporte)
                    }
                }
            }
            HistorialStage.NON_EMPTY_DRAFTS -> {
                Log.i("ReporteStage", "Borrador data: ${historialState.borradores}")
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(historialState.borradores.sortedBy{ it.date }.asReversed()) { borrador ->
                        TarjetaBorrador(
                            navController,
                            nuevoReporteVM,
                            historialVM,
                            borrador,
                            modifier,
                        )
                    }
                }
            }
            HistorialStage.EMPTY_DRAFTS -> {
                Image(
                    painter = painterResource(R.drawable.no_records),
                    contentDescription = "Sin datos",
                    modifier = Modifier.height(200.dp).offset(x = 20.dp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Aún sin borradores",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

/**
 * Barra de botones para alternar entre las vistas de reportes y borradores.
 *
 * @param historialVM ViewModel del historial.
 * @param historialState Estado del historial.
 * @param modifier Modificador para la vista.
 */
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

/**
 * Vista previa de la pantalla del historial.
 *
 * @param historialVM ViewModel del historial.
 */
@Preview(showBackground = true)
@Composable
fun HistorialPreview(
    navController: NavHostController = rememberNavController(),
    historialVM: HistorialVM = viewModel(),
    nuevoReporteVM: NuevoReporteVM = NuevoReporteVM()
) {
    HistorialActivity(
        navController = navController,
        nuevoReporteVM = nuevoReporteVM,
        authState = AuthState(),
        historialVM = historialVM,
        historialState = HistorialState()
    )
}

/**
 * Vista previa de la pantalla vacía de historial.
 */
@Preview(showBackground = true)
@Composable
fun PlaceholderHeaderPreview(
    navController: NavHostController = rememberNavController(),
    historialVM: HistorialVM = viewModel(),
    historialState: HistorialState = HistorialState(),
    nuevoReporteVM: NuevoReporteVM = NuevoReporteVM()
) {
    ReportesContent(
        nuevoReporteVM,
        navController,
        historialVM,
        historialState
    )
}