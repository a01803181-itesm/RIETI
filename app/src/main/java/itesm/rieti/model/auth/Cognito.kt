package itesm.rieti.model.auth

import android.util.Log
import aws.sdk.kotlin.services.cognitoidentityprovider.model.CodeMismatchException
import aws.sdk.kotlin.services.cognitoidentityprovider.model.InvalidParameterException
import aws.sdk.kotlin.services.cognitoidentityprovider.model.InvalidPasswordException
import aws.sdk.kotlin.services.cognitoidentityprovider.model.LimitExceededException
import aws.sdk.kotlin.services.cognitoidentityprovider.model.NotAuthorizedException
import aws.sdk.kotlin.services.cognitoidentityprovider.model.UserNotFoundException
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.core.Amplify

/**
 * Objeto para gestionar el flujo de autenticación propio a través de Amazon Cognito.
 */
object Cognito {
    /**
     * Mapea un error de Amazon Cognito a un mensaje legible para el usuario en español.
     *
     * @param error La excepción capturada durante la autenticación.
     * @return Un mensaje descriptivo del error en español.
     */
    fun mapError(error: Exception): String {
        return when (error.cause) {
            is InvalidPasswordException -> "La contraseña debe contener al menos 8 caracteres, un número y un símbolo."
            is InvalidParameterException -> "El formato del correo electrónico no es válido."
            is NotAuthorizedException -> "El correo electrónico o la contraseña son incorrectos."
            is UserNotFoundException -> "No se encontró ninguna cuenta con este correo."
            is CodeMismatchException -> "El código de verificación es incorrecto."
            is LimitExceededException -> "Demasiados intentos. Intenta de nuevo más tarde."
            else -> "Ocurrió un error inesperado. Intenta de nuevo."
        }
    }
    /**
     * Valida si el formato de una cadena de texto corresponde a un correo electrónico válido.
     *
     * @param email La cadena de correo electrónico a validar.
     * @return true si es válido, false en caso contrario.
     */
    fun validateEmail(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
        return emailRegex.matches(email)
    }
    /**
     * Inicia sesión con un correo electrónico y una contraseña en Amazon Cognito.
     *
     * @param email El correo electrónico del usuario.
     * @param password La contraseña del usuario.
     * @param onSuccess Callback ejecutado al iniciar sesión correctamente. Recibe el 'sub' (ID único de usuario).
     * @param onError Callback ejecutado si ocurre un error durante el inicio de sesión.
     */
    fun signInWithEmail(
        email: String,
        password: String,
        onSuccess: (String) -> Unit,
        onError: (Exception) -> Unit
    ) {
        Amplify.Auth.signIn(
            email,
            password,
            { result ->
                if (result.isSignedIn) {
                    Log.i("AmplifyAuth", "Sign-in successful, fetching credentials...")
                    Amplify.Auth.fetchUserAttributes(
                        { attributes ->
                            val sub = attributes.find { it.key.keyString == "sub" }?.value.orEmpty()
                            onSuccess(sub)
                        },
                        { error -> onError(error) }
                    )
                }
            },
            { error ->
                Log.e("AmplifyAuth", "Sign-in failed: ", error)
                onError(error)
            }
        )
    }

    /**
     * Registra un nuevo usuario en Amazon Cognito usando un correo y una contraseña.
     * 
     * @param email Correo electrónico a registrar.
     * @param password Contraseña para el nuevo usuario.
     * @param onCodeSent Callback ejecutado al enviarse el código de confirmación al correo del usuario.
     * @param onError Callback ejecutado al ocurrir algún error durante el registro.
     */
    fun signUpWithEmail(
        email: String,
        password: String,
        onCodeSent: () -> Unit,
        onError: (Exception) -> Unit,
    ) {
        val options = AuthSignUpOptions.builder()
            .userAttribute(AuthUserAttributeKey.email(), email)
            .build()

        Amplify.Auth.signUp(
            email,
            password,
            options,
            { result ->
                Log.i("AmplifyAuth", "Sign up initiated. Code sent: ${result.isSignUpComplete}")
                if (!result.isSignUpComplete) onCodeSent()
            },
            { error ->
                Log.e("AmplifyAuth", "Sign up failed: ", error)
                onError(error)
            }
        )
    }

    /**
     * Confirma el registro del usuario usando el código enviado a su correo electrónico.
     *
     * @param email Correo electrónico asociado a la cuenta a confirmar.
     * @param confirmationCode Código de confirmación enviado por correo.
     * @param onConfirmed Callback ejecutado cuando la cuenta ha sido confirmada satisfactoriamente.
     * @param onError Callback ejecutado si ocurre un error al validar el código.
     */
    fun confirmSignUp(
        email: String,
        confirmationCode: String,
        onConfirmed: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        Amplify.Auth.confirmSignUp(
            email,
            confirmationCode,
            { result ->
                if (result.isSignUpComplete) {
                    Log.i("AmplifyAuth", "User account successfully created!")
                    onConfirmed()
                }
            },
            { error ->
                Log.e("AmplifyAuth", "Confirmation failed: ", error)
                onError(error)
            }
        )
    }
}