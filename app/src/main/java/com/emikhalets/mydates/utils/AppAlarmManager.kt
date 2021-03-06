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
import com.emikhalets.mydates.utils.extentions.launchMainScope
import java.util.*

object AppAlarmManager {

    private const val EVENTS_ALARM_REQUEST_CODE = 7
    private const val UPDATING_ALARM_REQUEST_CODE = 8

    fun scheduleEventAlarm(context: Context) {
        launchMainScope {
            val hour = context.appComponent.appPreferences.getNotificationHour()
            val minute = context.appComponent.appPreferences.getNotificationMinute()
            setRepeatingAlarm(
                context = context,
                hour = hour,
                minute = minute,
                receiver = EventsReceiver::class.java,
                requestCode = EVENTS_ALARM_REQUEST_CODE
            )
        }
    }

    fun scheduleUpdatingAlarm(context: Context) {
        setRepeatingAlarm(
            context = context,
            hour = 0,
            minute = 5,
            receiver = UpdateEventsReceiver::class.java,
            requestCode = UPDATING_ALARM_REQUEST_CODE
        )
    }

    fun scheduleAllAlarms(context: Context) {
        scheduleEventAlarm(context)
        scheduleUpdatingAlarm(context)
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun setRepeatingAlarm(
        context: Context,
        hour: Int,
        minute: Int,
        receiver: Class<*>,
        requestCode: Int,
    ) {
        val alarmManager = context.getSystemService(Application.ALARM_SERVICE) as AlarmManager
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            Intent(context, receiver),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.setExact(
            AlarmManager.RTC,
            getScheduleTimestamp(hour, minute),
            pendingIntent
        )
    }

    private fun getScheduleTimestamp(hour: Int, minute: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        if (calendar.time < Date()) calendar.add(Calendar.DAY_OF_MONTH, 1)
        return calendar.timeInMillis
    }
}