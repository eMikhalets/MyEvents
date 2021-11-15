package com.emikhalets.mydates.ui.add_event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emikhalets.mydates.data.database.CompleteResult
import com.emikhalets.mydates.data.database.entities.Event
import com.emikhalets.mydates.data.repositories.DatabaseRepository
import com.emikhalets.mydates.utils.enums.EventType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class AddEventVM @Inject constructor(
    private val repository: DatabaseRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<AddEventState>(AddEventState.Init)
    val state: StateFlow<AddEventState> = _state

    var date = Date().time

    fun saveNewEvent(
        eventType: EventType,
        name: String,
        lastname: String,
        middleName: String,
        withoutYear: Boolean,
    ) {
        _state.value = AddEventState.Init
        if (name.isEmpty()) {
            _state.value = AddEventState.EmptyNameError
            return
        }

        viewModelScope.launch {
            val event = when (eventType) {
                EventType.ANNIVERSARY -> Event(name, date, withoutYear)
                EventType.BIRTHDAY -> Event(name, lastname, middleName, date, withoutYear)
            }
            event.calculateParameters()
            when (val result = repository.insertEvent(event)) {
                CompleteResult.Complete -> _state.value = AddEventState.Added
                is CompleteResult.Error -> AddEventState.Error(result.exception)
            }
        }
    }
}