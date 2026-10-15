package itesm.rieti.viewModel.config

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import itesm.rieti.model.preferences.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel encargado de manejar las preferencias de configuración de la interfaz, 
 * tales como el tema y el tamaño de letra.
 */
class ConfigVM(application: Application) : AndroidViewModel(application) {
    private val preferences = UserPreferences(application)
    private val _state = MutableStateFlow(ConfigState())
    
    /**
     * Estado observable que expone la configuración actual de la aplicación.
     */
    val state: StateFlow<ConfigState> = _state

    init {
        viewModelScope.launch {
            preferences.themeFlow.collect { theme ->
                theme?.let { _state.value = _state.value.copy(theme = Theme.valueOf(it)) }
            }
        }
        viewModelScope.launch {
            preferences.fontSizeFlow.collect { fontSize ->
                fontSize?.let { _state.value = _state.value.copy(fontSize = FontSize.valueOf(it)) }
            }
        }
    }

    /**
     * Actualiza el tema de la aplicación en el estado.
     *
     * @param theme Nuevo [Theme] a aplicar.
     */
    fun setTheme(theme: Theme) {
        _state.value = _state.value.copy(theme = theme)
        viewModelScope.launch { preferences.saveTheme(theme.name) }
    }

    /**
     * Actualiza el tamaño de la fuente en el estado.
     *
     * @param fontSize Nuevo [FontSize] a utilizar en la aplicación.
     */
    fun setFontSize(fontSize: FontSize) {
        _state.value = _state.value.copy(fontSize = fontSize)
        viewModelScope.launch { preferences.saveFontSize(fontSize.name) }
    }
}
