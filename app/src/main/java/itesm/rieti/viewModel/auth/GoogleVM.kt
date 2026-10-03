package itesm.rieti.viewModel.auth

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import itesm.rieti.model.api.usuarios.Manejador
import itesm.rieti.model.auth.Google
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class GoogleVM : ViewModel() {
    private val userAPIHandler = Manejador
    private val googleAuth = Google
    private val _authState = MutableStateFlow(AuthState())
    fun authenticate(activity: Activity) {
        googleAuth.authenticate(activity) { user ->
            viewModelScope.launch {
                val response = userAPIHandler.checkEmail(user.correoU)
                if (response.isSuccessful && response.body()?.exists == false) {
                    val response2 = userAPIHandler.crearUsuario(user)
                    if (response2.isSuccessful) {
                        _authState.value = _authState.value.copy(loggedIn = true)
                    } else {
                        _authState.value = _authState.value.copy(error = response2.message())
                    }
                } else if (response.isSuccessful && response.body()?.exists == true) {
                    _authState.value = _authState.value.copy(loggedIn = true)
                }
            }
        }
    }
}