package com.emikhalets.mydates.foreground

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.emikhalets.mydates.utils.*

class EventsReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        context.setEventAlarm()
        if (Preferences.getNotificationAll(context)) {
            val isMonth = Preferences.getNotificationMonth(context)
            val isWeek = Preferences.getNotificationWeek(context)
            val isTwoDays = Preferences.getNotificationTwoDay(context)
            val isDays = Preferences.getNotificationDay(context)
            val isToday = Preferences.getNotificationToday(context)

            val data = workDataOf(
                DATA_NOTIF_MONTH to isMonth,
                DATA_NOTIF_WEEK to isWeek,
                DATA_NOTIF_TWO_DAY to isTwoDays,
                DATA_NOTIF_DAY to isDays,
                DATA_NOTIF_TODAY to isToday
            )
            val work = OneTimeWorkRequestBuilder<EventWorker>().setInputData(data).build()
            WorkManager.getInstance(context).enqueue(work)
        }
    }
}