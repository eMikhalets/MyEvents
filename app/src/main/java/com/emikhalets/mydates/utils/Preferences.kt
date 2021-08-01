package com.emikhalets.mydates.utils

import android.content.Context

object Preferences {

    private const val NAME = "com.emikhalets.mydates.shared_preferences"
    private const val APP_FIRST_LAUNCH = "com.emikhalets.mydates.app_first_launch"
    private const val EVENTS_UPDATE_TIME = "com.emikhalets.mydates.events_update_time"
    private const val NOTIFICATION_HOUR = "com.emikhalets.mydates.notification_hour"
    private const val NOTIFICATION_MINUTE = "com.emikhalets.mydates.notification_minute"
    private const val NOTIFICATION_ALL = "com.emikhalets.mydates.notification_all"
    private const val NOTIFICATION_MONTH = "com.emikhalets.mydates.notification_month"
    private const val NOTIFICATION_WEEK = "com.emikhalets.mydates.notification_week"
    private const val NOTIFICATION_THREE_DAY = "com.emikhalets.mydates.notification_three_day"
    private const val NOTIFICATION_TWO_DAY = "com.emikhalets.mydates.notification_two_day"
    private const val NOTIFICATION_DAY = "com.emikhalets.mydates.notification_day"
    private const val NOTIFICATION_TODAY = "com.emikhalets.mydates.notification_today"

    fun getAppFirstLaunch(context: Context) =
        context.getSharedPreferences(NAME, 0).getBoolean(APP_FIRST_LAUNCH, true)

    fun setAppFirstLaunch(context: Context, value: Boolean) =
        context.getSharedPreferences(NAME, 0).edit().putBoolean(APP_FIRST_LAUNCH, value).apply()

    fun getEventsLastUpdateTime(context: Context) =
        context.getSharedPreferences(NAME, 0).getLong(EVENTS_UPDATE_TIME, 0)

    fun setEventsLastUpdateTime(context: Context, value: Long) =
        context.getSharedPreferences(NAME, 0).edit().putLong(EVENTS_UPDATE_TIME, value).apply()

    fun getNotificationHour(context: Context) =
        context.getSharedPreferences(NAME, 0).getInt(NOTIFICATION_HOUR, 0)

    fun setNotificationHour(context: Context, value: Int) =
        context.getSharedPreferences(NAME, 0).edit().putInt(NOTIFICATION_HOUR, value).apply()

    fun getNotificationMinute(context: Context) =
        context.getSharedPreferences(NAME, 0).getInt(NOTIFICATION_MINUTE, 0)

    fun setNotificationMinute(context: Context, value: Int) =
        context.getSharedPreferences(NAME, 0).edit().putInt(NOTIFICATION_MINUTE, value).apply()

    fun getNotificationAll(context: Context) =
        context.getSharedPreferences(NAME, 0).getBoolean(NOTIFICATION_ALL, true)

    fun setNotificationAll(context: Context, value: Boolean) =
        context.getSharedPreferences(NAME, 0).edit().putBoolean(NOTIFICATION_ALL, value).apply()

    fun getNotificationMonth(context: Context) =
        context.getSharedPreferences(NAME, 0).getBoolean(NOTIFICATION_MONTH, true)

    fun setNotificationMonth(context: Context, value: Boolean) =
        context.getSharedPreferences(NAME, 0).edit().putBoolean(NOTIFICATION_MONTH, value).apply()

    fun getNotificationWeek(context: Context) =
        context.getSharedPreferences(NAME, 0).getBoolean(NOTIFICATION_WEEK, true)

    fun setNotificationWeek(context: Context, value: Boolean) =
        context.getSharedPreferences(NAME, 0).edit().putBoolean(NOTIFICATION_WEEK, value).apply()

    fun getNotificationThreeDay(context: Context) =
        context.getSharedPreferences(NAME, 0).getBoolean(NOTIFICATION_THREE_DAY, true)

    fun setNotificationThreeDay(context: Context, value: Boolean) =
        context.getSharedPreferences(NAME, 0).edit().putBoolean(NOTIFICATION_THREE_DAY, value)
            .apply()

    fun getNotificationTwoDay(context: Context) =
        context.getSharedPreferences(NAME, 0).getBoolean(NOTIFICATION_TWO_DAY, true)

    fun setNotificationTwoDay(context: Context, value: Boolean) =
        context.getSharedPreferences(NAME, 0).edit().putBoolean(NOTIFICATION_TWO_DAY, value).apply()

    fun getNotificationDay(context: Context) =
        context.getSharedPreferences(NAME, 0).getBoolean(NOTIFICATION_DAY, true)

    fun setNotificationDay(context: Context, value: Boolean) =
        context.getSharedPreferences(NAME, 0).edit().putBoolean(NOTIFICATION_DAY, value).apply()

    fun getNotificationToday(context: Context) =
        context.getSharedPreferences(NAME, 0).getBoolean(NOTIFICATION_TODAY, true)

    fun setNotificationToday(context: Context, value: Boolean) =
        context.getSharedPreferences(NAME, 0).edit().putBoolean(NOTIFICATION_TODAY, value).apply()
}