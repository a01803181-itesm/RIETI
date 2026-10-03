package itesm.rieti.model.api.usuarios

import itesm.rieti.model.api.ManejadorAPI
import itesm.rieti.model.esquemas.Usuario
import retrofit2.Response

object Manejador {
    private val servicio by lazy {
        ManejadorAPI.retrofit.create(Peticiones::class.java)
    }

    suspend fun obtenerUsuario(correo: String): Response<Usuario> {
        return servicio.obtenerUsuario(correo)
    }

    suspend fun crearUsuario(usuario: Usuario): Response<Usuario> {
        return servicio.crearUsuario(usuario)
    }
}