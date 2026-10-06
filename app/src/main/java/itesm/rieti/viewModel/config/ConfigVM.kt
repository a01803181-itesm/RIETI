package itesm.rieti.viewModel.config

import androidx.lifecycle.ViewModel
import itesm.rieti.viewModel.ConfigState
import itesm.rieti.viewModel.FontSize
import itesm.rieti.viewModel.Theme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ConfigVM : ViewModel() {
    val _state = MutableStateFlow(ConfigState())
    val state: StateFlow<ConfigState> = _state
    fun setTheme(theme: Theme) {
        _state.value = _state.value.copy(theme = theme)
    }
    fun setFontSize(fontSize: FontSize) {
        _state.value = _state.value.copy(fontSize = fontSize)
    }
}