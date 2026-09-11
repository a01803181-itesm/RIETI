package itesm.rieti.model.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ManejadorAPI {
    private const val BASE_URL = "" // TODO

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val servicio by lazy {
        retrofit.create(ManejadorAPI::class.java)
    }

    suspend fun obtenerReporte() {

    }
}