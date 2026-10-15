package itesm.rieti.model.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import itesm.rieti.model.esquemas.Usuario
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "user_preferences")

class UserPreferences(private val context: Context) {
    companion object {
        val THEME_KEY = stringPreferencesKey("theme")
        val FONT_SIZE_KEY = stringPreferencesKey("font_size")
        val OFFLINE_EMAIL_KEY = stringPreferencesKey("offline_email")
        val OFFLINE_PROVIDER_KEY = stringPreferencesKey("offline_provider")
        val OFFLINE_SUB_KEY = stringPreferencesKey("offline_sub")
    }

    val themeFlow: Flow<String?> = context.dataStore.data.map { it[THEME_KEY] }
    val fontSizeFlow: Flow<String?> = context.dataStore.data.map { it[FONT_SIZE_KEY] }
    val offlineEmailFlow: Flow<String?> = context.dataStore.data.map { it[OFFLINE_EMAIL_KEY] }
    val offlineProviderFlow: Flow<String?> = context.dataStore.data.map { it[OFFLINE_PROVIDER_KEY] }
    val offlineSUBFlow: Flow<String?> = context.dataStore.data.map { it[OFFLINE_SUB_KEY] }

    suspend fun saveTheme(theme: String) { context.dataStore.edit { it[THEME_KEY] = theme } }
    suspend fun saveFontSize(fontSize: String) { context.dataStore.edit { it[FONT_SIZE_KEY] = fontSize } }
    suspend fun saveUser(user: Usuario, sub: String) {
        context.dataStore.edit {
            it[OFFLINE_EMAIL_KEY] = user.correoU
            it[OFFLINE_PROVIDER_KEY] = user.proveedor.toString()
            it[OFFLINE_SUB_KEY] = sub
        }
    }
    suspend fun clearOfflineUser() {
        context.dataStore.edit {
            it.remove(OFFLINE_EMAIL_KEY)
            it.remove(OFFLINE_PROVIDER_KEY)
            it.remove(OFFLINE_SUB_KEY)
        }
    }
}