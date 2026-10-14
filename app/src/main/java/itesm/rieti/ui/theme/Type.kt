package itesm.rieti.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.googlefonts.Font
import itesm.rieti.R
import itesm.rieti.viewModel.config.FontSize

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val bodyFontFamily = FontFamily(
    Font(
        googleFont = GoogleFont("Aleo"),
        fontProvider = provider,
    )
)

val displayFontFamily = FontFamily(
    Font(
        googleFont = GoogleFont("Gothic A1"),
        fontProvider = provider,
    )
)

// Default Material 3 typography values
val baseline = Typography()

val AppTypography = Typography(
    displayLarge = baseline.displayLarge.copy(fontFamily = displayFontFamily),
    displayMedium = baseline.displayMedium.copy(fontFamily = displayFontFamily),
    displaySmall = baseline.displaySmall.copy(fontFamily = displayFontFamily),
    headlineLarge = baseline.headlineLarge.copy(fontFamily = displayFontFamily),
    headlineMedium = baseline.headlineMedium.copy(fontFamily = displayFontFamily),
    headlineSmall = baseline.headlineSmall.copy(fontFamily = displayFontFamily),
    titleLarge = baseline.titleLarge.copy(fontFamily = displayFontFamily),
    titleMedium = baseline.titleMedium.copy(fontFamily = displayFontFamily),
    titleSmall = baseline.titleSmall.copy(fontFamily = displayFontFamily),
    bodyLarge = baseline.bodyLarge.copy(fontFamily = bodyFontFamily),
    bodyMedium = baseline.bodyMedium.copy(fontFamily = bodyFontFamily),
    bodySmall = baseline.bodySmall.copy(fontFamily = bodyFontFamily),
    labelLarge = baseline.labelLarge.copy(fontFamily = bodyFontFamily),
    labelMedium = baseline.labelMedium.copy(fontFamily = bodyFontFamily),
    labelSmall = baseline.labelSmall.copy(fontFamily = bodyFontFamily),
)

private fun TextStyle.scale(factor: Float): TextStyle {
    return copy(fontSize = fontSize * factor)
}

fun getTypography(fontSize: FontSize): Typography {
    val scale = when (fontSize) {
        FontSize.SMALL -> 0.85f
        FontSize.MEDIUM -> 1.0f
        FontSize.LARGE -> 1.15f
    }
    if (scale == 1.0f) return AppTypography

    return Typography(
        displayLarge = AppTypography.displayLarge.scale(scale),
        displayMedium = AppTypography.displayMedium.scale(scale),
        displaySmall = AppTypography.displaySmall.scale(scale),
        headlineLarge = AppTypography.headlineLarge.scale(scale),
        headlineMedium = AppTypography.headlineMedium.scale(scale),
        headlineSmall = AppTypography.headlineSmall.scale(scale),
        titleLarge = AppTypography.titleLarge.scale(scale),
        titleMedium = AppTypography.titleMedium.scale(scale),
        titleSmall = AppTypography.titleSmall.scale(scale),
        bodyLarge = AppTypography.bodyLarge.scale(scale),
        bodyMedium = AppTypography.bodyMedium.scale(scale),
        bodySmall = AppTypography.bodySmall.scale(scale),
        labelLarge = AppTypography.labelLarge.scale(scale),
        labelMedium = AppTypography.labelMedium.scale(scale),
        labelSmall = AppTypography.labelSmall.scale(scale),
    )
}