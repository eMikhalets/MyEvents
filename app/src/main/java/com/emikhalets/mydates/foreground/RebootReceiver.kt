package com.emikhalets.mydates.foreground

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.emikhalets.mydates.utils.restartAlarmManagers


class RebootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            context.restartAlarmManagers()
        }
    }
}