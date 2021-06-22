package com.emikhalets.mydates.foreground

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.emikhalets.mydates.utils.sendNotification

class UpdateEventsReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // TODO: set foreground service
        val worker = OneTimeWorkRequestBuilder<UpdateEventsWorker>().build()
        WorkManager.getInstance(context).enqueue(worker)
    }
}