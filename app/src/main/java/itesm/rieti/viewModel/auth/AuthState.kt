package itesm.rieti.viewModel.auth

import itesm.rieti.model.esquemas.Usuario

/**
 * Representa el estado de autenticación actual de la aplicación.
 *
 * @property usuario Objeto [Usuario] asociado a la sesión actual, o `null` si no hay sesión.
 * @property sub Identificador único del usuario proporcionado por el servicio de autenticación.
 * @property pictureURL URL de la imagen de perfil del usuario, o `null` si no está disponible.
 * @property loggedIn Indica si el usuario tiene una sesión activa (`true`) o no (`false`).
 * @property error Mensaje de error relacionado con la autenticación, o `null` si no hay errores.
 */
data class AuthState(
    val usuario: Usuario? = null,
    val sub: String? = null,
    val pictureURL: String? = null,
    val loggedIn: Boolean = false,
    val error: String? = null
)