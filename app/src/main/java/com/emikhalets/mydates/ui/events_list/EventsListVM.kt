package com.emikhalets.mydates.ui.events_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emikhalets.mydates.data.database.ListResult
import com.emikhalets.mydates.data.repositories.DatabaseRepository
import com.emikhalets.mydates.utils.sortWithDividers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class EventsListVM @Inject constructor(
    private val repository: DatabaseRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<EventsListState>(EventsListState.Init)
    val state: StateFlow<EventsListState> = _state

    fun loadAllEvents(lastUpdate: Long = Date().time) {
        _state.value = EventsListState.Init
        viewModelScope.launch {
            _state.value = EventsListState.Loading
            when (val result = repository.getAllEvents(lastUpdate)) {
                ListResult.EmptyList -> _state.value = EventsListState.EmptyEvents
                is ListResult.Error -> _state.value = EventsListState.Error(result.exception)
                is ListResult.Success -> {
                    _state.value = EventsListState.Events(sortWithDividers(result.data))
                }
            }
        }
    }
}