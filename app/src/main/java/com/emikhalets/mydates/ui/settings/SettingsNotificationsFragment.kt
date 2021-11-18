package com.emikhalets.mydates.ui.settings

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.view.isGone
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.emikhalets.mydates.R
import com.emikhalets.mydates.databinding.FragmentSettingsNotificationsBinding
import com.emikhalets.mydates.ui.base.BaseFragment
import com.emikhalets.mydates.utils.AppAlarmManager
import com.emikhalets.mydates.utils.AppDialogManager
import com.emikhalets.mydates.utils.di.appComponent
import com.emikhalets.mydates.utils.extentions.launchMainScope
import com.emikhalets.mydates.utils.extentions.toast
import com.google.android.material.switchmaterial.SwitchMaterial
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay

class SettingsNotificationsFragment : BaseFragment(R.layout.fragment_settings_notifications) {

    private val binding by viewBinding(FragmentSettingsNotificationsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareSettings()
        clickListeners()
    }

    private fun prepareSettings() {
        launchMainScope {
            binding.apply {
                val hour = appComponent.appPreferences.getNotificationHour()
                val minute = appComponent.appPreferences.getNotificationMinute()
                binding.textTime.text = formatTime(hour, minute)

                switchAllNotifications.isChecked =
                    appComponent.appPreferences.getNotificationAll()
                switchMonthNotifications.isChecked =
                    appComponent.appPreferences.getNotificationMonth()
                switchWeekNotifications.isChecked =
                    appComponent.appPreferences.getNotificationWeek()
                switchTwoDayNotifications.isChecked =
                    appComponent.appPreferences.getNotificationTwoDay()
                switchDayNotifications.isChecked =
                    appComponent.appPreferences.getNotificationDay()
                switchTodayNotifications.isChecked =
                    appComponent.appPreferences.getNotificationToday()

                switchAllNotifications.isChecked.apply {
                    layTime.isGone = this
                    switchMonthNotifications.isEnabled = this
                    switchWeekNotifications.isEnabled = this
                    switchTwoDayNotifications.isEnabled = this
                    switchDayNotifications.isEnabled = this
                    switchTodayNotifications.isEnabled = this
                }

                setAlarmStatesTexts()
            }
        }
    }

    private fun clickListeners() {
        binding.apply {
            layTime.setOnClickListener {
                AppDialogManager.showTimePickerDialog(requireContext()) { hour, minute ->
                    binding.textTime.text = formatTime(hour, minute)
                    saveNotificationsTime(hour, minute)
                }
            }

            switchAllNotifications.setOnCheckedChangeListener { isChecked ->
                appComponent.appPreferences.setNotificationAll(isChecked)
                setNotificationsEnabled(isChecked)
            }

            switchMonthNotifications.setOnCheckedChangeListener { isChecked ->
                appComponent.appPreferences.setNotificationMonth(isChecked)
            }

            switchWeekNotifications.setOnCheckedChangeListener { isChecked ->
                appComponent.appPreferences.setNotificationWeek(isChecked)
            }

            switchTwoDayNotifications.setOnCheckedChangeListener { isChecked ->
                appComponent.appPreferences.setNotificationTwoDay(isChecked)
            }

            switchDayNotifications.setOnCheckedChangeListener { isChecked ->
                appComponent.appPreferences.setNotificationDay(isChecked)
            }

            switchTodayNotifications.setOnCheckedChangeListener { isChecked ->
                appComponent.appPreferences.setNotificationToday(isChecked)
            }

            textRestartNotifications.setOnClickListener {
                restartAlarmManagers()
            }
        }
    }

    private fun restartAlarmManagers() {
        AppAlarmManager.scheduleAllAlarms(requireContext())
        toast(R.string.alarm_managers_restarted)
        setAlarmStatesTexts()
    }

    private fun saveNotificationsTime(hour: Int, minute: Int) {
        launchMainScope {
            appComponent.appPreferences.setNotificationHour(hour)
            appComponent.appPreferences.setNotificationMinute(minute)
            AppAlarmManager.scheduleEventAlarm(requireContext())
        }
    }

    private fun formatTime(hour: Int, minute: Int): String {
        return if (minute > 9) "$hour:$minute"
        else "$hour:0$minute"
    }

    private fun setNotificationsEnabled(bool: Boolean) {
        binding.apply {
            layTime.isGone = !bool
            switchMonthNotifications.isEnabled = bool
            switchWeekNotifications.isEnabled = bool
            switchTwoDayNotifications.isEnabled = bool
            switchDayNotifications.isEnabled = bool
            switchTodayNotifications.isEnabled = bool
        }
    }

    private fun SwitchMaterial.setOnCheckedChangeListener(
        block: suspend CoroutineScope.(isChecked: Boolean) -> Unit
    ) {
        setOnCheckedChangeListener { _, isChecked ->
            launchMainScope {
                block(isChecked)
            }
        }
    }

    private fun TextView.setAlarmState(running: Boolean) {
        text = if (running) {
            getString(R.string.settings_on)
        } else {
            getString(R.string.settings_off)
        }
    }

    private fun setAlarmStatesTexts() {
        lifecycleScope.launchWhenResumed {
            delay(1000)
            binding.textAlarmUpdates
                .setAlarmState(AppAlarmManager.isUpdatingAlarmRunning(requireContext()))
            binding.textAlarmEvents
                .setAlarmState(AppAlarmManager.isEventAlarmRunning(requireContext()))
        }
    }
}