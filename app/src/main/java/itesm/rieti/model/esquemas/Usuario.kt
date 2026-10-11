package itesm.rieti.model.esquemas

import com.google.gson.annotations.SerializedName

/**
 * Enumeración que define el proveedor de identidad empleado para la autenticación.
 */
enum class Provider {
    /** Proveedor de identidad Amazon Cognito (correo y contraseña). */
    @SerializedName("cognito")
    COGNITO,
    /** Proveedor de identidad Google (inicio de sesión social). */
    @SerializedName("google")
    GOOGLE
}

/**
 * Clase de datos que representa a un usuario dentro del sistema.
 *
 * @property correoU Correo electrónico del usuario.
 * @property proveedor Proveedor de autenticación empleado por este usuario.
 */
data class Usuario (
    val correoU: String,
    val proveedor: Provider
)

/**
 * Clase de datos usada para retornar el estado de verificación de un correo en la base de datos.
 *
 * @property exists Indica si el correo electrónico ya se encuentra registrado.
 * @property provider Nombre del proveedor con el cual el correo fue registrado (si aplica).
 */
data class CheckEmail (
    val exists: Boolean,
    val provider: String?
)