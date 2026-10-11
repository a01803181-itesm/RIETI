package itesm.rieti.viewModel.network

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import itesm.rieti.model.network.Monitor
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel encargado de monitorear el estado de conectividad a la red 
 * y proveerlo de forma reactiva al resto de la aplicación.
 *
 * @param context Contexto requerido para instanciar el [Monitor] de red.
 */
class NetworkVM(context: Context) : ViewModel() {
    /**
     * Monitor de conectividad que evalúa cambios en el estado de red.
     */
    val networkMonitor = Monitor(context)
    
    /**
     * Estado observable que indica si existe o no una conexión a internet disponible de manera continua.
     */
    val isNetworkAvailable: StateFlow<Boolean> = networkMonitor.isConnected
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )
}