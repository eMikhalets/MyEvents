package com.emikhalets.mydates.ui.events_list

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emikhalets.mydates.data.database.ListResult
import com.emikhalets.mydates.data.database.entities.Event
import com.emikhalets.mydates.data.repositories.RoomRepository
import com.emikhalets.mydates.utils.sortWithDividers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventsListVM @Inject constructor(
    private val repository: RoomRepository
) : ViewModel() {

    var state by mutableStateOf<EventsListState>(EventsListState.Init)
        private set

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
            Log.d("BEFORE", "state = $state")
            state = when (val result = repository.getAllEvents(lastUpdate)) {
                ListResult.EmptyList -> EventsListState.Empty
                is ListResult.Error -> EventsListState.Error(result.exception)
                is ListResult.Success -> EventsListState.Events(sortWithDividers(result.data))
            }
            Log.d("AFTER", "state = $state")
        }
    }
}