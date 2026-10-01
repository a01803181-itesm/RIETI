package itesm.rieti

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import itesm.rieti.model.esquemas.Borrador
import itesm.rieti.model.esquemas.Reporte
import itesm.rieti.view.HistorialActivity
import itesm.rieti.view.mockupData.BorradorMockUps
import itesm.rieti.view.mockupData.ReporteMockups
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HistorialReportesPruebaIntegracion {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun selectingAReportFromHistoryList() {
        val mockupReportes: List<Reporte> = ReporteMockups().values.toList()
        val mockupBorradores: List<Borrador> = BorradorMockUps().values.toList()

        composeTestRule.setContent {
            HistorialActivity(mockupReportes, mockupBorradores, {})
        }

        composeTestRule.onNodeWithTag(
            "20260906ATZL87",
            useUnmergedTree = false
        ).assertHasClickAction()
    }
}