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

    init {
        checkAuth()
    }
    fun setEmail(email: String, provider: Provider) { _authState.value = _authState.value.copy(usuario = Usuario(correoU = email, proveedor = provider)) }
    fun setError(error: String?) { _authState.value = _authState.value.copy(error = error) }
    fun registerUser() {
        viewModelScope.launch {
            if (_authState.value.usuario != null) {
                try {
                    Log.i("AUTH", "Inserting user through FastAPI")
                    Log.i("AUTH", "User: ${_authState.value.usuario}")
                    val response = userHandler.crearUsuario(_authState.value.usuario!!)
                    if (response.isSuccessful) {
                        Log.i("AUTH", "User inserted successfully")
                        setLoggedIn(true)
                    } else {
                        Log.e("AUTH", "FastAPI error response: ${response.message()}")
                        setError(response.message())
                    }
                } catch (e: Exception) {
                    Log.e("AUTH", "FastAPI connection error: ${e.message}")
                    setError(e.message)
                }
            }
        }
    }
    fun setLoggedIn(loggedIn: Boolean) {
        _authState.value = _authState.value.copy(loggedIn = loggedIn)
    }
    fun setSUB(sub: String?) { _authState.value = _authState.value.copy(sub = sub) }
    fun setPictureURL(url: String?) { _authState.value = _authState.value.copy(pictureURL = url) }
    private fun checkAuth() {
        auth.checkCachedSession(
            { user, sub, url ->
                setLoggedIn(true)
                setEmail(user.correoU, user.proveedor)
                setSUB(sub)
                setPictureURL(url)
            },
            {
                setLoggedIn(false)
                Log.i("Auth", "Not logged in")
            }
        )
    }
    fun signOut(onSignOut: () -> Unit) {
        auth.signOut {
            onSignOut()
            _authState.value = AuthState()
        }
    }
}