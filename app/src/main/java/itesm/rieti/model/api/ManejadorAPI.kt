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

    // GET
    suspend fun obtenerCorreo(nombre: String = ""): Usuario? {
        val response = itesm.rieti.model.api.usuarios.Manejador.obtenerUsuario(nombre)
        return response.body()
    }

    suspend fun obtenerReportes(nombre: String = ""): Reporte? {
        val response = itesm.rieti.model.api.reportes.Manejador.obtenerReporte(nombre)
        return response.body()
    }

    // POST
    suspend fun mandarUsuario(usuario: Usuario): Usuario? {
        val response = itesm.rieti.model.api.usuarios.Manejador.crearUsuario(usuario)
        return response.body()
    }

    suspend fun mandarReporte(reporte: Reporte): Reporte? {
        val response = itesm.rieti.model.api.reportes.Manejador.crearReporte(reporte)
        return response.body()
    }

    fun generarFolio(): Int {
        return 24578
    }
}
