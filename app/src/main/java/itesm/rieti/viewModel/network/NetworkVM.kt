package itesm.rieti.viewModel.network

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import itesm.rieti.model.network.Monitor
import itesm.rieti.model.network.NetworkMonitor
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel encargado de monitorear el estado de conectividad a la red 
 * y proveerlo de forma reactiva al resto de la aplicación.
 *
 * @param context Contexto requerido para instanciar el [Monitor] de red.
 * @param networkMonitor Instancia de [NetworkMonitor] para observar la conectividad.
 */
class NetworkVM(
    context: Context,
    val networkMonitor: NetworkMonitor = Monitor(context)
) : ViewModel() {
    val isNetworkAvailable: StateFlow<Boolean> = networkMonitor.isConnected
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )
}