package com.emikhalets.mydates.data.repositories

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.emikhalets.mydates.utils.DEFAULT_APP_LANGUAGE_CODE
import com.emikhalets.mydates.utils.DEFAULT_NOTIFICATION_HOUR
import com.emikhalets.mydates.utils.DEFAULT_NOTIFICATION_MINUTE
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreRepositoryImpl @Inject constructor(
    private val context: Context,
) : AppPrefsRepository {

    private val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = NAME)

    override suspend fun getAppFirstLaunch(): Boolean {
        return context.datastore.data.map { preferences ->
            preferences[APP_FIRST_LAUNCH] ?: false
        }.first()
    }

    override suspend fun setAppFirstLaunch(value: Boolean) {
        context.datastore.edit { preferences ->
            preferences[APP_FIRST_LAUNCH] = value
        }
    }

    override suspend fun getEventsLastUpdateTime(): Long {
        return context.datastore.data.map { preferences ->
            preferences[EVENTS_UPDATE_TIME] ?: 0
        }.first()
    }

    override suspend fun setEventsLastUpdateTime(value: Long) {
        context.datastore.edit { preferences ->
            preferences[EVENTS_UPDATE_TIME] = value
        }
    }

    override suspend fun getNotificationHour(): Int {
        return context.datastore.data.map { preferences ->
            preferences[NOTIFICATION_HOUR] ?: DEFAULT_NOTIFICATION_HOUR
        }.first()
    }

    override suspend fun setNotificationHour(value: Int) {
        context.datastore.edit { preferences ->
            preferences[NOTIFICATION_HOUR] = value
        }
    }

    override suspend fun getNotificationMinute(): Int {
        return context.datastore.data.map { preferences ->
            preferences[NOTIFICATION_MINUTE] ?: DEFAULT_NOTIFICATION_MINUTE
        }.first()
    }

    override suspend fun setNotificationMinute(value: Int) {
        context.datastore.edit { preferences ->
            preferences[NOTIFICATION_MINUTE] = value
        }
    }

    override suspend fun getNotificationAll(): Boolean {
        return context.datastore.data.map { preferences ->
            preferences[NOTIFICATION_ALL] ?: true
        }.first()
    }

    override suspend fun setNotificationAll(value: Boolean) {
        context.datastore.edit { preferences ->
            preferences[NOTIFICATION_ALL] = value
        }
    }

    override suspend fun getNotificationMonth(): Boolean {
        return context.datastore.data.map { preferences ->
            preferences[NOTIFICATION_MONTH] ?: true
        }.first()
    }

    override suspend fun setNotificationMonth(value: Boolean) {
        context.datastore.edit { preferences ->
            preferences[NOTIFICATION_MONTH] = value
        }
    }

    override suspend fun getNotificationWeek(): Boolean {
        return context.datastore.data.map { preferences ->
            preferences[NOTIFICATION_WEEK] ?: true
        }.first()
    }

    override suspend fun setNotificationWeek(value: Boolean) {
        context.datastore.edit { preferences ->
            preferences[NOTIFICATION_WEEK] = value
        }
    }

    override suspend fun getNotificationThreeDay(): Boolean {
        return context.datastore.data.map { preferences ->
            preferences[NOTIFICATION_THREE_DAY] ?: true
        }.first()
    }

    override suspend fun setNotificationThreeDay(value: Boolean) {
        context.datastore.edit { preferences ->
            preferences[NOTIFICATION_THREE_DAY] = value
        }
    }

    override suspend fun getNotificationTwoDay(): Boolean {
        return context.datastore.data.map { preferences ->
            preferences[NOTIFICATION_TWO_DAY] ?: true
        }.first()
    }

    override suspend fun setNotificationTwoDay(value: Boolean) {
        context.datastore.edit { preferences ->
            preferences[NOTIFICATION_TWO_DAY] = value
        }
    }

    override suspend fun getNotificationDay(): Boolean {
        return context.datastore.data.map { preferences ->
            preferences[NOTIFICATION_DAY] ?: true
        }.first()
    }

    override suspend fun setNotificationDay(value: Boolean) {
        context.datastore.edit { preferences ->
            preferences[NOTIFICATION_DAY] = value
        }
    }

    override suspend fun getNotificationToday(): Boolean {
        return context.datastore.data.map { preferences ->
            preferences[NOTIFICATION_TODAY] ?: true
        }.first()
    }

    override suspend fun setNotificationToday(value: Boolean) {
        context.datastore.edit { preferences ->
            preferences[NOTIFICATION_TODAY] = value
        }
    }

    override suspend fun getTheme(): Int {
        return context.datastore.data.map { preferences ->
            preferences[THEME] ?: 0
        }.first()
    }

    override suspend fun setTheme(value: Int) {
        context.datastore.edit { preferences ->
            preferences[THEME] = value
        }
    }

    override suspend fun getLanguage(): String {
        return context.datastore.data.map { preferences ->
            preferences[LANGUAGE] ?: DEFAULT_APP_LANGUAGE_CODE
        }.first()
    }

    override suspend fun setLanguage(value: String) {
        context.datastore.edit { preferences ->
            preferences[LANGUAGE] = value
        }
    }

    private companion object {
        const val NAME = "com.emikhalets.mydates.app_data_store"
        val APP_FIRST_LAUNCH = booleanPreferencesKey("com.emikhalets.mydates.app_first_launch")
        val EVENTS_UPDATE_TIME = longPreferencesKey("com.emikhalets.mydates.events_update_time")
        val NOTIFICATION_HOUR = intPreferencesKey("com.emikhalets.mydates.notification_hour")
        val NOTIFICATION_MINUTE = intPreferencesKey("com.emikhalets.mydates.notification_minute")
        val NOTIFICATION_ALL = booleanPreferencesKey("com.emikhalets.mydates.notification_all")
        val NOTIFICATION_MONTH = booleanPreferencesKey("com.emikhalets.mydates.notification_month")
        val NOTIFICATION_WEEK = booleanPreferencesKey("com.emikhalets.mydates.notification_week")
        val NOTIFICATION_THREE_DAY = booleanPreferencesKey("com.emikhalets.mydates.notification_three_day")
        val NOTIFICATION_TWO_DAY = booleanPreferencesKey("com.emikhalets.mydates.notification_two_day")
        val NOTIFICATION_DAY = booleanPreferencesKey("com.emikhalets.mydates.notification_day")
        val NOTIFICATION_TODAY = booleanPreferencesKey("com.emikhalets.mydates.notification_today")
        val THEME = intPreferencesKey("com.emikhalets.mydates.theme")
        val LANGUAGE = stringPreferencesKey("com.emikhalets.mydates.language")
    }
}