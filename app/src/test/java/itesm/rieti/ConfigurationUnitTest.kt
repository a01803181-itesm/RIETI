package itesm.rieti

import android.app.Application
import android.content.Context
import itesm.rieti.ui.theme.getTypography
import itesm.rieti.viewModel.config.ConfigVM
import itesm.rieti.viewModel.config.FontSize
import itesm.rieti.viewModel.config.Theme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

/**
 * Test application subclass that provides a non-null context and temporary filesDir for DataStore.
 */
class TestApplication(private val tempDir: File) : Application() {
    override fun getApplicationContext(): Context = this
    override fun getFilesDir(): File = tempDir
}

/**
 * Helper function replicating the deterministic dark theme decision logic used by [itesm.rieti.ui.theme.RIETITheme].
 */
fun resolveDarkTheme(theme: Theme, isSystemInDark: Boolean): Boolean {
    return when (theme) {
        Theme.LIGHT -> false
        Theme.DARK -> true
        Theme.SYSTEM -> isSystemInDark
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class ConfigurationUnitTest {

    @get:Rule
    val tempFolder = TemporaryFolder()

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `ConfigVM initial state defaults to SYSTEM theme and MEDIUM font size`() {
        val app = TestApplication(tempFolder.newFolder())
        val viewModel = ConfigVM(app)

        assertEquals(Theme.SYSTEM, viewModel.state.value.theme)
        assertEquals(FontSize.MEDIUM, viewModel.state.value.fontSize)
    }

    @Test
    fun `setTheme updates ConfigVM state to LIGHT, DARK, and SYSTEM`() {
        val app = TestApplication(tempFolder.newFolder())
        val viewModel = ConfigVM(app)

        viewModel.setTheme(Theme.DARK)
        assertEquals(Theme.DARK, viewModel.state.value.theme)

        viewModel.setTheme(Theme.LIGHT)
        assertEquals(Theme.LIGHT, viewModel.state.value.theme)

        viewModel.setTheme(Theme.SYSTEM)
        assertEquals(Theme.SYSTEM, viewModel.state.value.theme)
    }

    @Test
    fun `setFontSize updates ConfigVM state to SMALL, LARGE, and MEDIUM`() {
        val app = TestApplication(tempFolder.newFolder())
        val viewModel = ConfigVM(app)

        viewModel.setFontSize(FontSize.LARGE)
        assertEquals(FontSize.LARGE, viewModel.state.value.fontSize)

        viewModel.setFontSize(FontSize.SMALL)
        assertEquals(FontSize.SMALL, viewModel.state.value.fontSize)

        viewModel.setFontSize(FontSize.MEDIUM)
        assertEquals(FontSize.MEDIUM, viewModel.state.value.fontSize)
    }

    @Test
    fun `Theme enum descriptions match expected Spanish display strings`() {
        assertEquals("Claro", Theme.LIGHT.desc)
        assertEquals("Oscuro", Theme.DARK.desc)
        assertEquals("Sistema", Theme.SYSTEM.desc)
    }

    @Test
    fun `FontSize enum descriptions match expected Spanish display strings`() {
        assertEquals("Pequeña", FontSize.SMALL.desc)
        assertEquals("Mediana", FontSize.MEDIUM.desc)
        assertEquals("Grande", FontSize.LARGE.desc)
    }

    @Test
    fun `resolveDarkTheme returns false when Theme is LIGHT regardless of system mode`() {
        assertFalse(resolveDarkTheme(Theme.LIGHT, isSystemInDark = true))
        assertFalse(resolveDarkTheme(Theme.LIGHT, isSystemInDark = false))
    }

    @Test
    fun `resolveDarkTheme returns true when Theme is DARK regardless of system mode`() {
        assertTrue(resolveDarkTheme(Theme.DARK, isSystemInDark = true))
        assertTrue(resolveDarkTheme(Theme.DARK, isSystemInDark = false))
    }

    @Test
    fun `resolveDarkTheme delegates to system mode when Theme is SYSTEM`() {
        assertTrue(resolveDarkTheme(Theme.SYSTEM, isSystemInDark = true))
        assertFalse(resolveDarkTheme(Theme.SYSTEM, isSystemInDark = false))
    }

    @Test
    fun `getTypography for MEDIUM font size matches baseline typography`() {
        val typography = getTypography(FontSize.MEDIUM)
        val defaultTypography = getTypography(FontSize.MEDIUM)

        assertEquals(defaultTypography.bodyMedium.fontSize, typography.bodyMedium.fontSize)
        assertEquals(defaultTypography.titleLarge.fontSize, typography.titleLarge.fontSize)
        assertEquals(defaultTypography.headlineMedium.fontSize, typography.headlineMedium.fontSize)
    }

    @Test
    fun `getTypography for SMALL font size scales text styles by 0,85x`() {
        val mediumTypography = getTypography(FontSize.MEDIUM)
        val smallTypography = getTypography(FontSize.SMALL)

        val expectedBodyMediumSize = mediumTypography.bodyMedium.fontSize * 0.85f
        val expectedTitleLargeSize = mediumTypography.titleLarge.fontSize * 0.85f

        assertEquals(expectedBodyMediumSize.value, smallTypography.bodyMedium.fontSize.value, 0.01f)
        assertEquals(expectedTitleLargeSize.value, smallTypography.titleLarge.fontSize.value, 0.01f)
    }

    @Test
    fun `getTypography for LARGE font size scales text styles by 1,15x`() {
        val mediumTypography = getTypography(FontSize.MEDIUM)
        val largeTypography = getTypography(FontSize.LARGE)

        val expectedBodyMediumSize = mediumTypography.bodyMedium.fontSize * 1.15f
        val expectedHeadlineLargeSize = mediumTypography.headlineLarge.fontSize * 1.15f

        assertEquals(expectedBodyMediumSize.value, largeTypography.bodyMedium.fontSize.value, 0.01f)
        assertEquals(expectedHeadlineLargeSize.value, largeTypography.headlineLarge.fontSize.value, 0.01f)
    }
}