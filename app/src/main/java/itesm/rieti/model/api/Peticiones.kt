package itesm.rieti.model.api

import itesm.rieti.model.esquemas.Reporte
import itesm.rieti.model.esquemas.Usuario
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface Peticiones
{
    // endpoint: https://nyazu3k2shty7f2hmhqnrae3fq0cvdjz.lambda-url.us-east-1.on.aws/
    // GET
//    @GET("v1/usuarios/{correo}")
//    suspend fun obtenerCorreo(@Path("correo") nombre: String = ""): Usuario
//
//    @GET("reporte/{folio}")
//    suspend fun obtenerReportes(@Path("folio") nombre: String = ""): Reporte
//
//    // POST
//    @POST("usuario/")
//    suspend fun mandarUsuario(@Body usuario: Usuario): Usuario
//
//    @POST("reporte/")
//    suspend fun mandarReporte(@Body reporte: Reporte): Reporte
}