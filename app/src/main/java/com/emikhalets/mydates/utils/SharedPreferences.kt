package com.emikhalets.mydates.utils

import android.content.Context

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

fun Context.spGetAppFirstLaunch() =
    getSharedPreferences(NAME, 0).getBoolean(APP_FIRST_LAUNCH, true)

fun Context.spSetAppFirstLaunch(value: Boolean) =
    getSharedPreferences(NAME, 0).edit().putBoolean(APP_FIRST_LAUNCH, value).apply()

fun Context.spGetEventsLastUpdateTime() =
    getSharedPreferences(NAME, 0).getLong(EVENTS_UPDATE_TIME, 0)

fun Context.spSetEventsLastUpdateTime(value: Long) =
    getSharedPreferences(NAME, 0).edit().putLong(EVENTS_UPDATE_TIME, value).apply()

fun Context.spGetNotificationHour() =
    getSharedPreferences(NAME, 0).getInt(NOTIFICATION_HOUR, 0)

fun Context.spSetNotificationHour(value: Int) =
    getSharedPreferences(NAME, 0).edit().putInt(NOTIFICATION_HOUR, value).apply()

fun Context.spGetNotificationMinute() =
    getSharedPreferences(NAME, 0).getInt(NOTIFICATION_MINUTE, 0)

fun Context.spSetNotificationMinute(value: Int) =
    getSharedPreferences(NAME, 0).edit().putInt(NOTIFICATION_MINUTE, value).apply()

fun Context.spGetNotificationAll() =
    getSharedPreferences(NAME, 0).getBoolean(NOTIFICATION_ALL, true)

fun Context.spSetNotificationAll(value: Boolean) =
    getSharedPreferences(NAME, 0).edit().putBoolean(NOTIFICATION_ALL, value).apply()

fun Context.spGetNotificationMonth() =
    getSharedPreferences(NAME, 0).getBoolean(NOTIFICATION_MONTH, true)

fun Context.spSetNotificationMonth(value: Boolean) =
    getSharedPreferences(NAME, 0).edit().putBoolean(NOTIFICATION_MONTH, value).apply()

fun Context.spGetNotificationWeek() =
    getSharedPreferences(NAME, 0).getBoolean(NOTIFICATION_WEEK, true)

fun Context.spSetNotificationWeek(value: Boolean) =
    getSharedPreferences(NAME, 0).edit().putBoolean(NOTIFICATION_WEEK, value).apply()

fun Context.spGetNotificationThreeDay() =
    getSharedPreferences(NAME, 0).getBoolean(NOTIFICATION_THREE_DAY, true)

fun Context.spSetNotificationThreeDay(value: Boolean) =
    getSharedPreferences(NAME, 0).edit().putBoolean(NOTIFICATION_THREE_DAY, value).apply()

fun Context.spGetNotificationTwoDay() =
    getSharedPreferences(NAME, 0).getBoolean(NOTIFICATION_TWO_DAY, true)

fun Context.spSetNotificationTwoDay(value: Boolean) =
    getSharedPreferences(NAME, 0).edit().putBoolean(NOTIFICATION_TWO_DAY, value).apply()

fun Context.spGetNotificationDay() =
    getSharedPreferences(NAME, 0).getBoolean(NOTIFICATION_DAY, true)

fun Context.spSetNotificationDay(value: Boolean) =
    getSharedPreferences(NAME, 0).edit().putBoolean(NOTIFICATION_DAY, value).apply()

fun Context.spGetNotificationToday() =
    getSharedPreferences(NAME, 0).getBoolean(NOTIFICATION_TODAY, true)

fun Context.spSetNotificationToday(value: Boolean) =
    getSharedPreferences(NAME, 0).edit().putBoolean(NOTIFICATION_TODAY, value).apply()