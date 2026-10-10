package itesm.rieti.viewModel.auth

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amplifyframework.core.Amplify
import itesm.rieti.model.api.usuarios.Manejador
import itesm.rieti.model.auth.Google
import itesm.rieti.model.esquemas.Usuario
import kotlinx.coroutines.launch

class GoogleVM : ViewModel() {
    private val userAPIHandler = Manejador
    private val googleAuth = Google
    fun authenticate(
        activity: Activity,
        onSuccess: (Usuario, String, String) -> Unit,
        onError: (String) -> Unit
    ) {
        googleAuth.authenticate(activity) { user, sub, pictureURL ->
            viewModelScope.launch {
                val response = userAPIHandler.checkEmail(user.correoU)
                Log.i("Auth", "Check Email Response: $response")

                if (response.isSuccessful) {
                    val exists = response.body()?.exists == true
                    val provider = response.body()?.provider

                    if (!exists) {
                        Log.i("Auth", "User not found, creating new user")
                        val response2 = userAPIHandler.crearUsuario(user)
                        Log.i("Auth", "User creation response: $response2")
                        if (response2.isSuccessful) {
                            Log.i("Auth", "User signed up successfully")
                            onSuccess(user, sub, pictureURL)
                        } else {
                            Log.e("Auth", "Error signing up user: ${response2.message()}")
                            onError("Error al registrar al usuario: ${response2.message()}")
                        }
                    } else if (provider == "cognito") {
                        Log.w("Auth", "User already registered with cognito. Deleting duplicate Google account")
                        Amplify.Auth.deleteUser(
                            { Log.i("Auth", "Ghost Google account deleted successfully") },
                            { Log.e("Auth", "Failed to delete ghost Google account", it) }
                        )
                            onError("El usuario ya está registrado. Por favor, inicie sesión con su correo y contraseña")
                    } else {
                        Log.i("Auth", "User found, logging in")
                        onSuccess(user, sub, pictureURL)
                    }
                } else {
                    Log.e("Auth", "Error checking email: ${response.message()}")
                    onError("Error de conexión al servidor: ${response.message()}")
                }
            }
        }
    }
}