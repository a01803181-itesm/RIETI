package itesm.rieti.viewModel.auth

import androidx.lifecycle.ViewModel
import itesm.rieti.model.esquemas.Provider
import itesm.rieti.model.esquemas.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthVM : ViewModel() {
    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState
    fun setEmail(email: String, provider: Provider) { _authState.value = _authState.value.copy(usuario = Usuario(correoU = email, provider = provider)) }
    fun setError(error: String?) { _authState.value = _authState.value.copy(error = error) }
}