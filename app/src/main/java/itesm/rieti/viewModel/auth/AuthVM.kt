package itesm.rieti.viewModel.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import itesm.rieti.model.api.usuarios.Manejador
import itesm.rieti.model.auth.Auth
import itesm.rieti.model.esquemas.Provider
import itesm.rieti.model.esquemas.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthVM : ViewModel() {
    private val auth = Auth
    private val userHandler = Manejador
    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState
    fun setEmail(email: String, provider: Provider) { _authState.value = _authState.value.copy(usuario = Usuario(correoU = email, provider = provider)) }
    fun setError(error: String?) { _authState.value = _authState.value.copy(error = error) }
    fun registerUser(sub: String) {
        viewModelScope.launch {
            val user = _authState.value.usuario
            if (user != null) {
                try {
                    val response = userHandler.crearUsuario(user)
                    if (response.isSuccessful) {
                        setSUB(sub)
                        setLoggedIn(true)
                    } else {
                        setError(response.message())
                    }
                } catch (e: Exception) {
                    setError(e.message)
                }
            }
        }
    }
    fun setLoggedIn(loggedIn: Boolean) {
        _authState.value = _authState.value.copy(loggedIn = loggedIn)
    }
    fun setSUB(sub: String?) { _authState.value = _authState.value.copy(sub = sub) }
    fun checkAuth() {
        auth.checkCachedSession(
            { user, sub ->
                setLoggedIn(true)
                setEmail(user.correoU, user.provider)
                setSUB(sub)
            },
            {
                setLoggedIn(false)
                Log.i("Auth", "Not logged in")
            }
        )
    }
}