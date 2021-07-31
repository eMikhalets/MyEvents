package com.emikhalets.mydates.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.emikhalets.mydates.R
import com.emikhalets.mydates.databinding.FragmentSettingsBinding
import com.emikhalets.mydates.foreground.EventsReceiver
import com.emikhalets.mydates.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private val binding by viewBinding(FragmentSettingsBinding::bind)
    private val viewModel: SettingsVM by viewModels()

    private lateinit var documentCreator: DocumentCreator
    private lateinit var documentPicker: DocumentPicker

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle(R.string.title_settings)
        initActivityResult()
        prepareSettings()
        clickListeners()
    }

    private fun initActivityResult() {
        documentCreator = DocumentCreator(
            registry = requireActivity().activityResultRegistry,
            lifecycleOwner = viewLifecycleOwner,
            onResult = { viewModel.getAllEventsAndFillFile(requireContext(), it) }
        )
        documentPicker = DocumentPicker(
            registry = requireActivity().activityResultRegistry,
            lifecycleOwner = viewLifecycleOwner,
            onResult = { viewModel.readFileAndRecreateEventsTable(requireContext(), it) }
        )
    }

    private fun prepareSettings() {
        binding.apply {
            binding.textTime.text = formatTime(getNotifHour(), getNotifMinute())
            switchAllNotif.isChecked = getNotifPref(APP_SP_NOTIF_ALL_FLAG)
            switchMonthNotif.isChecked = getNotifPref(APP_SP_NOTIF_MONTH_FLAG)
            switchWeekNotif.isChecked = getNotifPref(APP_SP_NOTIF_WEEK_FLAG)
            switchTwoDayNotif.isChecked = getNotifPref(APP_SP_NOTIF_TWO_DAY_FLAG)
            switchDayNotif.isChecked = getNotifPref(APP_SP_NOTIF_DAY_FLAG)
            switchTodayNotif.isChecked = getNotifPref(APP_SP_NOTIF_TODAY_FLAG)

            switchMonthNotif.isEnabled = switchAllNotif.isChecked
            switchWeekNotif.isEnabled = switchAllNotif.isChecked
            switchTwoDayNotif.isEnabled = switchAllNotif.isChecked
            switchDayNotif.isEnabled = switchAllNotif.isChecked
            switchTodayNotif.isEnabled = switchAllNotif.isChecked
        }
    }

    private fun clickListeners() {
        binding.apply {
            layoutTime.setOnClickListener {
                if (switchAllNotif.isChecked) {
                    startTimePickerDialog { hour, minute ->
                        binding.textTime.text = formatTime(hour, minute)
                        saveNotificationsTime(hour, minute)
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
            textRestartAlarmManager.setOnClickListener {
                restartAlarmManagers()
            }
            textImportEvents.setOnClickListener { importEvents() }
            textExportEvents.setOnClickListener { exportEvents() }
        }
    }

    private fun importEvents() {
        startDeleteDialog(getString(R.string.dialog_import_alert)) {
            documentPicker.openFile()
        }
        viewModel.importEvents.observe(viewLifecycleOwner) {
            if (it) toast(R.string.import_success)
            else toast(R.string.import_failure)
        }
    }

    private fun exportEvents() {
        documentCreator.createFile()
        viewModel.exportEvents.observe(viewLifecycleOwner) {
            if (it) toast(R.string.export_success)
            else toast(R.string.export_failure)
        }
    }

    private fun restartAlarmManagers() {
        val sp = requireContext().getSharedPreferences(APP_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val notifHour = sp.getInt(APP_SP_EVENT_HOUR, 11)
        val notifMinute = sp.getInt(APP_SP_EVENT_MINUTE, 0)

        setRepeatingAlarm(
            requireContext(),
            EVENTS_UPDATE_HOUR,
            EVENTS_UPDATE_MINUTE,
            EventsReceiver::class.java,
            APP_UPDATE_ALARM_REQUEST_CODE
        )

        setRepeatingAlarm(
            requireContext(),
            notifHour,
            notifMinute,
            EventsReceiver::class.java,
            APP_EVENTS_ALARM_REQUEST_CODE
        )

        toast(R.string.alarm_managers_restarted)
    }

    private fun getNotifHour(): Int {
        return requireContext().getSharedPreferences(APP_SHARED_PREFERENCES, Context.MODE_PRIVATE)
            .getInt(APP_SP_EVENT_HOUR, 11)
    }

    private fun saveNotificationsTime(hour: Int, minute: Int) {
        val sp = requireContext()
            .getSharedPreferences(APP_SHARED_PREFERENCES, Context.MODE_PRIVATE).edit()
        sp.putInt(APP_SP_EVENT_HOUR, hour)
        sp.putInt(APP_SP_EVENT_MINUTE, minute)
        sp.apply()

        setRepeatingAlarm(
            requireContext(),
            11,
            0,
            EventsReceiver::class.java,
            APP_EVENTS_ALARM_REQUEST_CODE
        )
    }

    private fun getNotifMinute(): Int {
        return requireContext().getSharedPreferences(APP_SHARED_PREFERENCES, Context.MODE_PRIVATE)
            .getInt(APP_SP_EVENT_MINUTE, 0)
    }

    private fun getNotifPref(key: String): Boolean {
        return requireContext().getSharedPreferences(APP_SHARED_PREFERENCES, Context.MODE_PRIVATE)
            .getBoolean(key, false)
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