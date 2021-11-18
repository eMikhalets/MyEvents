package com.emikhalets.mydates.ui.settings

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emikhalets.mydates.data.database.ListResult
import com.emikhalets.mydates.data.repositories.DatabaseRepository
import com.emikhalets.mydates.utils.AppBackupManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsBackupsVM @Inject constructor(
    private val repository: DatabaseRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<SettingsBackupsState>(SettingsBackupsState.Init)
    val state: StateFlow<SettingsBackupsState> = _state

    fun getAllEventsAndFillFile(context: Context, uri: Uri) {
        _state.value = SettingsBackupsState.Init
        viewModelScope.launch {
            _state.value = SettingsBackupsState.Loading
            when (val result = repository.getAllEvents()) {
                is ListResult.Error -> _state.value = SettingsBackupsState.Error(result.exception)
                is ListResult.Success -> {
                    val complete = AppBackupManager.fillCreatedFile(context, uri, result.data)
                    _state.value = if (complete) SettingsBackupsState.Exported
                    else SettingsBackupsState.ExportingError
                }
            }
        }
    }

    fun readFileAndRecreateEventsTable(context: Context, uri: Uri) {
        _state.value = SettingsBackupsState.Init
        viewModelScope.launch {
            _state.value = SettingsBackupsState.Loading
            val complete = AppBackupManager.readFileAndCreateEventsList(context, uri)
            _state.value = if (complete.isNotEmpty()) {
                repository.dropEvents()
                repository.insertAllEvents(complete)
                SettingsBackupsState.Imported
            } else {
                SettingsBackupsState.ImportingError
            }
        }
    }
}