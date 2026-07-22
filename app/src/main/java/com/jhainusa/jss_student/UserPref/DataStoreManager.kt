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

private val Context.dataStore by preferencesDataStore("user_prefs")

object UserSession {
    var name: String? = null
}

object UserPreferences {
    private val NAME_KEY = stringPreferencesKey("user_name")
    private val USER_ID_KEY = stringPreferencesKey("user_id")
    private val NOTIFICATIONS_ENABLED_KEY = booleanPreferencesKey("notifications_enabled")

    private val BUNK_TOOLTIP_SHOWN_KEY = booleanPreferencesKey("bunk_tooltip_shown")
    private val SWIPE_TOOLTIP_SHOWN_KEY = booleanPreferencesKey("swipe_tooltip_shown")
    private val DESIRED_ATTENDANCE_KEY = stringPreferencesKey("desired_attendance")

    private val DESIRED_ATTENDANCE_DONE = booleanPreferencesKey("desired_attendance_done")
    private val ONBOARDING_COMPLETED_KEY = booleanPreferencesKey("onboarding_completed")

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

    // --- Bunk Tooltip Feature Discovery ---
    fun isBunkTooltipShown(context: Context): Flow<Boolean> {
        return context.dataStore.data.map { prefs -> prefs[BUNK_TOOLTIP_SHOWN_KEY] ?: false }
    }

    suspend fun setBunkTooltipShown(context: Context) {
        context.dataStore.edit { prefs -> prefs[BUNK_TOOLTIP_SHOWN_KEY] = true }
    }

    // --- Swipe Tooltip Feature Discovery ---
    fun isSwipeTooltipShown(context: Context): Flow<Boolean> {
        return context.dataStore.data.map { prefs -> prefs[SWIPE_TOOLTIP_SHOWN_KEY] ?: false }
    }

    suspend fun setSwipeTooltipShown(context: Context) {
        context.dataStore.edit { prefs -> prefs[SWIPE_TOOLTIP_SHOWN_KEY] = true }
    }

    suspend fun saveDesiredAttendance(context: Context, attendance: Float) {
        context.dataStore.edit { prefs ->
            prefs[DESIRED_ATTENDANCE_KEY] = attendance.toString()
        }
    }

    fun getDesiredAttendance(context: Context): Flow<Float> {
        return context.dataStore.data.map { prefs ->
            prefs[DESIRED_ATTENDANCE_KEY]?.toFloatOrNull() ?: 75f
        }
    }
    suspend fun setDesiredAttendanceDone(context: Context, done: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[DESIRED_ATTENDANCE_DONE] = done
        }
    }

    fun isDesiredAttendanceDone(context: Context): Flow<Boolean> {
        return context.dataStore.data.map { prefs ->
            prefs[DESIRED_ATTENDANCE_DONE] ?: false
        }
    }

    suspend fun skipOnboarding(context: Context) {
        context.dataStore.edit { prefs ->
            prefs[ONBOARDING_COMPLETED_KEY] = true
            prefs[DESIRED_ATTENDANCE_DONE] = true
            prefs[DESIRED_ATTENDANCE_KEY] = "75.0"
        }
    }

    suspend fun completeDesiredAttendance(context: Context, attendance: Float) {
        context.dataStore.edit { prefs ->
            prefs[ONBOARDING_COMPLETED_KEY] = true
            prefs[DESIRED_ATTENDANCE_DONE] = true
            prefs[DESIRED_ATTENDANCE_KEY] = attendance.toString()
        }
    }

    suspend fun setOnboardingCompleted(context: Context, completed: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[ONBOARDING_COMPLETED_KEY] = completed
        }
    }

    fun isOnboardingCompleted(context: Context): Flow<Boolean> {
        return context.dataStore.data.map { prefs ->
            prefs[ONBOARDING_COMPLETED_KEY] ?: false
        }
    }
}

