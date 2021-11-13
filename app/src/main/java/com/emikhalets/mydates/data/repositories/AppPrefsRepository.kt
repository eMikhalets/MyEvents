package com.emikhalets.mydates.data.repositories

interface AppPrefsRepository {

    fun getAppFirstLaunch(): Boolean
    fun setAppFirstLaunch(value: Boolean)

    fun getEventsLastUpdateTime(): Long
    fun setEventsLastUpdateTime(value: Long)

    fun getNotificationHour(): Int
    fun setNotificationHour(value: Int)

    fun getNotificationMinute(): Int
    fun setNotificationMinute(value: Int)

    fun getNotificationAll(): Boolean
    fun setNotificationAll(value: Boolean)

    fun getNotificationMonth(): Boolean
    fun setNotificationMonth(value: Boolean)

    fun getNotificationWeek(): Boolean
    fun setNotificationWeek(value: Boolean)

    fun getNotificationThreeDay(): Boolean
    fun setNotificationThreeDay(value: Boolean)

    fun getNotificationTwoDay(): Boolean
    fun setNotificationTwoDay(value: Boolean)

    fun getNotificationDay(): Boolean
    fun setNotificationDay(value: Boolean)

    fun getNotificationToday(): Boolean
    fun setNotificationToday(value: Boolean)

    fun getTheme(): Int
    fun setTheme(value: Int)

    fun getLanguage(): String
    fun setLanguage(value: String)
}