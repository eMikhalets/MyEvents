package com.emikhalets.mydates.ui.events_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emikhalets.mydates.data.database.ListResult
import com.emikhalets.mydates.data.database.entities.Event
import com.emikhalets.mydates.data.repositories.RoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventsListVM @Inject constructor(
    private val repository: RoomRepository
) : ViewModel() {

    private val _events = MutableLiveData<List<Event>>()
    val events get(): LiveData<List<Event>> = _events

    private val _eventAdd = MutableLiveData<Event>()
    val eventAdd get(): LiveData<Event> = _eventAdd

    private val _loading = MutableLiveData<Boolean>()
    val loading get(): LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String>()
    val error get(): LiveData<String> = _error

    fun loadAllEvents(lastUpdate: Long) {
        viewModelScope.launch {
            _loading.postValue(true)
            when (val result = repository.getAllEvents(lastUpdate)) {
                ListResult.EmptyList -> _error.postValue("Empty events list")
                is ListResult.Error -> _error.postValue(result.message)
                is ListResult.Success -> _events.postValue(result.data)
            }
            _loading.postValue(false)
        }
    }
}