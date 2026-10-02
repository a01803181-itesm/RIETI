package itesm.rieti.model.cognito

import android.util.Log
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.core.Amplify

object SignUp {
    fun withEmail(
        email: String,
        password: String,
        onCodeSent: () -> Unit,
        onError: (Exception) -> Unit
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
            }
        )
    }

    fun confirmUserAccount(
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