package itesm.rieti.model.api.evidencias

import itesm.rieti.model.esquemas.Evidencia
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.GET


interface Peticiones {

    @Multipart
    @POST("v1/evidencias/{folio}")
    suspend fun subirEvidencia(@Path("folio") folio: String, @Part foto: MultipartBody.Part): Response<Evidencia>

    @GET("v1/evidencias/by-folio/{folio}")
    suspend fun ObtenerEvidencia (@Path("folio") folio: String): Response<List<Evidencia>>

}
