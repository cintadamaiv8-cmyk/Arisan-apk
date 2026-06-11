package com.example.utils

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "settings")

class ThemePreferences(private val context: Context) {
    companion object {
        val THEME_MODE_KEY = stringPreferencesKey("theme_mode")
        const val LIGHT = "light"
        const val DARK = "dark"
        const val SYSTEM = "system"
    }

    val themeMode: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[THEME_MODE_KEY] ?: SYSTEM
        }

    suspend fun saveThemeMode(mode: String) {
        context.dataStore.edit { preferences ->
            preferences[THEME_MODE_KEY] = mode
        }
    }
}
