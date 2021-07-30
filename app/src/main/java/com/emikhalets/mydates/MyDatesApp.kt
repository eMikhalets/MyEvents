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
    }

    private fun initSharedPreferences() {
        if (spGetAppFirstLaunch()) {
            spSetAppFirstLaunch(false)
            spSetNotificationAll(true)
            spSetNotificationMonth(true)
            spSetNotificationWeek(true)
            spSetNotificationThreeDay(true)
            spSetNotificationTwoDay(true)
            spSetNotificationDay(true)
            spSetNotificationToday(true)
            spSetNotificationHour(DEFAULT_NOTIFICATION_HOUR)
            spSetNotificationMinute(DEFAULT_NOTIFICATION_MINUTE)
        }
    }

    companion object {
        const val DEFAULT_NOTIFICATION_HOUR = 11
        const val DEFAULT_NOTIFICATION_MINUTE = 0
    }
}