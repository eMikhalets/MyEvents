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
        event: Event
    ) {
        _state.value = EventDetailsState.Init
        if (event.name.isEmpty()) {
            _state.value = EventDetailsState.EmptyNameError
            return
        }

        viewModelScope.launch {
            event.calculateParameters()
            when (val result = repository.updateEvent(event)) {
                is CompleteResult.Error -> _state.value = EventDetailsState.Error(result.exception)
                CompleteResult.Complete -> _state.value = EventDetailsState.Saved
            }
        }
    }
}