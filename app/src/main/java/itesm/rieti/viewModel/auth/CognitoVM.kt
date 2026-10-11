package itesm.rieti.viewModel.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amplifyframework.core.Amplify
import itesm.rieti.model.api.usuarios.Manejador
import itesm.rieti.model.auth.Cognito
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel encargado de manejar el flujo de registro, inicio de sesión y recuperación 
 * de contraseña utilizando Amazon Cognito.
 */
class CognitoVM : ViewModel() {
    private val userAPIHandler = Manejador
    private val cognito = Cognito
    private val _cognitoState = MutableStateFlow(CognitoState())
    
    /**
     * Estado observable del flujo de Cognito.
     */
    val cognitoState: StateFlow<CognitoState> = _cognitoState

    /**
     * Actualiza la contraseña ingresada en el estado.
     *
     * @param password Contraseña proporcionada.
     */
    fun setPassword(password: String) { _cognitoState.value = _cognitoState.value.copy(password = password) }

    /**
     * Actualiza la bandera que indica si se está recuperando la contraseña.
     *
     * @param isRecoveringPassword `true` para activar el flujo de recuperación, `false` para desactivarlo.
     */
    fun setRecoveringPassword(isRecoveringPassword: Boolean) { _cognitoState.value = _cognitoState.value.copy(isRecoveringPassword = isRecoveringPassword) }

    /**
     * Actualiza el código OTP ingresado por el usuario.
     *
     * @param otp Código de un solo uso.
     */
    fun setOTP(otp: String) { _cognitoState.value = _cognitoState.value.copy(otp = otp) }

    /**
     * Autentica a un usuario comprobando su existencia en la API y luego usando Cognito 
     * para iniciar sesión o registrarlo.
     *
     * @param email Correo electrónico del usuario.
     * @param onSuccess Callback ejecutado al tener éxito en la autenticación.
     * @param onError Callback ejecutado al ocurrir un error, recibe el mensaje.
     */
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

    /**
     * Verifica el código OTP proporcionado durante el registro.
     *
     * @param email Correo electrónico del usuario.
     * @param onSuccess Callback ejecutado si se verifica e inicia sesión correctamente.
     * @param onError Callback ejecutado en caso de error de verificación o inicio de sesión.
     */
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

    /**
     * Reinicia por completo el estado de Cognito a sus valores predeterminados.
     */
    fun resetState() { _cognitoState.value = CognitoState() }

    /**
     * Inicia el flujo para restablecer la contraseña enviando un código al correo proporcionado.
     *
     * @param email Correo electrónico del usuario.
     * @param onSuccess Callback ejecutado cuando se envía exitosamente el código.
     * @param onError Callback ejecutado cuando ocurre un error en el proceso.
     */
    fun resetPassword(
        email: String,
        onSuccess: () -> Unit,
        onError: (String?) -> Unit
    ) {
        if (cognito.validateEmail(email)) {
            viewModelScope.launch {
                val response = userAPIHandler.checkEmail(email)
                if (response.isSuccessful) {
                    when (response.body()?.exists) {
                        true if response.body()?.provider == "cognito" -> {
                            Amplify.Auth.resetPassword(
                                email,
                                {
                                    Log.i("Auth", "Password reset code sent to $email")
                                    onSuccess()
                                },
                                {
                                    Log.e("Auth", "Failed to reset password", it)
                                    onError(it.message)
                                }
                            )
                        }
                        true if response.body()?.provider == "google" -> {
                            onError("No es posible restablecer la contraseña. La cuenta está vinculada con Google")
                        }
                        else -> {
                            onError("El correo electrónico no está registrado")
                        }
                    }
                } else {
                    onError("Error de conexión al servidor. ${response.code()}: ${response.message()}.")
                }
            }
        } else {
            onError("Correo electrónico inválido")
        }
    }

    /**
     * Confirma el restablecimiento de la contraseña empleando el código de confirmación.
     *
     * @param email Correo electrónico del usuario.
     * @param newPassword Nueva contraseña proporcionada.
     * @param confirmationCode Código de confirmación recibido en el correo.
     * @param onSuccess Callback ejecutado al confirmar exitosamente la nueva contraseña.
     * @param onError Callback ejecutado si la confirmación falla.
     */
    fun confirmPasswordReset(
        email: String,
        newPassword: String,
        confirmationCode: String,
        onSuccess: () -> Unit,
        onError: (String?) -> Unit
    ) {
        Amplify.Auth.confirmResetPassword(
            email,
            newPassword,
            confirmationCode,
            {
                Log.i("Auth", "Password reset successully")
                onSuccess()
            },
            {
                Log.e("Auth", "Failed to confirm reset password", it)
                onError(it.message)
            }
        )
    }
}