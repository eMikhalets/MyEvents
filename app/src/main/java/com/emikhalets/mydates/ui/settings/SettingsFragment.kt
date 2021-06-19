package com.emikhalets.mydates.ui.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.emikhalets.mydates.R
import com.emikhalets.mydates.databinding.FragmentSettingsBinding
import com.emikhalets.mydates.utils.navigateBack
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private val binding by viewBinding(FragmentSettingsBinding::bind)
    private val viewModel: SettingsVM by viewModels()

    private var date = 0L

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickListeners()
        observe()
    }

    private fun clickListeners() {
        binding.apply {
            switchAllNotif.setOnCheckedChangeListener { view, isChecked -> }
            switchMonthNotif.setOnCheckedChangeListener { view, isChecked -> }
            switchWeekNotif.setOnCheckedChangeListener { view, isChecked -> }
            switchTwoDayNotif.setOnCheckedChangeListener { view, isChecked -> }
            switchDayNotif.setOnCheckedChangeListener { view, isChecked -> }
            switchTodayNotif.setOnCheckedChangeListener { view, isChecked -> }
        }
    }

    private fun observe() {
        viewModel.eventAdd.observe(viewLifecycleOwner) { navigateBack() }
    }
}