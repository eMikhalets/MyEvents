package com.emikhalets.mydates.ui.settings

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.emikhalets.mydates.R
import com.emikhalets.mydates.databinding.FragmentSettingsBackupsBinding
import com.emikhalets.mydates.ui.base.BaseFragment
import com.emikhalets.mydates.utils.AppDialogManager
import com.emikhalets.mydates.utils.activity_result.DocumentCreator
import com.emikhalets.mydates.utils.activity_result.DocumentPicker
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

        lifecycleScope.launch {
            viewModel.state.collect { renderState(it) }
        }

        documentCreator = DocumentCreator(this) { export(it) }
        documentPicker = DocumentPicker(this) { import(it) }

        listeners()
    }

    private fun listeners() {
        binding.apply {
            textImport.setOnClickListener {
                importEvents()
            }

            textExport.setOnClickListener {
                documentCreator.createFile()
            }
        }
    }

    private fun renderState(state: SettingsBackupsState) {
        when (state) {
            SettingsBackupsState.Init,
            SettingsBackupsState.Loading -> Unit
            SettingsBackupsState.Exported -> toast(R.string.export_success)
            SettingsBackupsState.Imported -> toast(R.string.import_success)
            SettingsBackupsState.ExportingError -> toast(R.string.export_failure)
            SettingsBackupsState.ImportingError -> toast(R.string.import_failure)
            is SettingsBackupsState.Error -> toast(state.message)
        }
    }

    private fun export(uri: Uri) {
        viewModel.getAllEventsAndFillFile(requireContext(), uri)
    }

    private fun import(uri: Uri) {
        viewModel.readFileAndRecreateEventsTable(requireContext(), uri)
    }

    private fun importEvents() {
        AppDialogManager.showDeleteEvent(
            context = requireContext(),
            content = getString(R.string.dialog_import_alert),
            callback = { documentPicker.openFile() }
        )
    }
}