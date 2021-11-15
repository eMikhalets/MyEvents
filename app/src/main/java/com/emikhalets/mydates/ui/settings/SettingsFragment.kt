package com.emikhalets.mydates.ui.settings

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.emikhalets.mydates.R
import com.emikhalets.mydates.databinding.FragmentSettingsBinding
import com.emikhalets.mydates.ui.base.BaseFragment
import com.emikhalets.mydates.utils.AppAlarmManager
import com.emikhalets.mydates.utils.AppDialogManager
import com.emikhalets.mydates.utils.activity_result.DocumentCreator
import com.emikhalets.mydates.utils.activity_result.DocumentPicker
import com.emikhalets.mydates.utils.di.appComponent
import com.emikhalets.mydates.utils.enums.AppTheme
import com.emikhalets.mydates.utils.enums.AppTheme.Companion.getThemeName
import com.emikhalets.mydates.utils.enums.Language
import com.emikhalets.mydates.utils.enums.Language.Companion.getLanguageName
import com.emikhalets.mydates.utils.extentions.launchMainScope
import com.emikhalets.mydates.utils.extentions.setActivityLanguage
import com.emikhalets.mydates.utils.extentions.toast
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    private val binding by viewBinding(FragmentSettingsBinding::bind)
    private val viewModel by viewModels<SettingsVM> { viewModelFactory }

    private lateinit var documentCreator: DocumentCreator
    private lateinit var documentPicker: DocumentPicker

    private var isLanguageExpanded = false
    private var isThemeExpanded = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        launchMainScope {
            binding.apply {
                textLanguage.text = Language.get(appComponent.appPreferences.getLanguage())
                    .getLanguageName(requireContext())
                textTheme.text = AppTheme.get(appComponent.appPreferences.getTheme())
                    .getThemeName(requireContext())

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
        generalClicks()
        notificationsClicks()
        backupsClicks()
    }

    private fun generalClicks() {
        binding.apply {
            layLanguage.setOnClickListener {
                isLanguageExpanded = !isLanguageExpanded
                textLanguageRu.isGone = isLanguageExpanded
                textLanguageEn.isGone = isLanguageExpanded
            }

            textLanguageRu.setOnClickListener {
                textLanguage.text = Language.RUSSIAN.getLanguageName(requireContext())
                setLanguage(Language.RUSSIAN)
            }

            textLanguageEn.setOnClickListener {
                textLanguage.text = Language.ENGLISH.getLanguageName(requireContext())
                setLanguage(Language.ENGLISH)
            }

            layTheme.setOnClickListener {
                isThemeExpanded = !isThemeExpanded
                textThemeLight.isGone = isThemeExpanded
                textThemeDark.isGone = isLanguageExpanded
            }

            textThemeLight.setOnClickListener {
                setTheme(AppTheme.LIGHT)
            }

            textThemeDark.setOnClickListener {
                setTheme(AppTheme.DARK)
            }
        }
    }

    private fun setLanguage(language: Language) {
        isLanguageExpanded = false
        binding.textLanguageRu.isGone = false
        binding.textLanguageEn.isGone = false
        launchMainScope {
            if (appComponent.appPreferences.getLanguage() != language.value) {
                setActivityLanguage(language)
            }
        }
    }

    private fun setTheme(selectedTheme: AppTheme) {
        isThemeExpanded = false
        binding.textThemeLight.isGone = false
        binding.textThemeDark.isGone = false
        binding.textTheme.text = selectedTheme.getThemeName(requireContext())
        launchMainScope {
            appComponent.appPreferences.setTheme(selectedTheme.themeRes)
            requireActivity().recreate()
        }
    }

    private fun notificationsClicks() {
        binding.apply {
            layTime.setOnClickListener {
                AppDialogManager.showTimePickerDialog(requireContext()) { hour, minute ->
                    binding.textTime.text = formatTime(hour, minute)
                    saveNotificationsTime(hour, minute)
                }
            }

            switchAllNotifications.setOnCheckedChangeListener { _, isChecked ->
                launchMainScope {
                    appComponent.appPreferences.setNotificationAll(isChecked)
                    setNotificationsEnabled(isChecked)
                }
            }

            switchMonthNotifications.setOnCheckedChangeListener { _, isChecked ->
                launchMainScope {
                    appComponent.appPreferences.setNotificationMonth(isChecked)
                }
            }

            switchWeekNotifications.setOnCheckedChangeListener { _, isChecked ->
                launchMainScope {
                    appComponent.appPreferences.setNotificationWeek(isChecked)
                }
            }

            switchTwoDayNotifications.setOnCheckedChangeListener { _, isChecked ->
                launchMainScope {
                    appComponent.appPreferences.setNotificationTwoDay(isChecked)
                }
            }

            switchDayNotifications.setOnCheckedChangeListener { _, isChecked ->
                launchMainScope {
                    appComponent.appPreferences.setNotificationDay(isChecked)
                }
            }

            switchTodayNotifications.setOnCheckedChangeListener { _, isChecked ->
                launchMainScope {
                    appComponent.appPreferences.setNotificationToday(isChecked)
                }
            }
        }
    }

    private fun backupsClicks() {
        binding.apply {
            textRestartNotifications.setOnClickListener { restartAlarmManagers() }

            textImport.setOnClickListener { importEvents() }

            textExport.setOnClickListener { documentCreator.createFile() }
        }
    }

    private fun importEvents() {
        AppDialogManager.showDeleteDialog(
            requireContext(),
            getString(R.string.dialog_import_alert)
        ) {
            documentPicker.openFile()
        }
    }

    private fun restartAlarmManagers() {
        AppAlarmManager.scheduleAllAlarms(requireContext())
        toast(R.string.alarm_managers_restarted)
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