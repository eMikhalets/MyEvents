package com.emikhalets.mydates

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.emikhalets.mydates.utils.di.AppComponent
import com.emikhalets.mydates.utils.di.DaggerAppComponent
import com.emikhalets.mydates.utils.enums.DEFAULT_NOTIFICATION_HOUR
import com.emikhalets.mydates.utils.enums.DEFAULT_NOTIFICATION_MINUTE
import com.emikhalets.mydates.utils.enums.Theme
import com.emikhalets.mydates.utils.setEventAlarm
import com.emikhalets.mydates.utils.setUpdatingAlarm

class MyDatesApp : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        buildDaggerDependencies()
        checkTheme()
        initNotifications()
    }

    private fun buildDaggerDependencies() {
        appComponent = DaggerAppComponent.factory().create(applicationContext)
    }

    private fun initNotifications() {
        val isAppFirstLaunch = appComponent.appPreferences.getAppFirstLaunch()
        if (isAppFirstLaunch) {
            appComponent.appPreferences.setAppFirstLaunch(false)
            appComponent.appPreferences.setNotificationHour(DEFAULT_NOTIFICATION_HOUR)
            appComponent.appPreferences.setNotificationMinute(DEFAULT_NOTIFICATION_MINUTE)
            setUpdatingAlarm()
            setEventAlarm()
        }
    }

    private fun checkTheme() {
        when (Theme.get(appComponent.appPreferences.getTheme())) {
            Theme.LIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            Theme.DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }
}