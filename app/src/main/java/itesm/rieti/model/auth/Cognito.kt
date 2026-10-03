package itesm.rieti.model.auth

import android.util.Log
import aws.sdk.kotlin.services.cognitoidentityprovider.model.CodeMismatchException
import aws.sdk.kotlin.services.cognitoidentityprovider.model.InvalidParameterException
import aws.sdk.kotlin.services.cognitoidentityprovider.model.InvalidPasswordException
import aws.sdk.kotlin.services.cognitoidentityprovider.model.LimitExceededException
import aws.sdk.kotlin.services.cognitoidentityprovider.model.NotAuthorizedException
import aws.sdk.kotlin.services.cognitoidentityprovider.model.UserNotFoundException
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.core.Amplify

object Cognito {
    fun checkCachedSession(onSessionValid: () -> Unit, onRequireAuth: () -> Unit) {
        Amplify.Auth.fetchAuthSession(
            { session ->
                if (session.isSignedIn) {
                    onSessionValid()
                } else {
                    onRequireAuth()
                }
            },
            { _ ->
                onRequireAuth()
            }
        )
    }
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
    fun signInWithEmail(
        email: String,
        password: String,
        onSuccess: () -> Unit,
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
                            val email = attributes.find { it.key == AuthUserAttributeKey.email() }?.value.orEmpty()

                            Amplify.Auth.fetchAuthSession(
                                { session ->
                                    val cognitoSession = session as? AWSCognitoAuthSession
                                    val idToken = cognitoSession?.userPoolTokensResult?.value?.idToken
                                    onSuccess()
                                },
                                { error -> onError(error) }
                            )
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