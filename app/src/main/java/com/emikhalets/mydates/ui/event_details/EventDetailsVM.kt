package com.emikhalets.mydates.ui.event_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emikhalets.mydates.data.database.CompleteResult
import com.emikhalets.mydates.data.database.entities.Event
import com.emikhalets.mydates.data.repositories.RoomRepository
import com.emikhalets.mydates.utils.EventType
import com.emikhalets.mydates.utils.calculateParameters
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventDetailsVM @Inject constructor(
    private val repository: RoomRepository
) : ViewModel() {

    private val _state = MutableStateFlow<EventDetailsState>(EventDetailsState.Init)
    val state: StateFlow<EventDetailsState> = _state

    fun deleteEvent(event: Event) {
        viewModelScope.launch {
            when (val result = repository.deleteEvent(event)) {
                is CompleteResult.Error -> _state.value = EventDetailsState.Error(result.exception)
                CompleteResult.Complete -> _state.value = EventDetailsState.Deleted
            }
        }
    }

    fun updateEvent(
        eventType: EventType,
        name: String,
        lastname: String,
        middleName: String,
        date: Long,
        withoutYear: Boolean
    ) {
        _state.value = EventDetailsState.Init
        if (name.isEmpty()) {
            _state.value = EventDetailsState.EmptyNameError
            return
        }

        viewModelScope.launch {
            val event = when (eventType) {
                EventType.ANNIVERSARY -> Event(name, date, withoutYear)
                EventType.BIRTHDAY -> Event(name, lastname, middleName, date, withoutYear)
            }
            event.calculateParameters()
            when (val result = repository.updateEvent(event)) {
                is CompleteResult.Error -> _state.value = EventDetailsState.Error(result.exception)
                CompleteResult.Complete -> _state.value = EventDetailsState.Saved
            }
        }
    }
}