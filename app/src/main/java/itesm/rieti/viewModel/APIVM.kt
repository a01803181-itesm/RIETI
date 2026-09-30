package itesm.rieti.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import itesm.rieti.model.api.ManejadorAPI
import itesm.rieti.model.esquemas.Reporte
import itesm.rieti.model.esquemas.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class APIVM : ViewModel()
{
    private val manejador = ManejadorAPI

    private val _usuarioActual = MutableStateFlow<Usuario?>(null)
    val usuarioActual: StateFlow<Usuario?> = _usuarioActual

    private val _reporteActual = MutableStateFlow<Reporte?>(null)
    val reporteActual: StateFlow<Reporte?> = _reporteActual

    private val _estadoDescargando = MutableStateFlow(false)
    val estadoDescargando: StateFlow<Boolean> = _estadoDescargando

    fun obtenerCorreo(correo: String)
    {
        viewModelScope.launch {
            _estadoDescargando.value = true
            try
            {
                val resultado = manejador.obtenerCorreo(correo)
                _usuarioActual.value = resultado
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _estadoDescargando.value = false
            }
        }
    }

    fun obtenerReporte(folio: String)
    {
        viewModelScope.launch {
            _estadoDescargando.value = true
            try
            {
                val resultado = manejador.obtenerReportes(folio)
                _reporteActual.value = resultado
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _estadoDescargando.value = false
            }
        }
    }

    fun registrarUsuario(usuario: Usuario)
    {
        viewModelScope.launch {
            _estadoDescargando.value = true
            try
            {
                val resultado = manejador.mandarUsuario(usuario)
                _usuarioActual.value = resultado
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _estadoDescargando.value = false
            }
        }
    }

    fun mandarReporte(reporte: Reporte)
    {
        viewModelScope.launch {
            _estadoDescargando.value = true
            try
            {
                val resultado = manejador.mandarReporte(reporte)
                _reporteActual.value = resultado
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _estadoDescargando.value = false
            }
        }
    }
}