package com.emikhalets.mydates.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.emikhalets.mydates.R
import com.emikhalets.mydates.databinding.FragmentSettingsBinding
import com.emikhalets.mydates.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private val binding by viewBinding(FragmentSettingsBinding::bind)
    private val viewModel: SettingsVM by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareSettings()
        clickListeners()
    }

    private fun prepareSettings() {
        binding.apply {
            val isAllNotifChecked = binding.switchAllNotif.isChecked
            switchMonthNotif.isEnabled = isAllNotifChecked
            switchWeekNotif.isEnabled = isAllNotifChecked
            switchTwoDayNotif.isEnabled = isAllNotifChecked
            switchDayNotif.isEnabled = isAllNotifChecked
            switchTodayNotif.isEnabled = isAllNotifChecked
        }
    }

    private fun clickListeners() {
        binding.apply {
            layoutTime.setOnClickListener {
                if (switchAllNotif.isChecked) {
                    startTimePickerDialog { hour, minute ->
                        binding.textTime.text = formatTime(hour, minute)
                        resetEventAlarm(hour, minute)
                    }
                }
            }

            switchAllNotif.setOnCheckedChangeListener { _, isChecked ->
                saveSharedPrefNotif(APP_SP_NOTIF_ALL_FLAG, isChecked)
                switchMonthNotif.isEnabled = isChecked
                switchWeekNotif.isEnabled = isChecked
                switchTwoDayNotif.isEnabled = isChecked
                switchDayNotif.isEnabled = isChecked
                switchTodayNotif.isEnabled = isChecked
                if (isChecked) layoutTime.visibility = View.VISIBLE
                else layoutTime.visibility = View.GONE
            }

            switchMonthNotif.setOnCheckedChangeListener { _, isChecked ->
                saveSharedPrefNotif(APP_SP_NOTIF_MONTH_FLAG, isChecked)
            }

            switchWeekNotif.setOnCheckedChangeListener { _, isChecked ->
                saveSharedPrefNotif(APP_SP_NOTIF_WEEK_FLAG, isChecked)
            }

            switchTwoDayNotif.setOnCheckedChangeListener { _, isChecked ->
                saveSharedPrefNotif(APP_SP_NOTIF_TWO_DAY_FLAG, isChecked)
            }

            switchDayNotif.setOnCheckedChangeListener { _, isChecked ->
                saveSharedPrefNotif(APP_SP_NOTIF_DAY_FLAG, isChecked)
            }

            switchTodayNotif.setOnCheckedChangeListener { _, isChecked ->
                saveSharedPrefNotif(APP_SP_NOTIF_TODAY_FLAG, isChecked)
            }
        }
    }

    private fun saveSharedPrefNotif(key: String, value: Boolean) {
        requireContext().getSharedPreferences(APP_SHARED_PREFERENCES, Context.MODE_PRIVATE).edit()
            .putBoolean(key, value).apply()
    }

    private fun formatTime(hour: Int, minute: Int): String {
        return if (minute > 9) requireContext().getString(R.string.time_hour_minute, hour, minute)
        else requireContext().getString(R.string.time_hour_minute_sub, hour, minute)
    }
}