package com.emikhalets.mydates

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.emikhalets.mydates.foreground.UpdateEventsReceiver
import dagger.hilt.android.HiltAndroidApp
import java.util.*

@HiltAndroidApp
class MyDatesApp : Application() {

    override fun onCreate() {
        super.onCreate()

        val initUpdating = getSharedPreferences("my_dates_sp", MODE_PRIVATE)
            .getBoolean("is_init_updating", false)
        if (!initUpdating) {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, 1)
            if (calendar.time < Date()) calendar.add(Calendar.DAY_OF_MONTH, 1)

            val intent = Intent(applicationContext, UpdateEventsReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                applicationContext,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
            getSharedPreferences("my_dates_sp", MODE_PRIVATE).edit()
                .putBoolean("is_init_updating", true).apply()
        }
    }
}