package itesm.rieti.model.api.usuarios

import itesm.rieti.model.api.ManejadorAPI
import itesm.rieti.model.esquemas.CheckEmail
import itesm.rieti.model.esquemas.Usuario
import retrofit2.Response

/**
 * Objeto que gestiona las peticiones de red para la gestión de usuarios.
 */
object Manejador {
    private val servicio by lazy {
        ManejadorAPI.retrofit.create(Peticiones::class.java)
    }
    
    /**
     * Verifica la disponibilidad o existencia de un correo electrónico en el sistema.
     *
     * @param email Correo electrónico a verificar.
     * @return Respuesta HTTP con el estado del correo.
     */
    suspend fun checkEmail(email: String): Response<CheckEmail> {
        return servicio.checkEmail(email)
    }
    
    /**
     * Crea y registra un nuevo usuario en la base de datos a través de la API.
     *
     * @param usuario Objeto [Usuario] con la información a registrar.
     * @return Respuesta HTTP con la información del usuario creado.
     */
    suspend fun crearUsuario(usuario: Usuario): Response<Usuario> {
        return servicio.crearUsuario(usuario)
    }
}