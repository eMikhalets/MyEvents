package com.emikhalets.mydates.foreground

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.emikhalets.mydates.utils.*

class RebootReceiver : BroadcastReceiver() {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent) {
        val sp = context.getSharedPreferences(APP_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val notificationsHour = sp.getInt(APP_SP_EVENT_HOUR, 11)
        val notificationsMinute = sp.getInt(APP_SP_EVENT_MINUTE, 0)

        setRepeatingAlarm(
            context,
            0,
            10,
            UpdateEventsReceiver::class.java,
            APP_UPDATE_ALARM_REQUEST_CODE
        )

        setRepeatingAlarm(
            context,
            notificationsHour,
            notificationsMinute,
            UpdateEventsReceiver::class.java,
            APP_EVENTS_ALARM_REQUEST_CODE
        )
    }
}