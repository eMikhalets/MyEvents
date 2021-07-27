package com.emikhalets.mydates.foreground

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.emikhalets.mydates.utils.*


class RebootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val sp = context.getSharedPreferences(APP_SHARED_PREFERENCES, Context.MODE_PRIVATE)
            val notificationsHour = sp.getInt(APP_SP_EVENT_HOUR, 11)
            val notificationsMinute = sp.getInt(APP_SP_EVENT_MINUTE, 0)

            setRepeatingAlarm(
                context,
                EVENTS_UPDATE_HOUR,
                EVENTS_UPDATE_MINUTE,
                EventsReceiver::class.java,
                APP_UPDATE_ALARM_REQUEST_CODE
            )

            setRepeatingAlarm(
                context,
                notificationsHour,
                notificationsMinute,
                EventsReceiver::class.java,
                APP_EVENTS_ALARM_REQUEST_CODE
            )
        }
    }
}