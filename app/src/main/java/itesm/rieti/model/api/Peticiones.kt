package itesm.rieti.model.api

import itesm.rieti.model.esquemas.Reporte
import itesm.rieti.model.esquemas.Usuario
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface Peticiones
{
    @GET("usuario/{correo}")
    suspend fun obtenerCorreo(@Path("correo") nombre: String = ""): Usuario

    @GET("reporte/{folio}")
    suspend fun obtenerReportes(@Path("folio") nombre: String = ""): Reporte

    @POST("usuario/{cuenta}")
    suspend fun mandarUsuario(@Path("cuenta") nombre: String = ""): Usuario

    @POST("reporte/{folio}")
    suspend fun mandarReporte(@Path("folio") nombre: String = ""): Reporte
}