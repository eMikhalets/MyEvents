package com.emikhalets.mydates.data.repositories

import android.content.Context
import android.content.SharedPreferences
import com.emikhalets.mydates.utils.enums.DEFAULT_APP_LANGUAGE_CODE
import com.emikhalets.mydates.utils.enums.DEFAULT_NOTIFICATION_HOUR
import com.emikhalets.mydates.utils.enums.DEFAULT_NOTIFICATION_MINUTE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SharedPrefsRepositoryImpl @Inject constructor(
    private val context: Context,
) : AppPrefsRepository {

    private val sharedPreferences: SharedPreferences
        get() = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)

    override suspend fun getAppFirstLaunch(): Boolean {
        return withContext(Dispatchers.IO) {
            sharedPreferences.getBoolean(APP_FIRST_LAUNCH, true)
        }
    }

    override suspend fun setAppFirstLaunch(value: Boolean) {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit().putBoolean(APP_FIRST_LAUNCH, value).apply()
        }
    }

    override suspend fun getEventsLastUpdateTime(): Long {
        return withContext(Dispatchers.IO) {
            sharedPreferences.getLong(EVENTS_UPDATE_TIME, 0)
        }
    }

    override suspend fun setEventsLastUpdateTime(value: Long) {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit().putLong(EVENTS_UPDATE_TIME, value).apply()
        }
    }

    override suspend fun getNotificationHour(): Int {
        return withContext(Dispatchers.IO) {
            sharedPreferences.getInt(NOTIFICATION_HOUR, DEFAULT_NOTIFICATION_HOUR)
        }
    }

    override suspend fun setNotificationHour(value: Int) {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit().putInt(NOTIFICATION_HOUR, value).apply()
        }
    }

    override suspend fun getNotificationMinute(): Int {
        return withContext(Dispatchers.IO) {
            sharedPreferences.getInt(NOTIFICATION_MINUTE, DEFAULT_NOTIFICATION_MINUTE)
        }
    }

    override suspend fun setNotificationMinute(value: Int) {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit().putInt(NOTIFICATION_MINUTE, value).apply()
        }
    }

    override suspend fun getNotificationAll(): Boolean {
        return withContext(Dispatchers.IO) {
            sharedPreferences.getBoolean(NOTIFICATION_ALL, true)
        }
    }

    override suspend fun setNotificationAll(value: Boolean) {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit().putBoolean(NOTIFICATION_ALL, value).apply()
        }
    }

    override suspend fun getNotificationMonth(): Boolean {
        return withContext(Dispatchers.IO) {
            sharedPreferences.getBoolean(NOTIFICATION_MONTH, true)
        }
    }

    override suspend fun setNotificationMonth(value: Boolean) {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit().putBoolean(NOTIFICATION_MONTH, value).apply()
        }
    }

    override suspend fun getNotificationWeek(): Boolean {
        return withContext(Dispatchers.IO) {
            sharedPreferences.getBoolean(NOTIFICATION_WEEK, true)
        }
    }

    override suspend fun setNotificationWeek(value: Boolean) {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit().putBoolean(NOTIFICATION_WEEK, value).apply()
        }
    }

    override suspend fun getNotificationThreeDay(): Boolean {
        return withContext(Dispatchers.IO) {
            sharedPreferences.getBoolean(NOTIFICATION_THREE_DAY, true)
        }
    }

    override suspend fun setNotificationThreeDay(value: Boolean) {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit().putBoolean(NOTIFICATION_THREE_DAY, value).apply()
        }
    }

    override suspend fun getNotificationTwoDay(): Boolean {
        return withContext(Dispatchers.IO) {
            sharedPreferences.getBoolean(NOTIFICATION_TWO_DAY, true)
        }
    }

    override suspend fun setNotificationTwoDay(value: Boolean) {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit().putBoolean(NOTIFICATION_TWO_DAY, value).apply()
        }
    }

    override suspend fun getNotificationDay(): Boolean {
        return withContext(Dispatchers.IO) {
            sharedPreferences.getBoolean(NOTIFICATION_DAY, true)
        }
    }

    override suspend fun setNotificationDay(value: Boolean) {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit().putBoolean(NOTIFICATION_DAY, value).apply()
        }
    }

    override suspend fun getNotificationToday(): Boolean {
        return withContext(Dispatchers.IO) {
            sharedPreferences.getBoolean(NOTIFICATION_TODAY, true)
        }
    }

    override suspend fun setNotificationToday(value: Boolean) {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit().putBoolean(NOTIFICATION_TODAY, value).apply()
        }
    }

    override suspend fun getTheme(): Int {
        return withContext(Dispatchers.IO) {
            sharedPreferences.getInt(THEME, 0)
        }
    }

    override suspend fun setTheme(value: Int) {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit().putInt(THEME, value).apply()
        }
    }

    override suspend fun getLanguage(): String {
        return withContext(Dispatchers.IO) {
            sharedPreferences.getString(LANGUAGE, DEFAULT_APP_LANGUAGE_CODE)
                ?: DEFAULT_APP_LANGUAGE_CODE
        }
    }

    override suspend fun setLanguage(value: String) {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit().putString(LANGUAGE, value).apply()
        }
    }

    private companion object {
        const val NAME = "com.emikhalets.mydates.app_shared_preferences"
        const val APP_FIRST_LAUNCH = "com.emikhalets.mydates.app_first_launch"
        const val EVENTS_UPDATE_TIME = "com.emikhalets.mydates.events_update_time"
        const val NOTIFICATION_HOUR = "com.emikhalets.mydates.notification_hour"
        const val NOTIFICATION_MINUTE = "com.emikhalets.mydates.notification_minute"
        const val NOTIFICATION_ALL = "com.emikhalets.mydates.notification_all"
        const val NOTIFICATION_MONTH = "com.emikhalets.mydates.notification_month"
        const val NOTIFICATION_WEEK = "com.emikhalets.mydates.notification_week"
        const val NOTIFICATION_THREE_DAY = "com.emikhalets.mydates.notification_three_day"
        const val NOTIFICATION_TWO_DAY = "com.emikhalets.mydates.notification_two_day"
        const val NOTIFICATION_DAY = "com.emikhalets.mydates.notification_day"
        const val NOTIFICATION_TODAY = "com.emikhalets.mydates.notification_today"
        const val THEME = "com.emikhalets.mydates.theme"
        const val LANGUAGE = "com.emikhalets.mydates.language"
    }
}