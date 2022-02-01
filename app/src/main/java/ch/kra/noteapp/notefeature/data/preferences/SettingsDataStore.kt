package ch.kra.noteapp.notefeature.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException


private const val IS_LINEAR_LAYOUT_MANAGER = "IS_LINEAR_LAYOUT_MANAGER"
private const val SETTINGS_DATA_STORE = "SETTINGS_DATA_STORE"

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = SETTINGS_DATA_STORE
)

class SettingsDataStore(context: Context) {
    private val settingsDataStore = context.dataStore
    private val booleanKeyLayoutManager = booleanPreferencesKey(IS_LINEAR_LAYOUT_MANAGER)

    val layoutManagerFlow: Flow<Boolean> = settingsDataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[booleanKeyLayoutManager] ?: true
        }

    suspend fun saveLayoutToPreferencesStore(isLinearLayoutManager: Boolean) {
        settingsDataStore.edit { preferences ->
            preferences[booleanKeyLayoutManager] = isLinearLayoutManager
        }
    }
}