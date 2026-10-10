package itesm.rieti.viewModel.config

enum class Theme(val desc: String) {
    LIGHT("Claro"),
    DARK("Oscuro"),
    SYSTEM("Sistema")
}

enum class FontSize(val desc: String) {
    SMALL("Pequeña"),
    MEDIUM("Mediana"),
    LARGE("Grande")
}

data class ConfigState (
    val theme: Theme = Theme.SYSTEM,
    val fontSize: FontSize = FontSize.MEDIUM
)