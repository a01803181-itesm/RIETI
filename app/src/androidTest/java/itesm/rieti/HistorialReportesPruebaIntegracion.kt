package itesm.rieti

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
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
        val targetReport = mockupReportes.first()

        composeTestRule.setContent {
            HistorialActivity(mockupReportes, mockupBorradores, {})
        }

        composeTestRule.onNodeWithTag(targetReport.folio)
            .assertHasClickAction()
            .performClick()

        composeTestRule.onNodeWithText("Detalles Reporte").assertIsDisplayed()
        composeTestRule.onNodeWithText("Folio: ${targetReport.folio}").assertIsDisplayed()
        composeTestRule.onNodeWithText(targetReport.descripcion).assertIsDisplayed()
        composeTestRule.onNodeWithText(targetReport.municipio.name).assertIsDisplayed()
    }
}
