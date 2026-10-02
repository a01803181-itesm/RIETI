package itesm.rieti.viewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LogInVM : ViewModel() {
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _otpRequired = MutableStateFlow(false)
    val otpRequired: StateFlow<Boolean> = _otpRequired

    private val _otp = MutableStateFlow("")
    val otp: StateFlow<String> = _otp

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun updateOtpRequired(otpRequired: Boolean) {
        _otpRequired.value = otpRequired
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
}