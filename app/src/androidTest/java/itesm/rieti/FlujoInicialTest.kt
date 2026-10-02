package itesm.rieti

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import itesm.rieti.ui.theme.RIETITheme
import itesm.rieti.view.Pantalla
import itesm.rieti.view.RIETIApp
import itesm.rieti.view.RegistroApp
import itesm.rieti.viewModel.api.UsuariosVM
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FlujoInicialTest
{
    @get:Rule
    val composeTestRule = createComposeRule()

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun logInAndNavigateToHistory() {
        val email = "casita@gmail.com"
        val password = "123456"
        val usuariosVM = UsuariosVM()

        composeTestRule.setContent {
            val login by usuariosVM.login.collectAsState()
            RIETITheme {
                if (!login) {
                    RegistroApp(usuariosVM = usuariosVM)
                } else {
                    RIETIApp(usuariosVM = usuariosVM)
                }
            }
        }

        composeTestRule.onNodeWithTag("emailTextField")
            .performTextInput(email)

        composeTestRule.onNodeWithTag("passwordTextField")
            .performTextInput(password)

        composeTestRule.onNodeWithTag("localLoginBtn")
            .performClick()

        composeTestRule.waitUntilAtLeastOneExists(
            matcher = hasTestTag(Pantalla.listaPantallas[2].etiqueta),
            timeoutMillis = 5000
        )

        composeTestRule.onNodeWithTag(Pantalla.listaPantallas[2].etiqueta)
            .performClick()

        composeTestRule.waitUntilAtLeastOneExists(
            matcher = hasTestTag("historyTitle"),
            timeoutMillis = 5000
        )

        composeTestRule.onNodeWithTag("historyTitle")
            .assertExists()
            .assertIsDisplayed()
    }
}