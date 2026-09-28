package itesm.rieti.model.api

import itesm.rieti.model.esquemas.Reporte
import retrofit2.http.GET

interface Peticiones {
    @GET("reportes")
    suspend fun obtenerReportes(): Reporte
}