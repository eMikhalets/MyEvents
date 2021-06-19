package com.emikhalets.mydates.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Build
import androidx.core.app.NotificationCompat
import com.emikhalets.mydates.MainActivity
import com.emikhalets.mydates.R
import com.emikhalets.mydates.data.database.entities.Event

const val ID = "my_dates_notification_id"
const val NAME_MONTH = "my_dates_notification_month"
const val CHANNEL_MONTH = "my_dates_channel_month"
const val NOTIFICATION_WORK = "my_dates_notification_work"

fun sendMonthNotification(context: Context, id: Int, events: List<Event>) {
    val intent = Intent(context, MainActivity::class.java)
    val pendingIntent = getActivity(context, 0, intent, 0)
    intent.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
    intent.putExtra(ID, id)

    val notificationManager =
        context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

    val style = NotificationCompat.InboxStyle()
    events.forEach {
        if (it.eventType != 0) style.addLine("${it.fullName()} ${it.age}")
    }

    val notification = NotificationCompat.Builder(context, CHANNEL_MONTH)
        .setSmallIcon(R.drawable.ic_calendar)
        .setContentTitle(context.getString(R.string.notification_title_month))
        .setContentText(context.getString(R.string.notification_text_month))
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setContentIntent(pendingIntent)
        .setStyle(style)
        .setAutoCancel(true)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        notification.setChannelId(CHANNEL_MONTH)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_MONTH, NAME_MONTH, importance)
        notificationManager.createNotificationChannel(channel)
    }

    notificationManager.notify(id, notification.build())
}