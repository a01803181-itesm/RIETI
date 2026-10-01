package itesm.rieti.model.api.usuarios

import itesm.rieti.model.esquemas.Usuario
import retrofit2.http.GET
import retrofit2.http.Path

interface Peticiones {
    @GET("v1/usuarios/{user_email}")
    suspend fun obtenerUsuario(@Path("user_email") correo: String): Usuario

    @GET
    suspend fun obtenerUsuarios(): List<Usuario>
}