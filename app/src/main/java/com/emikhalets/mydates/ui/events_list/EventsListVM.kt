package com.emikhalets.mydates.ui.events_list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emikhalets.mydates.data.database.ListResult
import com.emikhalets.mydates.data.repositories.RoomRepository
import com.emikhalets.mydates.utils.sortWithDividers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventsListVM @Inject constructor(
    private val repository: RoomRepository
) : ViewModel() {

    var state by mutableStateOf<EventsListState>(EventsListState.Init)
        private set

    fun loadAllEvents(lastUpdate: Long) {
        viewModelScope.launch {
            state = EventsListState.Loading
            state = when (val result = repository.getAllEvents(lastUpdate)) {
                ListResult.EmptyList -> EventsListState.Empty
                is ListResult.Error -> EventsListState.Error(result.exception)
                is ListResult.Success -> EventsListState.Events(sortWithDividers(result.data))
            }
        }
    }
}