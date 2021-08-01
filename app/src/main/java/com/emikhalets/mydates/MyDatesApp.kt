package com.emikhalets.mydates

import android.app.Application
import com.emikhalets.mydates.foreground.EventsReceiver
import com.emikhalets.mydates.foreground.UpdateEventsReceiver
import com.emikhalets.mydates.utils.*
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltAndroidApp
class MyDatesApp : Application() {

    override fun onCreate() {
        super.onCreate()

        CoroutineScope(Dispatchers.IO).launch {
            initSharedPreferences()
            startAlarmManager()
        }
    }

    private fun initSharedPreferences() {
        if (Preferences.getAppFirstLaunch(this)) {
            Preferences.setAppFirstLaunch(this, false)
            Preferences.setNotificationHour(this, DEFAULT_NOTIFICATION_HOUR)
            Preferences.setNotificationMinute(this, DEFAULT_NOTIFICATION_MINUTE)
        }
    }

    private fun startAlarmManager() {
        setRepeatingAlarm(
            context = this@MyDatesApp,
            hour = EVENTS_UPDATE_HOUR,
            minute = EVENTS_UPDATE_MINUTE,
            receiver = UpdateEventsReceiver::class.java,
            requestCode = APP_UPDATE_ALARM_REQUEST_CODE
        )

        setRepeatingAlarm(
            context = this@MyDatesApp,
            hour = 11,
            minute = 0,
            receiver = EventsReceiver::class.java,
            requestCode = APP_EVENTS_ALARM_REQUEST_CODE
        )
    }

    companion object {
        const val DEFAULT_NOTIFICATION_HOUR = 11
        const val DEFAULT_NOTIFICATION_MINUTE = 0
    }
}