package com.emikhalets.mydates.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat

object AppPermissionManager {

    fun isContactsGranted(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) ==
                PackageManager.PERMISSION_GRANTED
    }

    fun requestContactsPermission(launcher: ActivityResultLauncher<String>) {
        launcher.launch(Manifest.permission.READ_CONTACTS)
    }
}