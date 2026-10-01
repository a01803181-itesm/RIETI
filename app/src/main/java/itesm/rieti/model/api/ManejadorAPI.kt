package itesm.rieti.model.api

import itesm.rieti.model.esquemas.Reporte
import itesm.rieti.model.esquemas.Usuario
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ManejadorAPI {
    val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Config.BaseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val servicio by lazy {
        retrofit.create(Peticiones::class.java)
    }

    // GET
    suspend fun obtenerCorreo(nombre: String = ""): Usuario
    {
        val correo: Usuario = servicio.obtenerCorreo(nombre)
        return correo
    }

    suspend fun obtenerReportes(nombre: String = ""): Reporte
    {
        val reporte: Reporte = servicio.obtenerReportes(nombre)
        return reporte
    }

    // POST
    suspend fun mandarUsuario(usuario: Usuario): Usuario
    {
        return servicio.mandarUsuario(usuario)
    }

    suspend fun mandarReporte(reporte: Reporte): Reporte
    {
        return servicio.mandarReporte(reporte)
    }

    fun generarFolio(): Int
    {
        return 24578
    }
}
