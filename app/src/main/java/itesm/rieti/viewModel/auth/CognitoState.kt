package itesm.rieti.viewModel.auth

/**
 * Representa el estado de autenticación con Amazon Cognito.
 *
 * @property password Contraseña actual ingresada por el usuario.
 * @property otpSent Indica si se ha enviado el código OTP al correo del usuario.
 * @property otp Código de verificación OTP ingresado por el usuario.
 * @property isRecoveringPassword Indica si el usuario está en un flujo de recuperación de contraseña.
 */
data class CognitoState (
    val password: String = "",
    val otpSent: Boolean = false,
    val otp: String = "",
    val isRecoveringPassword: Boolean = false
)