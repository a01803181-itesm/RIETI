package itesm.rieti.model.api

import itesm.rieti.model.auth.Auth
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.coroutines.resume

/**
 * Objeto manejador de la configuración de la API y el cliente HTTP.
 */
object ManejadorAPI {
    /**
     * Interceptor de autenticación que añade el token Bearer a las peticiones.
     */
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
    
    /**
     * Cliente OkHttp configurado con el interceptor de autenticación.
     */
    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .build()
        
    /**
     * Instancia de Retrofit configurada con la URL base, el cliente OkHttp y el convertidor GSON.
     */
    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Config.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    /**
     * Obtiene el token de autenticación de forma asíncrona suspendiendo la corrutina.
     *
     * @return El token de autenticación, o null si no se puede obtener.
     */
    suspend fun getSynchronousToken(): String? = suspendCancellableCoroutine { continuation ->
        Auth.getBearerToken { token ->
            continuation.resume(token)
        }
    }
}