package itesm.rieti.viewModel.api

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import itesm.rieti.model.api.usuarios.Manejador
import itesm.rieti.model.esquemas.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UsuariosVM : ViewModel() {
    private val _login = MutableStateFlow(false)
    val login: StateFlow<Boolean> = _login
    private val manejador = Manejador

    private val _usuarioActual = MutableStateFlow<Usuario?>(null)
    val usuarioActual: StateFlow<Usuario?> = _usuarioActual

    private val _esperando = MutableStateFlow(false)
    val esperando: StateFlow<Boolean> = _esperando

    fun obtenerUsuario(correo: String) {
        viewModelScope.launch {
            _esperando.value = true
            try {
                _usuarioActual.value = manejador.obtenerUsuario(correo)
                _login.value = true
            } catch (e: Exception) {
                e.printStackTrace()
                _login.value = false
            } finally {
                _esperando.value = false
            }
        }
    }
}