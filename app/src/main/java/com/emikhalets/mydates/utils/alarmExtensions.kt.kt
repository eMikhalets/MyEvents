package com.emikhalets.mydates.utils

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import androidx.fragment.app.Fragment
import com.emikhalets.mydates.foreground.EventsReceiver
import java.util.*

fun Application.setAlarm(hour: Int, minute: Int, receiver: Class<*>, requestCode: Int) {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.HOUR_OF_DAY, hour)
    calendar.set(Calendar.MINUTE, minute)
    calendar.set(Calendar.SECOND, 0)
    if (calendar.time < Date()) calendar.add(Calendar.DAY_OF_MONTH, 1)

    val alarmManager = getSystemService(Application.ALARM_SERVICE) as AlarmManager
    val intent = Intent(this, receiver)
    val pendingIntent = PendingIntent.getBroadcast(
        this,
        APP_EVENTS_ALARM_REQUEST_CODE,
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

fun Fragment.resetEventAlarm(hour: Int, minute: Int) {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.HOUR_OF_DAY, hour)
    calendar.set(Calendar.MINUTE, minute)
    calendar.set(Calendar.SECOND, 0)
    if (calendar.time < Date()) calendar.add(Calendar.DAY_OF_MONTH, 1)

    val intent = Intent(context, EventsReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        APP_EVENTS_ALARM_REQUEST_CODE,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val alarmManager = requireContext().getSystemService(Application.ALARM_SERVICE) as AlarmManager
    alarmManager.setRepeating(
        AlarmManager.RTC_WAKEUP,
        calendar.timeInMillis,
        AlarmManager.INTERVAL_DAY,
        pendingIntent
    )
}