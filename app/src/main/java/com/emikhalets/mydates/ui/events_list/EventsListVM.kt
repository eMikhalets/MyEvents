package com.emikhalets.mydates.ui.events_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emikhalets.mydates.data.database.ListResult
import com.emikhalets.mydates.data.database.entities.Event
import com.emikhalets.mydates.data.repositories.DatabaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    private suspend fun sortWithDividers(events: List<Event>): List<Event> {
        return withContext(Dispatchers.IO) {
            val sorted = events.sortedBy { it.daysLeft }
            val result = mutableListOf<Event>()

            var insert = 0
            result.add(insert++, Event(sorted.first().monthNumber()))
            for (i in 0..(sorted.size - 2)) {
                result.add(insert++, sorted[i])
                val current = sorted[i].monthNumber()
                val next = sorted[i + 1].monthNumber()
                if (current != next) result.add(insert++, Event(next))
            }
            result.add(insert, sorted.last())

            result
        }
    }
}