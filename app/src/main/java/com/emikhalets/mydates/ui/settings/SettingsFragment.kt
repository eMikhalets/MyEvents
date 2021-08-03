package com.emikhalets.mydates.ui.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.emikhalets.mydates.R
import com.emikhalets.mydates.databinding.FragmentSettingsBinding
import com.emikhalets.mydates.foreground.EventsReceiver
import com.emikhalets.mydates.utils.*
import com.emikhalets.mydates.utils.activity_result.DocumentCreator
import com.emikhalets.mydates.utils.activity_result.DocumentPicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

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
        observe()
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
            val hour = Preferences.getNotificationHour(requireContext())
            val minute = Preferences.getNotificationMinute(requireContext())
            binding.textTime.text = formatTime(hour, minute)

            switchAllNotifications.isChecked =
                Preferences.getNotificationAll(requireContext())
            switchMonthNotifications.isChecked =
                Preferences.getNotificationMonth(requireContext())
            switchWeekNotifications.isChecked =
                Preferences.getNotificationWeek(requireContext())
            switchTwoDayNotifications.isChecked =
                Preferences.getNotificationTwoDay(requireContext())
            switchDayNotifications.isChecked =
                Preferences.getNotificationDay(requireContext())
            switchTodayNotifications.isChecked =
                Preferences.getNotificationToday(requireContext())

            switchAllNotifications.isChecked.apply {
                if (this) layTime.visibility = View.VISIBLE
                else layTime.visibility = View.GONE
                switchMonthNotifications.isEnabled = this
                switchWeekNotifications.isEnabled = this
                switchTwoDayNotifications.isEnabled = this
                switchDayNotifications.isEnabled = this
                switchTodayNotifications.isEnabled = this
            }
        }
    }

    private fun observe() {
        lifecycleScope.launch {
            viewModel.state.collect { renderState(it) }
        }
    }

    private fun renderState(state: SettingsState) {
        when (state) {
            is SettingsState.Error -> {
                toast(state.message)
            }
            SettingsState.ExportingError -> {
                toast(R.string.export_failure)
            }
            SettingsState.ImportingError -> {
                toast(R.string.import_failure)
            }
            SettingsState.Exported -> {
                toast(R.string.export_success)
            }
            SettingsState.Imported -> {
                toast(R.string.import_success)
            }
            SettingsState.Loading -> {
            }
            SettingsState.Init -> {
            }
        }
    }

    private fun clickListeners() {
        binding.apply {
            layTime.setOnClickListener {
                startTimePickerDialog { hour, minute ->
                    binding.textTime.text = formatTime(hour, minute)
                    saveNotificationsTime(hour, minute)
                }
            }

            switchAllNotifications.setOnCheckedChangeListener { _, isChecked ->
                Preferences.setNotificationAll(requireContext(), isChecked)
                setNotificationsEnabled(isChecked)
            }
            switchMonthNotifications.setOnCheckedChangeListener { _, isChecked ->
                Preferences.setNotificationMonth(requireContext(), isChecked)
            }
            switchWeekNotifications.setOnCheckedChangeListener { _, isChecked ->
                Preferences.setNotificationWeek(requireContext(), isChecked)
            }
            switchTwoDayNotifications.setOnCheckedChangeListener { _, isChecked ->
                Preferences.setNotificationTwoDay(requireContext(), isChecked)
            }
            switchDayNotifications.setOnCheckedChangeListener { _, isChecked ->
                Preferences.setNotificationDay(requireContext(), isChecked)
            }
            switchTodayNotifications.setOnCheckedChangeListener { _, isChecked ->
                Preferences.setNotificationToday(requireContext(), isChecked)
            }

            textRestartNotifications.setOnClickListener { restartAlarmManagers() }
            textImport.setOnClickListener { importEvents() }
            textExport.setOnClickListener { documentCreator.createFile() }
        }
    }

    private fun importEvents() {
        startDeleteDialog(getString(R.string.dialog_import_alert)) {
            documentPicker.openFile()
        }
    }

    private fun restartAlarmManagers() {
        requireContext().restartAlarmManagers()
        toast(R.string.alarm_managers_restarted)
    }

    private fun saveNotificationsTime(hour: Int, minute: Int) {
        Preferences.setNotificationHour(requireContext(), hour)
        Preferences.setNotificationMinute(requireContext(), minute)
        setRepeatingAlarm(
            context = requireContext(),
            hour = 11,
            minute = 0,
            receiver = EventsReceiver::class.java,
            requestCode = APP_EVENTS_ALARM_REQUEST_CODE
        )
    }

    private fun formatTime(hour: Int, minute: Int): String {
        return if (minute > 9) "$hour:$minute"
        else "$hour:0$minute"
    }

    private fun setNotificationsEnabled(bool: Boolean) {
        binding.apply {
            if (bool) layTime.visibility = View.VISIBLE
            else layTime.visibility = View.GONE
            switchMonthNotifications.isEnabled = bool
            switchWeekNotifications.isEnabled = bool
            switchTwoDayNotifications.isEnabled = bool
            switchDayNotifications.isEnabled = bool
            switchTodayNotifications.isEnabled = bool
        }
    }
}