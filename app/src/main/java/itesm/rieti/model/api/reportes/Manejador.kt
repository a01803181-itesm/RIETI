package itesm.rieti.model.api.reportes

import itesm.rieti.model.api.ManejadorAPI
import itesm.rieti.model.esquemas.Reporte
import itesm.rieti.model.esquemas.Usuario
import retrofit2.Response

object Manejador
{
    private val servicio by lazy {
        ManejadorAPI.retrofit.create(Peticiones::class.java)
    }
    suspend fun obtenerReportesDeUsuario(user: Usuario): Response<List<Reporte>> {
        return servicio.obtenerReporteByUser(user.correoU)
    }
    suspend fun crearReporte(reporte: Reporte): Response<Reporte> {
        return servicio.crearReporte(reporte)
    }
}