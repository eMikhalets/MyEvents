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
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.emikhalets.mydates.MainActivity
import com.emikhalets.mydates.R
import com.emikhalets.mydates.data.database.entities.Event

const val DATA_NOTIF_MONTH = "worker_notif_month"
const val DATA_NOTIF_WEEK = "worker_notif_week"
const val DATA_NOTIF_TWO_DAY = "worker_notif_two_day"
const val DATA_NOTIF_DAY = "worker_notif_day"
const val DATA_NOTIF_TODAY = "worker_notif_today"

private const val PENDING_ID = "my_dates_pending_id"

private const val ID_EVENTS = "my_dates_channel_id_events"
private const val ID_UPDATE = "my_dates_channel_id_update"

private const val NAME_EVENTS = "my_dates_channel_name_events"
private const val NAME_UPDATE = "my_dates_channel_name_update"

fun sendErrorUpdateNotification(context: Context) {
    val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

    val notification = NotificationCompat.Builder(context, ID_UPDATE)
        .setSmallIcon(R.drawable.ic_calendar)
        .setContentTitle("Error updating events")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)

    notification.setPendingIntent(context, 0)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        notificationManager.setNotificationChannel(notification, ID_UPDATE, NAME_UPDATE)

    notificationManager.notify(0, notification.build())
}

fun sendEventsNotification(context: Context, events: HashMap<String, List<Event>>) {
    val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

    val notification = NotificationCompat.Builder(context, ID_EVENTS)
        .setSmallIcon(R.drawable.ic_calendar)
        .setContentTitle(context.getString(R.string.notification_title))
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)

    notification.setPendingIntent(context, 1)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        notificationManager.setNotificationChannel(notification, ID_EVENTS, NAME_EVENTS)

    val style = NotificationCompat.InboxStyle()
    events[DATA_NOTIF_TODAY]?.let { list ->
        if (list.isNotEmpty()) {
            style.addLine(context.getString(R.string.notification_text_today))
            list.forEach { style.addLine("${it.fullName()} ${it.age}") }
        }
    }
    events[DATA_NOTIF_DAY]?.let { list ->
        if (list.isNotEmpty()) {
            style.addLine(context.getString(R.string.notification_text_day))
            list.forEach { style.addLine("${it.fullName()} ${it.age}") }
        }
    }
    events[DATA_NOTIF_TWO_DAY]?.let { list ->
        if (list.isNotEmpty()) {
            style.addLine(context.getString(R.string.notification_text_two_day))
            list.forEach { style.addLine("${it.fullName()} ${it.age}") }
        }
    }
    events[DATA_NOTIF_WEEK]?.let { list ->
        if (list.isNotEmpty()) {
            style.addLine(context.getString(R.string.notification_text_week))
            list.forEach { style.addLine("${it.fullName()} ${it.age}") }
        }
    }
    events[DATA_NOTIF_MONTH]?.let { list ->
        if (list.isNotEmpty()) {
            style.addLine(context.getString(R.string.notification_text_month))
            list.forEach { style.addLine("${it.fullName()} ${it.age}") }
        }
    }

    notificationManager.notify(1, notification.build())
}

fun sendNotification(context: Context) {
    val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

    val notification = NotificationCompat.Builder(context, ID_UPDATE)
        .setSmallIcon(R.drawable.ic_calendar)
        .setContentTitle("Уведомление Обновление")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)

    notification.setPendingIntent(context, 3)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        notificationManager.setNotificationChannel(notification, ID_UPDATE, NAME_UPDATE)

    notificationManager.notify(3, notification.build())
}

@RequiresApi(Build.VERSION_CODES.O)
fun NotificationManager.setNotificationChannel(
    notification: NotificationCompat.Builder,
    id: String,
    name: String
) {
    notification.setChannelId(id)
    val importance = NotificationManager.IMPORTANCE_DEFAULT
    val channel = NotificationChannel(id, name, importance)
    createNotificationChannel(channel)
}

fun NotificationCompat.Builder.setPendingIntent(context: Context, id: Int) {
    val intent = Intent(context, MainActivity::class.java)
    val pendingIntent = getActivity(context, 0, intent, 0)
    intent.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
    intent.putExtra(PENDING_ID, id)
    setContentIntent(pendingIntent)
}