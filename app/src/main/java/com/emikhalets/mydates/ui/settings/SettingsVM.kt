package com.emikhalets.mydates.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emikhalets.mydates.data.database.ListResult
import com.emikhalets.mydates.data.repositories.RoomRepository
import com.emikhalets.mydates.utils.BackupEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SettingsVM @Inject constructor(
    private val repository: RoomRepository
) : ViewModel() {

    private val _importEvents = MutableLiveData<String>()
    val importEvents: LiveData<String> = _importEvents

    private val _error = MutableLiveData<String>()
    val error get(): LiveData<String> = _error

    fun createEventsJson(filePath: File?) {
        viewModelScope.launch {
            when (val result = repository.getAllEvents()) {
                ListResult.EmptyList -> _error.postValue("Empty events list")
                is ListResult.Error -> _error.postValue(result.message)
                is ListResult.Success -> {
                    val backupPath = BackupEvents.createJsonEvents(filePath, result.data)
                    _importEvents.postValue(backupPath)
                }
            }
        }
    }
}