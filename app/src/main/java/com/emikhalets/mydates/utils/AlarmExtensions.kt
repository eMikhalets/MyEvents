package com.emikhalets.mydates.utils

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.emikhalets.mydates.foreground.EventsReceiver
import com.emikhalets.mydates.foreground.UpdateEventsReceiver
import com.emikhalets.mydates.utils.di.appComponent
import java.util.*

fun Context.setEventAlarm() {
    launchMainScope {
        val hour = appComponent.appPreferences.getNotificationHour()
        val minute = appComponent.appPreferences.getNotificationMinute()
        setRepeatingAlarm(
            context = this@setEventAlarm,
            hour = hour,
            minute = minute,
            receiver = EventsReceiver::class.java,
            requestCode = 7
        )
    }
}

fun Context.setUpdatingAlarm() {
    setRepeatingAlarm(
        context = this,
        hour = 0,
        minute = 5,
        receiver = UpdateEventsReceiver::class.java,
        requestCode = 8
    )
}

@SuppressLint("UnspecifiedImmutableFlag")
private fun setRepeatingAlarm(
    context: Context,
    hour: Int,
    minute: Int,
    receiver: Class<*>,
    requestCode: Int,
) {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.HOUR_OF_DAY, hour)
    calendar.set(Calendar.MINUTE, minute)
    calendar.set(Calendar.SECOND, 0)
    if (calendar.time < Date()) calendar.add(Calendar.DAY_OF_MONTH, 1)

    val alarmManager = context.getSystemService(Application.ALARM_SERVICE) as AlarmManager
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        requestCode,
        Intent(context, receiver),
        PendingIntent.FLAG_CANCEL_CURRENT
    )

    alarmManager.setExact(
        AlarmManager.RTC,
        calendar.timeInMillis,
        pendingIntent
    )
}