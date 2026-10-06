package itesm.rieti.model.auth

import android.util.Log
import com.amplifyframework.auth.AuthUserAttribute
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession
import com.amplifyframework.auth.cognito.result.AWSCognitoAuthSignOutResult
import com.amplifyframework.core.Amplify
import itesm.rieti.model.esquemas.Provider
import itesm.rieti.model.esquemas.Usuario
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Interceptor
import kotlin.coroutines.resume

object Auth {
    fun checkCachedSession(onSessionValid: (Usuario, String, String) -> Unit, onRequireAuth: () -> Unit) {
        Log.i("Auth", "Starting checkAuth")
        Amplify.Auth.fetchAuthSession(
            { session ->
                if (session.isSignedIn) {
                    Log.i("Auth", "Session is valid")
                    Amplify.Auth.fetchUserAttributes (
                        { attributes ->
                            Log.i("Auth", "Attributes successfully fetched")
                            val email = attributes.find { it.key == AuthUserAttributeKey.email() }?.value.orEmpty()
                            val sub = attributes.find { it.key.keyString == "sub" }?.value.orEmpty()
                            val pictureUrl = attributes.find { it.key == AuthUserAttributeKey.picture() }?.value.orEmpty()
                            val identitiesJson = attributes.find { it.key.keyString == "identities" }?.value
                            val provider = if (identitiesJson != null && identitiesJson.contains("Google", ignoreCase = true)) {
                                Provider.GOOGLE
                            } else {
                                Provider.COGNITO
                            }
                            onSessionValid(Usuario(email, provider), sub, pictureUrl)
                        },
                        { error ->
                            Log.e("Auth", "Error fetching attributes", error)
                            onRequireAuth()
                        }
                    )
                } else {
                    Log.i("Auth", "Session is invalid")
                    onRequireAuth()
                }
            },
            { error ->
                Log.e("Auth", "Error checking session", error)
                onRequireAuth()
            }
        )
    }
    fun signOut(onComplete: () -> Unit) {
        Amplify.Auth.signOut { result ->
            when (result) {
                is AWSCognitoAuthSignOutResult.CompleteSignOut -> {
                    Log.i("AUTH", "Sign out successful. Tokens have been wiped")
                    onComplete()
                }
                is AWSCognitoAuthSignOutResult.FailedSignOut -> {
                    Log.e("AUTH", "Sign out failed", result.exception)
                    onComplete()
                }
                is AWSCognitoAuthSignOutResult.PartialSignOut -> {
                    Log.i("AUTH", "Partial sign out. Hosted UI might still have a cookie")
                    onComplete()
                }
            }
        }
    }
    fun getBearerToken(onResult: (String?) -> Unit) {
        Amplify.Auth.fetchAuthSession(
            { session ->
                val cognitoSession = session as? AWSCognitoAuthSession
                val bearerToken = cognitoSession?.userPoolTokensResult?.value?.accessToken
                if (bearerToken != null) {
                    onResult(bearerToken)
                } else {
                    Log.w("Auth", "Session is valid but token is missing")
                    onResult(null)
                }
            },
            { error ->
                Log.e("Auth", "Failed to fetch auth session for bearer token: ", error)
                onResult(null)
            }
        )
    }
}