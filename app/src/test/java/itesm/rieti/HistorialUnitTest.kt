package itesm.rieti

import itesm.rieti.model.drafts.Borrador
import itesm.rieti.model.enums.RangoEdad
import itesm.rieti.model.enums.TipoTrabajo
import itesm.rieti.model.esquemas.Reporte
import itesm.rieti.model.esquemas.ReporteResponse
import itesm.rieti.viewModel.history.HistorialStage
import itesm.rieti.viewModel.history.HistorialState
import itesm.rieti.viewModel.history.HistorialVM
import itesm.rieti.viewModel.history.HistoryView
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class HistorialUnitTest {

    private lateinit var viewModel: HistorialVM

    @Before
    fun setUp() {
        viewModel = HistorialVM()
    }

    @Test
    fun `estado inicial de HistorialState tiene listas vacias y vista REPORTES por defecto`() {
        val state = HistorialState()
        assertTrue(state.reportes.isEmpty())
        assertTrue(state.borradores.isEmpty())
        assertNull(state.selectedReporte)
        assertNull(state.selectedBorrador)
        assertNull(state.errorReportes)
        assertNull(state.errorBorradores)
        assertEquals(HistoryView.REPORTES, state.selectedView)
        assertEquals(HistorialStage.NON_EMPTY_REPORTS, state.stage)
    }

    @Test
    fun `setView cambia la vista seleccionada entre REPORTES y BORRADORES`() {
        viewModel.setView(HistoryView.BORRADORES)
        assertEquals(HistoryView.BORRADORES, viewModel.state.value.selectedView)

        viewModel.setView(HistoryView.REPORTES)
        assertEquals(HistoryView.REPORTES, viewModel.state.value.selectedView)
    }

    @Test
    fun `setLoadingState actualiza la etapa del historial correctamente`() {
        viewModel.setLoadingState(HistorialStage.LOADING_REPORTS)
        assertEquals(HistorialStage.LOADING_REPORTS, viewModel.state.value.stage)

        viewModel.setLoadingState(HistorialStage.EMPTY_REPORTS)
        assertEquals(HistorialStage.EMPTY_REPORTS, viewModel.state.value.stage)

        viewModel.setLoadingState(HistorialStage.OFFLINE)
        assertEquals(HistorialStage.OFFLINE, viewModel.state.value.stage)
    }

    @Test
    fun `setReporteSeleccionado asigna y limpia el reporte seleccionado para ver detalle`() {
        val reporteResponse = ReporteResponse(
            reporte = Reporte(
                folio = "REP-2026-001",
                numNinios = 2,
                rangoEdad = RangoEdad.INFANTES,
                tipoTrabajo = TipoTrabajo.VENTA_AMBULANTE,
                municipio = "Monterrey"
            )
        )

        viewModel.setReporteSeleccionado(reporteResponse)
        assertEquals(reporteResponse, viewModel.state.value.selectedReporte)
        assertEquals("REP-2026-001", viewModel.state.value.selectedReporte?.reporte?.folio)

        viewModel.setReporteSeleccionado(null)
        assertNull(viewModel.state.value.selectedReporte)
    }

    @Test
    fun `setBorradorSeleccionado asigna y limpia el borrador seleccionado para ver detalle`() {
        val borrador = Borrador(
            idBorrador = 5,
            correoU = "user@test.com",
            municipio = "Guadalajara",
            numNinios = 3
        )

        viewModel.setBorradorSeleccionado(borrador)
        assertEquals(borrador, viewModel.state.value.selectedBorrador)
        assertEquals(5, viewModel.state.value.selectedBorrador?.idBorrador)

        viewModel.setBorradorSeleccionado(null)
        assertNull(viewModel.state.value.selectedBorrador)
    }

    @Test
    fun `flush reinicia el estado de HistorialVM a sus valores iniciales por defecto`() {
        viewModel.setView(HistoryView.BORRADORES)
        viewModel.setLoadingState(HistorialStage.ERROR_IN_REPORTS)
        viewModel.setReporteSeleccionado(ReporteResponse(reporte = Reporte(folio = "FOLIO-123")))

        viewModel.flush()

        val state = viewModel.state.value
        assertTrue(state.reportes.isEmpty())
        assertNull(state.selectedReporte)
        assertEquals(HistoryView.REPORTES, state.selectedView)
        assertEquals(HistorialStage.NON_EMPTY_REPORTS, state.stage)
    }

    @Test
    fun `descripciones de HistoryView coinciden con las etiquetas en espanol`() {
        assertEquals("Reportes", HistoryView.REPORTES.desc)
        assertEquals("Borradores", HistoryView.BORRADORES.desc)
    }
}
