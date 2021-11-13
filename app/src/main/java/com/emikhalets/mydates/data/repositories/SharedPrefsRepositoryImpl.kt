package com.emikhalets.mydates.data.repositories

import android.content.Context
import android.content.SharedPreferences
import com.emikhalets.mydates.utils.enums.DEFAULT_APP_LANGUAGE_CODE
import com.emikhalets.mydates.utils.enums.DEFAULT_NOTIFICATION_HOUR
import com.emikhalets.mydates.utils.enums.DEFAULT_NOTIFICATION_MINUTE
import javax.inject.Inject

class SharedPrefsRepositoryImpl @Inject constructor(
    private val context: Context,
) : AppPrefsRepository {

    private val sharedPreferences: SharedPreferences
        get() = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)

    override fun getAppFirstLaunch(): Boolean {
        return sharedPreferences.getBoolean(APP_FIRST_LAUNCH, true)
    }

    override fun setAppFirstLaunch(value: Boolean) {
        sharedPreferences.edit().putBoolean(APP_FIRST_LAUNCH, value).apply()
    }

    override fun getEventsLastUpdateTime(): Long {
        return sharedPreferences.getLong(EVENTS_UPDATE_TIME, 0)
    }

    override fun setEventsLastUpdateTime(value: Long) {
        sharedPreferences.edit().putLong(EVENTS_UPDATE_TIME, value).apply()
    }

    override fun getNotificationHour(): Int {
        return sharedPreferences.getInt(NOTIFICATION_HOUR, DEFAULT_NOTIFICATION_HOUR)
    }

    override fun setNotificationHour(value: Int) {
        sharedPreferences.edit().putInt(NOTIFICATION_HOUR, value).apply()
    }

    override fun getNotificationMinute(): Int {
        return sharedPreferences.getInt(NOTIFICATION_MINUTE, DEFAULT_NOTIFICATION_MINUTE)
    }

    override fun setNotificationMinute(value: Int) {
        sharedPreferences.edit().putInt(NOTIFICATION_MINUTE, value).apply()
    }

    override fun getNotificationAll(): Boolean {
        return sharedPreferences.getBoolean(NOTIFICATION_ALL, true)
    }

    override fun setNotificationAll(value: Boolean) {
        sharedPreferences.edit().putBoolean(NOTIFICATION_ALL, value).apply()
    }

    override fun getNotificationMonth(): Boolean {
        return sharedPreferences.getBoolean(NOTIFICATION_MONTH, true)
    }

    override fun setNotificationMonth(value: Boolean) {
        sharedPreferences.edit().putBoolean(NOTIFICATION_MONTH, value).apply()
    }

    override fun getNotificationWeek(): Boolean {
        return sharedPreferences.getBoolean(NOTIFICATION_WEEK, true)
    }

    override fun setNotificationWeek(value: Boolean) {
        sharedPreferences.edit().putBoolean(NOTIFICATION_WEEK, value).apply()
    }

    override fun getNotificationThreeDay(): Boolean {
        return sharedPreferences.getBoolean(NOTIFICATION_THREE_DAY, true)
    }

    override fun setNotificationThreeDay(value: Boolean) {
        sharedPreferences.edit().putBoolean(NOTIFICATION_THREE_DAY, value).apply()
    }

    override fun getNotificationTwoDay(): Boolean {
        return sharedPreferences.getBoolean(NOTIFICATION_TWO_DAY, true)
    }

    override fun setNotificationTwoDay(value: Boolean) {
        sharedPreferences.edit().putBoolean(NOTIFICATION_TWO_DAY, value).apply()
    }

    override fun getNotificationDay(): Boolean {
        return sharedPreferences.getBoolean(NOTIFICATION_DAY, true)
    }

    override fun setNotificationDay(value: Boolean) {
        sharedPreferences.edit().putBoolean(NOTIFICATION_DAY, value).apply()
    }

    override fun getNotificationToday(): Boolean {
        return sharedPreferences.getBoolean(NOTIFICATION_TODAY, true)
    }

    override fun setNotificationToday(value: Boolean) {
        sharedPreferences.edit().putBoolean(NOTIFICATION_TODAY, value).apply()
    }

    override fun getTheme(): Int {
        return sharedPreferences.getInt(THEME, 0)
    }

    override fun setTheme(value: Int) {
        sharedPreferences.edit().putInt(THEME, value).apply()
    }

    override fun getLanguage(): String {
        return sharedPreferences.getString(LANGUAGE, DEFAULT_APP_LANGUAGE_CODE)
            ?: DEFAULT_APP_LANGUAGE_CODE
    }

    override fun setLanguage(value: String) {
        sharedPreferences.edit().putString(LANGUAGE, value).apply()
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