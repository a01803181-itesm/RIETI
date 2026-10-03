package itesm.rieti.viewModel.auth

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import itesm.rieti.model.api.usuarios.Manejador
import itesm.rieti.model.auth.Google
import kotlinx.coroutines.launch

class GoogleVM : ViewModel() {
    private val userAPIHandler = Manejador
    private val googleAuth = Google
    fun authenticate(
        activity: Activity,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        googleAuth.authenticate(activity) { user ->
            viewModelScope.launch {
                val response = userAPIHandler.checkEmail(user.correoU)
                if (response.isSuccessful && response.body()?.exists == false) {
                    Log.i("GoogleVM", "User not found, creating new user")
                    val response2 = userAPIHandler.crearUsuario(user)
                    if (response2.isSuccessful) {
                        Log.i("GoogleVM", "User signed up successfully")
                        onSuccess()
                    } else {
                        Log.e("GoogleVM", "Error signing up user: ${response2.message()}")
                        onError("Error signing up user: ${response2.message()}")
                    }
                } else if (response.isSuccessful && response.body()?.exists == true) {
                    Log.i("GoogleVM", "User found, logging in")
                    onSuccess()
                } else {
                    Log.e("GoogleVM", "Error checking email: ${response.message()}")
                    onError("Error checking email: ${response.message()}")
                }
            }
        }
    }
}