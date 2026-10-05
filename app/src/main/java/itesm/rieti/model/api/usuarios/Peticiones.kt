package itesm.rieti.model.api.usuarios

import itesm.rieti.model.esquemas.CheckEmail
import itesm.rieti.model.esquemas.Usuario
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface Peticiones {
    @GET("v1/usuarios/{user_email}")
    suspend fun obtenerUsuario(@Path("user_email") correo: String): Response<Usuario>
    @GET("v1/usuarios/check-email/{email}")
    suspend fun checkEmail(@Path("email") email: String): Response<CheckEmail>
    @POST("v1/usuarios")
    suspend fun crearUsuario(@Body usuario: Usuario): Response<Usuario>
}