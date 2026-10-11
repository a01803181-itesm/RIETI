package itesm.rieti.viewModel.config

/**
 * Enumeración que representa los temas visuales disponibles en la aplicación.
 *
 * @property desc Descripción en texto del tema.
 */
enum class Theme(val desc: String) {
    /** Tema claro. */
    LIGHT("Claro"),
    
    /** Tema oscuro. */
    DARK("Oscuro"),
    
    /** Tema basado en el sistema operativo. */
    SYSTEM("Sistema")
}

/**
 * Enumeración que representa los tamaños de fuente disponibles.
 *
 * @property desc Descripción en texto del tamaño de fuente.
 */
enum class FontSize(val desc: String) {
    /** Tamaño de fuente pequeño. */
    SMALL("Pequeña"),
    
    /** Tamaño de fuente mediano (por defecto). */
    MEDIUM("Mediana"),
    
    /** Tamaño de fuente grande. */
    LARGE("Grande")
}

/**
 * Representa el estado de la configuración global de la aplicación.
 *
 * @property theme Tema de interfaz gráfica actual.
 * @property fontSize Tamaño de fuente seleccionado en la interfaz.
 */
data class ConfigState (
    val theme: Theme = Theme.SYSTEM,
    val fontSize: FontSize = FontSize.MEDIUM
)