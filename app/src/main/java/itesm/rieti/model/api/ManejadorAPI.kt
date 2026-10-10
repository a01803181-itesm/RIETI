package itesm.rieti.model.api

import itesm.rieti.model.auth.Auth
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.coroutines.resume

object ManejadorAPI {
    val authInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val token = runBlocking { getSynchronousToken() }
        if (token != null) {
            val newRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
            chain.proceed(newRequest)
        } else {
            chain.proceed(originalRequest)
        }
    }
    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .build()
    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Config.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    suspend fun getSynchronousToken(): String? = suspendCancellableCoroutine { continuation ->
        Auth.getBearerToken { token ->
            continuation.resume(token)
        }
    }
}
