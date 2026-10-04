package itesm.rieti.viewModel.auth

import itesm.rieti.model.esquemas.Usuario

data class AuthState(
    val usuario: Usuario? = null,
    val sub: String? = null,
    val loggedIn: Boolean = false,
    val error: String? = null
)