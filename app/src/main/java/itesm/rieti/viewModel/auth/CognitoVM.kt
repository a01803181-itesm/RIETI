package itesm.rieti.viewModel.auth

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
    private val _authState = MutableStateFlow(AuthState())
    private val _cognitoState = MutableStateFlow(CognitoState())
    val cognitoState: StateFlow<CognitoState> = _cognitoState
    fun setPassword(password: String) { _cognitoState.value = _cognitoState.value.copy(password = password) }
    fun setOTP(otp: String) { _cognitoState.value = _cognitoState.value.copy(otp = otp) }
    fun authenticate() {
        viewModelScope.launch {
            val response = userAPIHandler.checkEmail(_authState.value.usuario!!.correoU)
            if (response.isSuccessful) {
                if (response.body()?.exists == true) {
                    _authState.value = _authState.value.copy(error = "El correo ya está registrado por ${response.body()?.provider!!}. Inicie sesión con ${response.body()?.provider!!}")
                } else {
                    cognito.signUpWithEmail(
                        _authState.value.usuario!!.correoU,
                        _cognitoState.value.password,
                        { _cognitoState.value = _cognitoState.value.copy(otpSent = true) },
                        {
                            val error = cognito.mapError(it)
                            _authState.value = _authState.value.copy(error = error)
                        }
                    )
                }
            } else {
                _authState.value = _authState.value.copy(error = "Error de conexión al servidor. ${response.errorBody()}")
            }
        }
    }

    fun verifyOTP() {
        cognito.confirmSignUp(
            _authState.value.usuario!!.correoU,
            _cognitoState.value.otp,
            { _authState.value = _authState.value.copy(loggedIn = true) },
            { _authState.value = _authState.value.copy(error = it.message) }
        )
    }
}