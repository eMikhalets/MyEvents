package com.emikhalets.mydates.ui.settings

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.emikhalets.mydates.R
import com.emikhalets.mydates.databinding.FragmentSettingsBinding
import com.emikhalets.mydates.ui.base.BaseFragment
import com.emikhalets.mydates.utils.AppNavigationManager

class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    private val binding by viewBinding(FragmentSettingsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            textGeneral.setOnClickListener {
                AppNavigationManager.toSettingsGeneral(this@SettingsFragment)
            }

            textNotifications.setOnClickListener {
                AppNavigationManager.toSettingsNotifications(this@SettingsFragment)
            }

            textBackups.setOnClickListener {
                AppNavigationManager.toSettingsBackups(this@SettingsFragment)
            }
        }
    }
}