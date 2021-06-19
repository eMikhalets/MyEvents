package com.emikhalets.mydates.ui.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.emikhalets.mydates.R
import com.emikhalets.mydates.databinding.FragmentSettingsBinding
import com.emikhalets.mydates.foreground.NotifMonthWorker
import com.emikhalets.mydates.utils.navigateBack
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private val binding by viewBinding(FragmentSettingsBinding::bind)
    private val viewModel: SettingsVM by viewModels()

    private var date = 0L

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareSettings()
        clickListeners()
        observe()
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
            switchAllNotif.setOnCheckedChangeListener { _, isChecked ->
                applyAllNotifications(isChecked)
            }
            switchMonthNotif.setOnCheckedChangeListener { view, isChecked ->
                applyMonthNotifications(isChecked)
            }
            switchWeekNotif.setOnCheckedChangeListener { view, isChecked -> }
            switchTwoDayNotif.setOnCheckedChangeListener { view, isChecked -> }
            switchDayNotif.setOnCheckedChangeListener { view, isChecked -> }
            switchTodayNotif.setOnCheckedChangeListener { view, isChecked -> }
        }
    }

    private fun applyAllNotifications(isChecked: Boolean) {
        binding.apply {
            switchMonthNotif.isEnabled = isChecked
            switchWeekNotif.isEnabled = isChecked
            switchTwoDayNotif.isEnabled = isChecked
            switchDayNotif.isEnabled = isChecked
            switchTodayNotif.isEnabled = isChecked
        }
        if (!isChecked) {
            binding.apply {
                switchMonthNotif.isChecked = false
                switchWeekNotif.isChecked = false
                switchTwoDayNotif.isChecked = false
                switchDayNotif.isChecked = false
                switchTodayNotif.isChecked = false
            }
            applyMonthNotifications(isChecked)
        }
    }

    private fun applyMonthNotifications(isChecked: Boolean) {
        if (isChecked) {
            val work = PeriodicWorkRequest.Builder(NotifMonthWorker::class.java, 15, TimeUnit.MINUTES)
                .build()
            val workManager = WorkManager.getInstance(requireContext())
            workManager.enqueue(work)
//            workManager.enqueueUniquePeriodicWork(
//                "notif_month",
//                ExistingPeriodicWorkPolicy.REPLACE,
//                work
//            )
        } else {
            WorkManager.getInstance(requireContext()).cancelUniqueWork("notif_month")
        }
    }

    private fun observe() {
        viewModel.eventAdd.observe(viewLifecycleOwner) { navigateBack() }
    }
}