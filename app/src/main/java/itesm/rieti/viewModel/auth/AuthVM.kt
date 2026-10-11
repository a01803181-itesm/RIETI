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

/**
 * ViewModel encargado de manejar la autenticación principal de la aplicación,
 * incluyendo la persistencia de la sesión y la conexión con el servidor.
 */
class AuthVM : ViewModel() {
    private val auth = Auth
    private val userHandler = Manejador
    private val _authState = MutableStateFlow(AuthState())
    
    /**
     * Estado observable de la autenticación.
     */
    val authState: StateFlow<AuthState> = _authState

    init {
        checkAuth()
    }

    /**
     * Establece el correo electrónico y el proveedor del usuario actual.
     *
     * @param email Correo electrónico del usuario.
     * @param provider Proveedor de autenticación (AWS Cognito o Google).
     */
    fun setEmail(email: String, provider: Provider) { _authState.value = _authState.value.copy(usuario = Usuario(correoU = email, proveedor = provider)) }

    /**
     * Establece el mensaje de error de autenticación.
     *
     * @param error Mensaje de error, o `null` para limpiarlo.
     */
    fun setError(error: String?) { _authState.value = _authState.value.copy(error = error) }

    /**
     * Registra un nuevo usuario en la base de datos de la API utilizando FastAPI.
     * Si la creación es exitosa, se marca al usuario como conectado.
     */
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

    /**
     * Actualiza el estado de conexión del usuario.
     *
     * @param loggedIn `true` si el usuario inició sesión correctamente, `false` en caso contrario.
     */
    fun setLoggedIn(loggedIn: Boolean) {
        _authState.value = _authState.value.copy(loggedIn = loggedIn)
    }

    /**
     * Establece el identificador único (sub) del usuario.
     *
     * @param sub Identificador proporcionado por el proveedor de identidad.
     */
    fun setSUB(sub: String?) { _authState.value = _authState.value.copy(sub = sub) }

    /**
     * Establece la URL de la imagen de perfil del usuario.
     *
     * @param url URL de la imagen.
     */
    fun setPictureURL(url: String?) { _authState.value = _authState.value.copy(pictureURL = url) }

    /**
     * Verifica de manera asíncrona si hay una sesión almacenada en la caché local y actualiza el estado.
     */
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

    /**
     * Cierra la sesión activa del usuario y reinicia el estado de autenticación.
     *
     * @param onSignOut Función de retorno que se ejecuta al finalizar el cierre de sesión.
     */
    fun signOut(onSignOut: () -> Unit) {
        auth.signOut {
            onSignOut()
            _authState.value = AuthState()
        }
    }
}