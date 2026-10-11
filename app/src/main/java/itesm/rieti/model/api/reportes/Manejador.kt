package itesm.rieti.model.api.reportes

import itesm.rieti.model.api.ManejadorAPI
import itesm.rieti.model.esquemas.Reporte
import itesm.rieti.model.esquemas.Usuario
import retrofit2.Response

/**
 * Objeto que gestiona las peticiones de red relacionadas con los reportes.
 */
object Manejador
{
    private val servicio by lazy {
        ManejadorAPI.retrofit.create(Peticiones::class.java)
    }
    
    /**
     * Obtiene la lista de reportes generados por un usuario en específico.
     *
     * @param user Objeto [Usuario] del cual obtener los reportes.
     * @return Una respuesta HTTP que contiene la lista de reportes.
     */
    suspend fun obtenerReportesDeUsuario(user: Usuario): Response<List<Reporte>> {
        return servicio.obtenerReporteByUser(user.correoU)
    }
    
    /**
     * Crea y envía un nuevo reporte.
     *
     * @param reporte Objeto [Reporte] a crear.
     * @return Una respuesta HTTP que contiene el reporte creado.
     */
    suspend fun crearReporte(reporte: Reporte): Response<Reporte> {
        return servicio.crearReporte(reporte)
    }
}