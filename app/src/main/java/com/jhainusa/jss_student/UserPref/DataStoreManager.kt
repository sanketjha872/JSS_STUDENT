package com.jhainusa.jss_student.UserPref

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.UUID

object UserSession {
    var name: String? = null
}
object UserPreferences {
    private val Context.dataStore by preferencesDataStore("user_prefs")
    private val NAME_KEY = stringPreferencesKey("user_name")
    private val USER_ID_KEY = stringPreferencesKey("user_id")
    private val NOTIFICATIONS_ENABLED_KEY = booleanPreferencesKey("notifications_enabled")

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

    fun getName(context: Context): Flow<String?> {
        return context.dataStore.data
            .map { prefs -> prefs[NAME_KEY]}
    }

    fun getNotificationsEnabled(context: Context): Flow<Boolean> {
        return context.dataStore.data
            .map { prefs -> prefs[NOTIFICATIONS_ENABLED_KEY] ?: true }
    }

    suspend fun setNotificationsEnabled(context: Context, enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[NOTIFICATIONS_ENABLED_KEY] = enabled
        }
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
