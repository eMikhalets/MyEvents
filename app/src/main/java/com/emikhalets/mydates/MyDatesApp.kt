package com.emikhalets.mydates

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.emikhalets.mydates.utils.Preferences
import com.emikhalets.mydates.utils.enums.Theme
import com.emikhalets.mydates.utils.setEventAlarm
import com.emikhalets.mydates.utils.setUpdatingAlarm
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltAndroidApp
class MyDatesApp : Application() {

    override fun onCreate() {
        super.onCreate()
        checkTheme()

        CoroutineScope(Dispatchers.IO).launch {
            initNotifications()
        }
    }

    private fun initNotifications() {
        if (Preferences.getAppFirstLaunch(this)) {
            Preferences.setAppFirstLaunch(this, false)
            Preferences.setNotificationHour(this, DEFAULT_NOTIFICATION_HOUR)
            Preferences.setNotificationMinute(this, DEFAULT_NOTIFICATION_MINUTE)
            setUpdatingAlarm()
            setEventAlarm()
        }
    }

    private fun checkTheme() {
        when (Theme.get(Preferences.getTheme(this))) {
            Theme.LIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            Theme.DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    companion object {
        const val DEFAULT_NOTIFICATION_HOUR = 11
        const val DEFAULT_NOTIFICATION_MINUTE = 0
    }
}