package itesm.rieti.model.api.reportes

import itesm.rieti.model.esquemas.Reporte
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface Peticiones {
    @GET("v1/reportes/{folio}")
    suspend fun obtenerReporte(@Path("folio") folio: String): Response<Reporte>
    @POST("v1/reportes")
    suspend fun crearReporte(@Body reporte: Reporte): Response<Reporte>
}