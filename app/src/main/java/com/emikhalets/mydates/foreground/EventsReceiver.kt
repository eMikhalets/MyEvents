package com.emikhalets.mydates.foreground

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.workDataOf
import com.emikhalets.mydates.utils.*

class EventsReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val sp = context.getSharedPreferences("my_dates_sp", Context.MODE_PRIVATE)
        val isMonth = sp.getBoolean("is_month_notifications", false)
        val isWeek = sp.getBoolean("is_week_notifications", false)
        val isTwoDays = sp.getBoolean("is_two_day_notifications", false)
        val isDays = sp.getBoolean("is_day_notifications", false)
        val isToday = sp.getBoolean("is_today_notifications", false)

        val data = workDataOf(
            DATA_NOTIF_MONTH to isMonth,
            DATA_NOTIF_WEEK to isWeek,
            DATA_NOTIF_TWO_DAY to isTwoDays,
            DATA_NOTIF_DAY to isDays,
            DATA_NOTIF_TODAY to isToday
        )
        OneTimeWorkRequestBuilder<EventWorker>().setInputData(data).build()
    }
}