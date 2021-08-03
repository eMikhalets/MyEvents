package com.emikhalets.mydates.ui.settings

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emikhalets.mydates.data.database.ListResult
import com.emikhalets.mydates.data.repositories.RoomRepository
import com.emikhalets.mydates.utils.BackupHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsVM @Inject constructor(
    private val repository: RoomRepository
) : ViewModel() {

    private val _state = MutableStateFlow<SettingsState>(SettingsState.Init)
    val state: StateFlow<SettingsState> = _state

    fun getAllEventsAndFillFile(context: Context, uri: Uri) {
        _state.value = SettingsState.Init
        viewModelScope.launch {
            _state.value = SettingsState.Loading
            when (val result = repository.getAllEvents()) {
                is ListResult.Error -> _state.value = SettingsState.Error(result.exception)
                is ListResult.Success -> {
                    val complete = BackupHandler.fillCreatedFile(context, uri, result.data)
                    _state.value = if (complete) SettingsState.Exported
                    else SettingsState.ExportingError
                }
            }
        }
    }

    fun readFileAndRecreateEventsTable(context: Context, uri: Uri) {
        _state.value = SettingsState.Init
        viewModelScope.launch {
            _state.value = SettingsState.Loading
            val complete = BackupHandler.readFileAndCreateEventsList(context, uri)
            _state.value = if (complete.isNotEmpty()) {
                repository.dropEvents()
                repository.insertAllEvents(complete)
                SettingsState.Imported
            } else {
                SettingsState.ImportingError
            }
        }
    }
}