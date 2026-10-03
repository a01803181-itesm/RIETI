package itesm.rieti.viewModel.api

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import itesm.rieti.model.api.usuarios.Manejador
import itesm.rieti.model.esquemas.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UsuarioVM : ViewModel() {
    private val manejadorUsuarios = Manejador
    private val _loggedIn = MutableStateFlow(false)
    val loggedIn: StateFlow<Boolean> = _loggedIn
    private val _usuarioActual = MutableStateFlow<Usuario?>(null)
    val usuarioActual: StateFlow<Usuario?> = _usuarioActual
    private val _esperando = MutableStateFlow(false)
    val esperando: StateFlow<Boolean> = _esperando
    fun actualizarUsuario(usuario: Usuario) {
        _usuarioActual.value = usuario
    }

    fun obtenerUsuario(correo: String) {
        viewModelScope.launch {
            _esperando.value = true
            try {
                val response = manejadorUsuarios.obtenerUsuario(correo)
                if (response.isSuccessful) {
                    val usuario = response.body()
                } else {
                    // Manejar errores de respuesta
                }
                _loggedIn.value = true
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _esperando.value = false
            }
        }
    }
}