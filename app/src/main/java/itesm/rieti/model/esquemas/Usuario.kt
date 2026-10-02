package itesm.rieti.model.esquemas

data class LegacyUsuario (
    val correoU: String,
    val contrasenia: String,
    val proveedor: String
)

data class Usuario (
    val correoU: String,
    val sub: String
)