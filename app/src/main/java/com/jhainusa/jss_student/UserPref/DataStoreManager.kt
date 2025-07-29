package com.jhainusa.jss_student.UserPref

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object UserPreferences {
    private val Context.dataStore by preferencesDataStore("user_prefs")
    private val NAME_KEY = stringPreferencesKey("user_name")

    suspend fun saveName(context: Context, name: String) {
        context.dataStore.edit { prefs ->
            prefs[NAME_KEY] = name
        }
    }

    fun getName(context: Context): Flow<String> {
        return context.dataStore.data
            .map { prefs -> prefs[NAME_KEY] ?: "Unknown" }
    }
}




