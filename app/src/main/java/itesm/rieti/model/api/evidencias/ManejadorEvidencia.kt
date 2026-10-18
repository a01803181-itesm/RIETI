package itesm.rieti.model.api.evidencias

import itesm.rieti.model.api.ManejadorAPI
import itesm.rieti.model.esquemas.Evidencia
import okhttp3.MultipartBody
import retrofit2.Response

object ManejadorEvidencia {
    private val servicio by lazy {
        ManejadorAPI.retrofit.create(Peticiones::class.java)
    }

    suspend fun subirEvidencia(folio: String, foto: MultipartBody.Part): Response<Evidencia> {return servicio.subirEvidencia(folio, foto)}
}