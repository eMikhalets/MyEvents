package com.emikhalets.mydates.data.repositories

interface AppPrefsRepository {

    suspend fun getAppFirstLaunch(): Boolean
    suspend fun setAppFirstLaunch(value: Boolean)

    suspend fun getEventsLastUpdateTime(): Long
    suspend fun setEventsLastUpdateTime(value: Long)

    suspend fun getNotificationHour(): Int
    suspend fun setNotificationHour(value: Int)

    suspend fun getNotificationMinute(): Int
    suspend fun setNotificationMinute(value: Int)

    suspend fun getNotificationAll(): Boolean
    suspend fun setNotificationAll(value: Boolean)

    suspend fun getNotificationMonth(): Boolean
    suspend fun setNotificationMonth(value: Boolean)

    suspend fun getNotificationWeek(): Boolean
    suspend fun setNotificationWeek(value: Boolean)

    suspend fun getNotificationThreeDay(): Boolean
    suspend fun setNotificationThreeDay(value: Boolean)

    suspend fun getNotificationTwoDay(): Boolean
    suspend fun setNotificationTwoDay(value: Boolean)

    suspend fun getNotificationDay(): Boolean
    suspend fun setNotificationDay(value: Boolean)

    suspend fun getNotificationToday(): Boolean
    suspend fun setNotificationToday(value: Boolean)

    suspend fun getTheme(): Int
    suspend fun setTheme(value: Int)

    suspend fun getLanguage(): String
    suspend fun setLanguage(value: String)
}