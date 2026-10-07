package itesm.rieti.model.api.reportes

import itesm.rieti.model.api.ManejadorAPI
import itesm.rieti.model.esquemas.Reporte
import retrofit2.Response

object Manejador
{
    private val servicio by lazy {
        ManejadorAPI.retrofit.create(Peticiones::class.java)
    }
    suspend fun obtenerReportesDeUsuario(email: String): Response<List<Reporte>> {
        return servicio.obtenerReporteByUser(email)
    }
    suspend fun obtenerReporte(folio: String): Response<Reporte> {
        return servicio.obtenerReporte(folio)
    }
    suspend fun crearReporte(reporte: Reporte): Response<Reporte> {
        return servicio.crearReporte(reporte)
    }
}