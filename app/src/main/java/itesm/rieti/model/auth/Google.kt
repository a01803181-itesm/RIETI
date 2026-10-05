package itesm.rieti.model.auth

import android.app.Activity
import android.util.Log
import com.amplifyframework.auth.AuthProvider
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.core.Amplify
import itesm.rieti.model.esquemas.Provider
import itesm.rieti.model.esquemas.Usuario

object Google {
    fun authenticate(activity: Activity, onSuccessData: (Usuario, String, String) -> Unit) {
        Amplify.Auth.fetchAuthSession(
            { session ->
                if (session.isSignedIn) {
                    Log.i("Auth", "Already signed in (from Google Model)")
                    fetchAndSyncData(onSuccessData)
                } else {
                    Log.i("Auth", "User Not signed in (from Google Model)")
                    launchGoogleWebUI(activity, onSuccessData)
                }
            },
            { error ->
                Log.e("Auth", "Failed to fetch auth session (from Google Model)", error)
            }
        )
    }

    private fun launchGoogleWebUI(activity: Activity, onSuccessData: (Usuario, String, String) -> Unit) {
        Amplify.Auth.signInWithSocialWebUI(
            AuthProvider.google(),
            activity,
            { result ->
                if (result.isSignedIn) {
                    fetchAndSyncData(onSuccessData)
                }
            },
            { error ->
                Log.e("AmplifyAuth", "Social WebUI login failed: ", error)
            }
        )
    }

    private fun fetchAndSyncData(onSuccessData: (Usuario, String, String) -> Unit) {
        Amplify.Auth.fetchUserAttributes(
            { attributes ->
                val email = attributes.find { it.key == AuthUserAttributeKey.email() }?.value.orEmpty()
                val sub = attributes.find { it.key.keyString == "sub" }?.value.orEmpty()
                val pictureUrl = attributes.find { it.key == AuthUserAttributeKey.picture() }?.value.orEmpty()

                onSuccessData(Usuario(email, Provider.GOOGLE), sub, pictureUrl)

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
}