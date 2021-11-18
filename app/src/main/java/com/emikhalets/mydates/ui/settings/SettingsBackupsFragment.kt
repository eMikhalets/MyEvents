package com.emikhalets.mydates.ui.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.emikhalets.mydates.R
import com.emikhalets.mydates.databinding.FragmentSettingsBackupsBinding
import com.emikhalets.mydates.ui.base.BaseFragment
import com.emikhalets.mydates.utils.AppAlarmManager
import com.emikhalets.mydates.utils.AppDialogManager
import com.emikhalets.mydates.utils.activity_result.DocumentCreator
import com.emikhalets.mydates.utils.activity_result.DocumentPicker
import com.emikhalets.mydates.utils.di.appComponent
import com.emikhalets.mydates.utils.enums.AppTheme
import com.emikhalets.mydates.utils.enums.AppTheme.Companion.getName
import com.emikhalets.mydates.utils.enums.Language
import com.emikhalets.mydates.utils.enums.Language.Companion.getName
import com.emikhalets.mydates.utils.extentions.launchMainScope
import com.emikhalets.mydates.utils.extentions.setActivityLanguage
import com.emikhalets.mydates.utils.extentions.toast
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SettingsBackupsFragment : BaseFragment(R.layout.fragment_settings_backups) {

    private val binding by viewBinding(FragmentSettingsBackupsBinding::bind)
    private val viewModel by viewModels<SettingsBackupsVM> { viewModelFactory }

    private lateinit var documentCreator: DocumentCreator
    private lateinit var documentPicker: DocumentPicker

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initActivityResult()
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

    private fun observe() {
        lifecycleScope.launch {
            viewModel.state.collect { renderState(it) }
        }
    }

    private fun renderState(state: SettingsBackupsState) {
        when (state) {
            is SettingsBackupsState.Error -> {
                toast(state.message)
            }
            SettingsBackupsState.ExportingError -> {
                toast(R.string.export_failure)
            }
            SettingsBackupsState.ImportingError -> {
                toast(R.string.import_failure)
            }
            SettingsBackupsState.Exported -> {
                toast(R.string.export_success)
            }
            SettingsBackupsState.Imported -> {
                toast(R.string.import_success)
            }
            SettingsBackupsState.Loading -> {
            }
            SettingsBackupsState.Init -> {
            }
        }
    }

    private fun clickListeners() {
        binding.apply {

            textImport.setOnClickListener {
                importEvents()
            }

            textExport.setOnClickListener {
                documentCreator.createFile()
            }
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
}