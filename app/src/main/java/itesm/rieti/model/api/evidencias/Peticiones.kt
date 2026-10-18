package itesm.rieti.model.api.evidencias

import itesm.rieti.model.esquemas.Evidencia
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface Peticiones {

    @Multipart
    @POST("v1/evidencias/{folio}")
    suspend fun subirEvidencia(@Path("folio") folio: String, @Part foto: MultipartBody.Part): Response<Evidencia>
}