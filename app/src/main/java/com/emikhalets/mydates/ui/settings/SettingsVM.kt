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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsVM @Inject constructor(
    private val repository: RoomRepository
) : ViewModel() {

    private val _exportEvents = MutableLiveData<Boolean>()
    val exportEvents: LiveData<Boolean> = _exportEvents

    private val _importEvents = MutableLiveData<Boolean>()
    val importEvents: LiveData<Boolean> = _importEvents

    private val _error = MutableLiveData<String>()
    val error get(): LiveData<String> = _error

    fun getAllEventsAndFillFile(context: Context, uri: Uri) {
        viewModelScope.launch {
            when (val result = repository.getAllEvents()) {
                ListResult.EmptyList -> _error.postValue("Empty events list")
                is ListResult.Error -> _error.postValue(result.message)
                is ListResult.Success -> {
                    val complete = BackupHandler.fillCreatedFile(context, uri, result.data)
                    _exportEvents.postValue(complete)
                }
            }
        }
    }

    fun readFileAndRecreateEventsTable(context: Context, uri: Uri) {
        viewModelScope.launch {
            val complete = BackupHandler.readFileAndCreateEventsList(context, uri)
            if (complete.isNotEmpty()) {
                repository.dropEvents()
                repository.insertAllEvents(complete)
                _importEvents.postValue(true)
            } else {
                _importEvents.postValue(false)
            }
        }
    }
}