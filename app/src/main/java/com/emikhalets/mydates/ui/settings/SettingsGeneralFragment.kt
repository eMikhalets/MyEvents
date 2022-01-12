package com.emikhalets.mydates.ui.settings

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import by.kirich1409.viewbindingdelegate.viewBinding
import com.emikhalets.mydates.R
import com.emikhalets.mydates.databinding.FragmentSettingsGeneralBinding
import com.emikhalets.mydates.ui.base.BaseFragment
import com.emikhalets.mydates.utils.di.appComponent
import com.emikhalets.mydates.utils.enums.AppTheme
import com.emikhalets.mydates.utils.enums.AppTheme.Companion.getName
import com.emikhalets.mydates.utils.enums.Language
import com.emikhalets.mydates.utils.enums.Language.Companion.getName
import com.emikhalets.mydates.utils.extentions.launchMainScope
import com.emikhalets.mydates.utils.extentions.setActivityLanguage

class SettingsGeneralFragment : BaseFragment(R.layout.fragment_settings_general) {

    private val binding by viewBinding(FragmentSettingsGeneralBinding::bind)

    private var isLanguageExpanded = false
    private var isThemeExpanded = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        launchMainScope {
            binding.apply {
                val language = Language.get(appComponent.appPreferences.getLanguage())
                val theme = AppTheme.get(appComponent.appPreferences.getTheme())
                textLanguage.text = language.getName(requireContext())
                textTheme.text = theme.getName(requireContext())
            }
        }

        listeners()
    }

    private fun listeners() {
        binding.apply {
            layLanguage.setOnClickListener {
                setLanguagesVisibility(!isLanguageExpanded)
            }

            textLanguageRu.setOnClickListener {
                setLanguage(Language.RUSSIAN)
            }

            textLanguageEn.setOnClickListener {
                setLanguage(Language.ENGLISH)
            }

            layTheme.setOnClickListener {
                setThemesVisibility(!isThemeExpanded)
            }

            textThemeLight.setOnClickListener {
                setTheme(AppTheme.LIGHT)
            }

            textThemeDark.setOnClickListener {
                setTheme(AppTheme.DARK)
            }
        }
    }

    private fun setLanguagesVisibility(show: Boolean) {
        isLanguageExpanded = show
        binding.textLanguageRu.isGone = !show
        binding.textLanguageEn.isGone = !show
    }

    private fun setThemesVisibility(show: Boolean) {
        isThemeExpanded = show
        binding.textThemeLight.isGone = !show
        binding.textThemeDark.isGone = !show
    }

    private fun setLanguage(language: Language) {
        binding.textLanguage.text = language.getName(requireContext())
        setLanguagesVisibility(false)
        launchMainScope {
            if (appComponent.appPreferences.getLanguage() != language.langCode) {
                setActivityLanguage(language)
            }
        }
    }

    private fun setTheme(theme: AppTheme) {
        binding.textTheme.text = theme.getName(requireContext())
        setThemesVisibility(false)
        launchMainScope {
            if (appComponent.appPreferences.getTheme() != theme.themeRes) {
                appComponent.appPreferences.setTheme(theme.themeRes)
                requireActivity().recreate()
            }
        }
    }
}