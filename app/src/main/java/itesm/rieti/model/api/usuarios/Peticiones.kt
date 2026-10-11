package itesm.rieti.model.api.usuarios

import itesm.rieti.model.esquemas.CheckEmail
import itesm.rieti.model.esquemas.Usuario
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Interfaz que define los endpoints de la API para operaciones con usuarios.
 */
interface Peticiones {
    /**
     * Realiza una petición GET para verificar la existencia de un correo.
     *
     * @param email Correo electrónico a consultar.
     * @return Respuesta HTTP con el objeto [CheckEmail].
     */
    @GET("v1/usuarios/check-email/{email}")
    suspend fun checkEmail(@Path("email") email: String): Response<CheckEmail>
    
    /**
     * Realiza una petición POST para crear un nuevo usuario.
     *
     * @param usuario Objeto [Usuario] a registrar.
     * @return Respuesta HTTP con el usuario registrado.
     */
    @POST("v1/usuarios")
    suspend fun crearUsuario(@Body usuario: Usuario): Response<Usuario>
}