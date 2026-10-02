package itesm.rieti.model.cognito

import android.app.Activity
import android.util.Log
import com.amplifyframework.auth.AuthProvider
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession
import com.amplifyframework.core.Amplify

object SignIn {
    fun withGoogle(activity: Activity) {
        Amplify.Auth.fetchAuthSession(
            { session ->
                if (session.isSignedIn) {
                    Log.i("AmplifyAuth", "Already signed in")
                    fetchAndSyncData()
                } else {
                    Log.i("AmplifyAuth", "Not signed in")
                    launchGoogleWebUI(activity)
                }
            },
            { error ->
                Log.e("AmplifyAuth", "Failed to fetch auth session", error)
            }
        )
    }

    private fun launchGoogleWebUI(activity: Activity) {
        Amplify.Auth.signInWithSocialWebUI(
            AuthProvider.google(),
            activity,
            { result ->
                if (result.isSignedIn) {
                    fetchAndSyncData()
                }
            },
            { error ->
                Log.e("AmplifyAuth", "Social WebUI login failed: ", error)
            }
        )
    }

    private fun fetchAndSyncData() {
        Amplify.Auth.fetchUserAttributes(
            { attributes ->
                val email = attributes.find { it.key == AuthUserAttributeKey.email() }?.value.orEmpty()
                val sub = attributes.find { it.key.keyString == "sub" }?.value.orEmpty()

                Amplify.Auth.fetchAuthSession(
                    { _ ->
                        Log.i("AmplifyAuth", "Authenticated User SUB: $sub")
                        Log.i("AmplifyAuth", "Authenticated Email: $email")
                    },
                    { error -> Log.e("AmplifyAuth", "Failed to fetch session tokens: ", error) }
                )
            },
            { error -> Log.e("AmplifyAuth", "Failed to fetch user attributes: ", error) }
        )
    }

    fun withEmail(
        email: String,
        password: String,
        onSuccess: (sub: String, email: String, idToken: String?) -> Unit,
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
                            val email = attributes.find { it.key == AuthUserAttributeKey.email() }?.value.orEmpty()

                            Amplify.Auth.fetchAuthSession(
                                { session ->
                                    val cognitoSession = session as? AWSCognitoAuthSession
                                    val idToken = cognitoSession?.userPoolTokensResult?.value?.idToken
                                    Log.i("AmplifyAuth", "Authenticated Local User SUB: $sub")
                                    onSuccess(sub, email, idToken)
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
            }
        )
    }
}