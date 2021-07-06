package com.emikhalets.mydates

import android.app.Application
import com.emikhalets.mydates.foreground.EventsReceiver
import com.emikhalets.mydates.foreground.UpdateEventsReceiver
import com.emikhalets.mydates.utils.*
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyDatesApp : Application() {

    override fun onCreate() {
        super.onCreate()

        if (!isAlarmLaunched(APP_SP_FIRST_LAUNCH)) {
            initSharedPreferencesNotification()
        }

        if (!isAlarmLaunched(APP_SP_ALARM_UPDATE_FLAG)) {
            setRepeatingAlarm(
                this,
                EVENTS_UPDATE_HOUR,
                EVENTS_UPDATE_MINUTE,
                UpdateEventsReceiver::class.java,
                APP_UPDATE_ALARM_REQUEST_CODE
            )
            saveAlarmLaunchState(APP_SP_ALARM_UPDATE_FLAG)
        }

        if (!isAlarmLaunched(APP_SP_ALARM_EVENT_FLAG)) {
            setRepeatingAlarm(
                this,
                11,
                0,
                EventsReceiver::class.java,
                APP_EVENTS_ALARM_REQUEST_CODE
            )
            saveAlarmLaunchState(APP_SP_ALARM_EVENT_FLAG)
        }

    }

    private fun initSharedPreferencesNotification() {
        val sp = getSharedPreferences(APP_SHARED_PREFERENCES, MODE_PRIVATE).edit()

        sp.putInt(APP_SP_EVENT_HOUR, 11)
        sp.putInt(APP_SP_EVENT_MINUTE, 0)
        sp.putBoolean(APP_SP_NOTIF_ALL_FLAG, true)
        sp.putBoolean(APP_SP_NOTIF_MONTH_FLAG, true)
        sp.putBoolean(APP_SP_NOTIF_WEEK_FLAG, true)
        sp.putBoolean(APP_SP_NOTIF_TWO_DAY_FLAG, true)
        sp.putBoolean(APP_SP_NOTIF_DAY_FLAG, true)
        sp.putBoolean(APP_SP_NOTIF_TODAY_FLAG, true)

        sp.apply()
    }

    private fun isAlarmLaunched(key: String): Boolean {
        return getSharedPreferences(APP_SHARED_PREFERENCES, MODE_PRIVATE)
            .getBoolean(key, false)
    }

    private fun saveAlarmLaunchState(key: String) {
        getSharedPreferences(APP_SHARED_PREFERENCES, MODE_PRIVATE).edit()
            .putBoolean(key, true).apply()
    }
}