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
        val sp = context.getSharedPreferences(APP_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val isAll = sp.getBoolean(APP_SP_NOTIF_ALL_FLAG, false)

        if (isAll) {
            val isMonth = sp.getBoolean(APP_SP_NOTIF_MONTH_FLAG, false)
            val isWeek = sp.getBoolean(APP_SP_NOTIF_WEEK_FLAG, false)
            val isTwoDays = sp.getBoolean(APP_SP_NOTIF_TWO_DAY_FLAG, false)
            val isDays = sp.getBoolean(APP_SP_NOTIF_DAY_FLAG, false)
            val isToday = sp.getBoolean(APP_SP_NOTIF_TODAY_FLAG, false)

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