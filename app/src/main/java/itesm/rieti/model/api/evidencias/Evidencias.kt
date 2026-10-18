package itesm.rieti.model.api.evidencias

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import android.content.Context
import android.net.Uri
import okhttp3.RequestBody.Companion.toRequestBody

class Evidencias {

    fun prepararEvidencia(context: Context, uri: Uri): MultipartBody.Part {

        val bytes = context.contentResolver
            .openInputStream(uri)
            ?.use { it.readBytes() }
            ?: throw IllegalArgumentException(
                "No se pudo leer la fotografía"
            )

        require(bytes.isNotEmpty()) {
            "La fotografía está vacía"
        }

        val requestBody = bytes.toRequestBody(
            "image/jpeg".toMediaType()
        )

        return MultipartBody.Part.createFormData(
            "foto",
            "evidencia.jpg",
            requestBody
        )
    }
}