package com.emikhalets.mydates.ui.event_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emikhalets.mydates.data.database.CompleteResult
import com.emikhalets.mydates.data.database.entities.Event
import com.emikhalets.mydates.data.repositories.RoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventDetailsVM @Inject constructor(
    private val repository: RoomRepository
) : ViewModel() {

    private var updateFlag = false

    private val _eventUpdate = MutableLiveData<Boolean>()
    val eventUpdate get(): LiveData<Boolean> = _eventUpdate

    private val _eventDelete = MutableLiveData<Boolean>()
    val eventDelete get(): LiveData<Boolean> = _eventDelete

    private val _error = MutableLiveData<String>()
    val error get(): LiveData<String> = _error

    fun deleteEvent(event: Event) {
        viewModelScope.launch {
            when (val result = repository.deleteEvent(event)) {
                CompleteResult.Complete -> _eventDelete.postValue(true)
                is CompleteResult.Error -> _error.postValue(result.message)
            }
        }
    }

    fun updateEvent(event: Event) {
        viewModelScope.launch {
            when (val result = repository.insertEvent(event)) {
                CompleteResult.Complete -> {
                    updateFlag = !updateFlag
                    _eventUpdate.postValue(updateFlag)
                }
                is CompleteResult.Error -> _error.postValue(result.message)
            }
        }
    }
}