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

private const val PENDING_ID = "my_dates_pending_id"

private const val ID_EVENTS = "my_dates_channel_id_events"
private const val ID_UPDATE = "my_dates_channel_id_update"

private const val NAME_EVENTS = "my_dates_channel_name_events"
private const val NAME_UPDATE = "my_dates_channel_name_update"

fun sendMonthNotification(context: Context, id: Int, events: List<Event>) {
    val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

    val style = NotificationCompat.InboxStyle()
    events.forEach {
        if (it.eventType != 0) style.addLine("${it.fullName()} ${it.age}")
    }

    val notification = NotificationCompat.Builder(context, ID_EVENTS)
        .setSmallIcon(R.drawable.ic_calendar)
        .setContentTitle(context.getString(R.string.notification_title_month))
        .setContentText(context.getString(R.string.notification_text_month))
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setStyle(style)
        .setAutoCancel(true)

    notification.setPendingIntent(context, id)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        notificationManager.setNotificationChannel(notification, ID_EVENTS, NAME_EVENTS)

    notificationManager.notify(id, notification.build())
}

fun sendErrorUpdateNotification(context: Context) {
    val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

    val notification = NotificationCompat.Builder(context, ID_UPDATE)
        .setSmallIcon(R.drawable.ic_calendar)
        .setContentTitle(context.getString(R.string.notification_title_month))
        .setContentText(context.getString(R.string.notification_text_month))
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)

    notification.setPendingIntent(context, 0)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        notificationManager.setNotificationChannel(notification, ID_UPDATE, NAME_UPDATE)

    notificationManager.notify(0, notification.build())
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