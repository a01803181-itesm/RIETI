package itesm.rieti.viewModel.config

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * ViewModel encargado de manejar las preferencias de configuración de la interfaz, 
 * tales como el tema y el tamaño de letra.
 */
class ConfigVM : ViewModel() {
    private val _state = MutableStateFlow(ConfigState())
    
    /**
     * Estado observable que expone la configuración actual de la aplicación.
     */
    val state: StateFlow<ConfigState> = _state

    /**
     * Actualiza el tema de la aplicación en el estado.
     *
     * @param theme Nuevo [Theme] a aplicar.
     */
    fun setTheme(theme: Theme) {
        _state.value = _state.value.copy(theme = theme)
    }

    /**
     * Actualiza el tamaño de la fuente en el estado.
     *
     * @param fontSize Nuevo [FontSize] a utilizar en la aplicación.
     */
    fun setFontSize(fontSize: FontSize) {
        _state.value = _state.value.copy(fontSize = fontSize)
    }
}
