package itesm.rieti.viewModel

import android.app.Activity
import androidx.lifecycle.ViewModel
import itesm.rieti.model.cognito.SignIn
import itesm.rieti.model.cognito.SignUp
import itesm.rieti.model.cognito.SignUp.confirmUserAccount
import itesm.rieti.viewModel.api.UsuariosVM
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LogInVM : ViewModel() {
    val model = SignUp
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _otpRequired = MutableStateFlow(false)
    val otpRequired: StateFlow<Boolean> = _otpRequired

    private val _otp = MutableStateFlow("")
    val otp: StateFlow<String> = _otp

    private val _successful = MutableStateFlow(false)
    val successful: StateFlow<Boolean> = _successful

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun triggerOTPSend() {
        SignUp.withEmail(
            email = _email.value,
            password = _password.value,
            onCodeSent = { _otpRequired.value = true }
        )
    }

    fun tryLogIn() {
        confirmUserAccount(
            email = _email.value,
            confirmationCode = _otp.value,
            onConfirmed = { _successful.value = true },
            onError = { _ -> _error.value = "Código incorrecto. Inténtalo de nuevo." }
        )
    }

    fun updateEmail(email: String) {
        _email.value = email
    }

    fun updatePassword(password: String) {
        _password.value = password
    }

    fun updateOtp(otp: String) {
        _otp.value = otp
    }

    fun updateError(error: String?) {
        _error.value = error
    }

    val signInWithGoogleModel = SignIn

    fun withGoogle(activity: Activity) {
        val usuarioVM = UsuariosVM()
        signInWithGoogleModel.withGoogle(activity) { user ->
            usuarioVM.actualizarUsuario(user)
            _successful.value = true
        }
    }
}