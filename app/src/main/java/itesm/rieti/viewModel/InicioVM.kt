package itesm.rieti.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import itesm.rieti.model.api.ManejadorAPI
import itesm.rieti.model.esquemas.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class InicioVM : ViewModel()
{
    var correo by mutableStateOf("")
        private set

    var contrasenia by mutableStateOf("")
        private set

    private val manejador = ManejadorAPI

    private val _usuarioActual = MutableStateFlow<Usuario?>(null)
    val usuarioActual: StateFlow<Usuario?> = _usuarioActual

    private val _estadoDescargando = MutableStateFlow(false)
    val estadoDescargando: StateFlow<Boolean> = _estadoDescargando

    fun CorreoCambiado(nuevoCorreo: String)
    {
        correo = nuevoCorreo
    }

    fun ContrasenaCambiada(nuevaContrasenia: String)
    {
        contrasenia = nuevaContrasenia
    }

    fun obtenerCorreo()
    {
        viewModelScope.launch {
            _estadoDescargando.value = true
            try
            {
                val resultado = manejador.obtenerCorreo(correo)
                _usuarioActual.value = resultado
                println("Correo obtenido: ${resultado.correoU}")
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _estadoDescargando.value = false
            }
        }
    }
}