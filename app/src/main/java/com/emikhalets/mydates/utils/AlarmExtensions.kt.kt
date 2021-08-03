package com.emikhalets.mydates.utils

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.emikhalets.mydates.foreground.EventsReceiver
import java.util.*

fun Context.restartAlarmManagers() {
    val hour = Preferences.getNotificationHour(this)
    val minute = Preferences.getNotificationMinute(this)

    setRepeatingAlarm(
        context = this,
        hour = EVENTS_UPDATE_HOUR,
        minute = EVENTS_UPDATE_MINUTE,
        receiver = EventsReceiver::class.java,
        requestCode = APP_UPDATE_ALARM_REQUEST_CODE
    )

    setRepeatingAlarm(
        context = this,
        hour = hour,
        minute = minute,
        receiver = EventsReceiver::class.java,
        requestCode = APP_EVENTS_ALARM_REQUEST_CODE
    )
}

fun setRepeatingAlarm(
    context: Context,
    hour: Int,
    minute: Int,
    receiver: Class<*>,
    requestCode: Int
) {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.HOUR_OF_DAY, hour)
    calendar.set(Calendar.MINUTE, minute)
    calendar.set(Calendar.SECOND, 0)
    if (calendar.time < Date()) calendar.add(Calendar.DAY_OF_MONTH, 1)

    val alarmManager = context.getSystemService(Application.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, receiver)
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        requestCode,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    alarmManager.setRepeating(
        AlarmManager.RTC_WAKEUP,
        calendar.timeInMillis,
        AlarmManager.INTERVAL_DAY,
        pendingIntent
    )
}