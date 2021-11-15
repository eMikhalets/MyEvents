package com.emikhalets.mydates.foreground

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.emikhalets.mydates.utils.AppAlarmManager
import com.emikhalets.mydates.utils.DATA_NOTIF_DAY
import com.emikhalets.mydates.utils.DATA_NOTIF_MONTH
import com.emikhalets.mydates.utils.DATA_NOTIF_TODAY
import com.emikhalets.mydates.utils.DATA_NOTIF_TWO_DAY
import com.emikhalets.mydates.utils.DATA_NOTIF_WEEK
import com.emikhalets.mydates.utils.di.appComponent
import com.emikhalets.mydates.utils.extentions.launchMainScope

class EventsReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        AppAlarmManager.scheduleEventAlarm(context)
        launchMainScope {
            if (context.appComponent.appPreferences.getNotificationAll()) {
                val isMonth = context.appComponent.appPreferences.getNotificationMonth()
                val isWeek = context.appComponent.appPreferences.getNotificationWeek()
                val isTwoDays = context.appComponent.appPreferences.getNotificationTwoDay()
                val isDays = context.appComponent.appPreferences.getNotificationDay()
                val isToday = context.appComponent.appPreferences.getNotificationToday()

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
}