package itesm.rieti.viewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ConnectionVM : ViewModel() {
    val _state = MutableStateFlow(ConnectionState())
    val state: StateFlow<ConnectionState> = _state
    fun setInternetConnection(connected: Boolean) {
        _state.value = _state.value.copy(internetConnection = connected)
    }
}