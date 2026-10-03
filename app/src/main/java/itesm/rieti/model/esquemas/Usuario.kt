package itesm.rieti.model.esquemas

enum class Provider(name: String) {
    COGNITO("cognito"),
    GOOGLE("google")
}
data class Usuario (
    val correoU: String,
    val provider: Provider
)
data class CheckEmail (
    val exists: Boolean,
    val provider: String?
)