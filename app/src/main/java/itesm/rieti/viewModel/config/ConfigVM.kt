package itesm.rieti.viewModel.config

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ConfigVM : ViewModel() {
    private val _state = MutableStateFlow(ConfigState())
    val state: StateFlow<ConfigState> = _state
    fun setTheme(theme: Theme) {
        _state.value = _state.value.copy(theme = theme)
    }
    fun setFontSize(fontSize: FontSize) {
        _state.value = _state.value.copy(fontSize = fontSize)
    }
}