package itesm.rieti.viewModel.auth

data class CognitoState (
    val password: String = "",
    val otpSent: Boolean = false,
    val otp: String = "",
    val isRecoveringPassword: Boolean = false
)