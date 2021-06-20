package com.emikhalets.mydates.foreground

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.OneTimeWorkRequestBuilder

class UpdateEventsReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        OneTimeWorkRequestBuilder<UpdateEventsWorker>().build()
    }
}