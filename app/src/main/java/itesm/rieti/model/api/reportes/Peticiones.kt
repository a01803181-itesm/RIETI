package itesm.rieti.model.api.reportes

import itesm.rieti.model.esquemas.Reporte
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Interfaz que define los endpoints de la API para los reportes.
 */
interface Peticiones {
    /**
     * Realiza una petición GET para obtener los reportes de un usuario mediante su correo.
     *
     * @param email Correo electrónico del usuario.
     * @return Una respuesta HTTP con la lista de reportes.
     */
    @GET("v1/reportes/by-user/{user_email}")
    suspend fun obtenerReporteByUser(@Path("user_email") email: String): Response<List<Reporte>>
    
    /**
     * Realiza una petición POST para crear un reporte.
     *
     * @param reporte Objeto [Reporte] con la información del reporte a guardar.
     * @return Una respuesta HTTP con la información del reporte guardado.
     */
    @POST("v1/reportes")
    suspend fun crearReporte(@Body reporte: Reporte): Response<Reporte>
}