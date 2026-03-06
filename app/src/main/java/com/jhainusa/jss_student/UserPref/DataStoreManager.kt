package com.jhainusa.jss_student.UserPref

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.UUID

object UserPreferences {
    private val Context.dataStore by preferencesDataStore("user_prefs")
    private val NAME_KEY = stringPreferencesKey("user_name")
    private val USER_ID_KEY = stringPreferencesKey("user_id")

    suspend fun saveName(context: Context, name: String) {
        context.dataStore.edit { prefs ->
            prefs[NAME_KEY] = name
            
            // Prepend name to the existing UUID or generate a new one
            val currentId = prefs[USER_ID_KEY]
            val uuid = if (currentId != null && currentId.contains("_")) {
                currentId.substringAfter("_")
            } else {
                UUID.randomUUID().toString()
            }
            prefs[USER_ID_KEY] = "${name}_$uuid"
        }
    }

    fun getName(context: Context): Flow<String> {
        return context.dataStore.data
            .map { prefs -> prefs[NAME_KEY] ?: "Unknown" }
    }

    suspend fun getOrCreateUserId(context: Context): String {
        val prefs = context.dataStore.data.first()
        val existingId = prefs[USER_ID_KEY]
        
        return if (existingId != null) {
            existingId
        } else {
            val name = prefs[NAME_KEY] ?: "Unknown"
            val newId = "${name}_${UUID.randomUUID()}"
            context.dataStore.edit { settings ->
                settings[USER_ID_KEY] = newId
            }
            newId
        }
    }
}
