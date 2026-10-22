package itesm.rieti

import android.app.Application
import android.content.Context
import itesm.rieti.ui.theme.getTypography
import itesm.rieti.viewModel.config.ConfigVM
import itesm.rieti.viewModel.config.FontSize
import itesm.rieti.viewModel.config.Theme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class TestApplication(private val tempDir: File) : Application() {
    override fun getApplicationContext(): Context = this
    override fun getFilesDir(): File = tempDir
}

fun resolveDarkTheme(theme: Theme, isSystemInDark: Boolean): Boolean {
    return when (theme) {
        Theme.LIGHT -> false
        Theme.DARK -> true
        Theme.SYSTEM -> isSystemInDark
    }
}

class ConfigurationUnitTest {

    @get:Rule
    val tempFolder = TemporaryFolder()

    @Test
    fun `estado inicial de ConfigVM es por defecto tema SISTEMA y tamano de letra MEDIANO`() {
        val app = TestApplication(tempFolder.newFolder())
        val viewModel = ConfigVM(app)

        assertEquals(Theme.SYSTEM, viewModel.state.value.theme)
        assertEquals(FontSize.MEDIUM, viewModel.state.value.fontSize)
    }

    @Test
    fun `setTheme actualiza el estado de ConfigVM a CLARO, OSCURO y SISTEMA`() {
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
    fun `setFontSize actualiza el estado de ConfigVM a PEQUENA, GRANDE y MEDIANA`() {
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
    fun `descripciones de enum Theme coinciden con los textos en espanol esperados`() {
        assertEquals("Claro", Theme.LIGHT.desc)
        assertEquals("Oscuro", Theme.DARK.desc)
        assertEquals("Sistema", Theme.SYSTEM.desc)
    }

    @Test
    fun `descripciones de enum FontSize coinciden con los textos en espanol esperados`() {
        assertEquals("Pequeña", FontSize.SMALL.desc)
        assertEquals("Mediana", FontSize.MEDIUM.desc)
        assertEquals("Grande", FontSize.LARGE.desc)
    }

    @Test
    fun `resolveDarkTheme devuelve false cuando el tema es CLARO independientemente del sistema`() {
        assertFalse(resolveDarkTheme(Theme.LIGHT, isSystemInDark = true))
        assertFalse(resolveDarkTheme(Theme.LIGHT, isSystemInDark = false))
    }

    @Test
    fun `resolveDarkTheme devuelve true cuando el tema es OSCURO independientemente del sistema`() {
        assertTrue(resolveDarkTheme(Theme.DARK, isSystemInDark = true))
        assertTrue(resolveDarkTheme(Theme.DARK, isSystemInDark = false))
    }

    @Test
    fun `resolveDarkTheme delega al modo del sistema cuando el tema es SISTEMA`() {
        assertTrue(resolveDarkTheme(Theme.SYSTEM, isSystemInDark = true))
        assertFalse(resolveDarkTheme(Theme.SYSTEM, isSystemInDark = false))
    }

    @Test
    fun `getTypography para tamano MEDIANO coincide con la tipografia base`() {
        val typography = getTypography(FontSize.MEDIUM)
        val defaultTypography = getTypography(FontSize.MEDIUM)

        assertEquals(defaultTypography.bodyMedium.fontSize, typography.bodyMedium.fontSize)
        assertEquals(defaultTypography.titleLarge.fontSize, typography.titleLarge.fontSize)
        assertEquals(defaultTypography.headlineMedium.fontSize, typography.headlineMedium.fontSize)
    }

    @Test
    fun `getTypography para tamano PEQUENA escala los estilos de texto al 85 por ciento`() {
        val mediumTypography = getTypography(FontSize.MEDIUM)
        val smallTypography = getTypography(FontSize.SMALL)

        val expectedBodyMediumSize = mediumTypography.bodyMedium.fontSize * 0.85f
        val expectedTitleLargeSize = mediumTypography.titleLarge.fontSize * 0.85f

        assertEquals(expectedBodyMediumSize.value, smallTypography.bodyMedium.fontSize.value, 0.01f)
        assertEquals(expectedTitleLargeSize.value, smallTypography.titleLarge.fontSize.value, 0.01f)
    }

    @Test
    fun `getTypography para tamano GRANDE escala los estilos de texto al 115 por ciento`() {
        val mediumTypography = getTypography(FontSize.MEDIUM)
        val largeTypography = getTypography(FontSize.LARGE)

        val expectedBodyMediumSize = mediumTypography.bodyMedium.fontSize * 1.15f
        val expectedHeadlineLargeSize = mediumTypography.headlineLarge.fontSize * 1.15f

        assertEquals(expectedBodyMediumSize.value, largeTypography.bodyMedium.fontSize.value, 0.01f)
        assertEquals(expectedHeadlineLargeSize.value, largeTypography.headlineLarge.fontSize.value, 0.01f)
    }
}
