package itesm.rieti.model.api

import itesm.rieti.model.esquemas.Reporte
import itesm.rieti.model.esquemas.Usuario
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ManejadorAPI {
    private const val BASE_URL = "" // imagino q ya la tengo

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val servicio by lazy {
        retrofit.create(ManejadorAPI::class.java)
    }

    // GET
    suspend fun obtenerCorreo(nombre: String = ""): Usuario
    {

    }

    suspend fun obtenerReportes(nombre: String = ""): Reporte
    {

    }

    // POST
    suspend fun mandarUsuario(nombre: String = ""): Usuario
    {

    }

    suspend fun mandarReporte(nombre: String = ""): Reporte
    {

    }
}