package itesm.rieti.viewModel.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import itesm.rieti.model.api.usuarios.Manejador
import itesm.rieti.model.auth.Cognito
import itesm.rieti.model.esquemas.Provider
import itesm.rieti.model.esquemas.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CognitoVM : ViewModel() {
    private val userAPIHandler = Manejador
    private val cognito = Cognito
    private val _cognitoState = MutableStateFlow(CognitoState())
    val cognitoState: StateFlow<CognitoState> = _cognitoState
    fun setPassword(password: String) { _cognitoState.value = _cognitoState.value.copy(password = password) }
    fun setOTP(otp: String) { _cognitoState.value = _cognitoState.value.copy(otp = otp) }
    fun authenticate(
        email: String,
        onSuccess: (String) -> Unit,
        onError: (String?) -> Unit,
    ) {
        Log.i("AUTH", "Function called")
        viewModelScope.launch {
            Log.i("AUTH", "User email: $email")
            val response = userAPIHandler.checkEmail(email)
            if (response.isSuccessful) {
                if (response.body()?.exists == true) {
                    cognito.signInWithEmail(
                        email,
                        _cognitoState.value.password,
                        { onSuccess(it) },
                        {
                            val error = cognito.mapError(it)
                            onError(error)
                        }
                    )
                } else {
                    cognito.signUpWithEmail(
                        email,
                        _cognitoState.value.password,
                        { _cognitoState.value = _cognitoState.value.copy(otpSent = true) },
                        {
                            val error = cognito.mapError(it)
                            onError(error)
                        }
                    )
                }
            } else {
                onError("Error de conexión al servidor. ${response.errorBody()}")
            }
        }
    }
    fun verifyOTP(
        email: String,
        onSuccess: () -> Unit,
        onError: (String?) -> Unit
    ) {
        cognito.confirmSignUp(
            email,
            _cognitoState.value.otp,
            {
                viewModelScope.launch {
                    onSuccess()
                    cognito.signInWithEmail(
                        email,
                        _cognitoState.value.password,
                        { onSuccess() },
                        { error -> onError("Background login failed: ${error.message}") }
                    )
                }
            },
            { onError(it.message) }
        )
    }
    fun resetState() { _cognitoState.value = CognitoState() }
}