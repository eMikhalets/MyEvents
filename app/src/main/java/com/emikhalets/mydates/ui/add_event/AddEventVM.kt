package com.emikhalets.mydates.ui.add_event

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emikhalets.mydates.data.database.CompleteResult
import com.emikhalets.mydates.data.database.entities.Event
import com.emikhalets.mydates.data.repositories.RoomRepository
import com.emikhalets.mydates.utils.calculateParameters
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEventVM @Inject constructor(
    private val repository: RoomRepository
) : ViewModel() {

    var state by mutableStateOf<AddEventState>(AddEventState.Init)
        private set

    fun addNewAnniversary(name: String, date: Long, withoutYear: Boolean) {
        addNewEvent(Event(name, date, withoutYear))
    }

    fun addNewBirthday(
        name: String,
        lastname: String,
        middleName: String,
        date: Long,
        withoutYear: Boolean
    ) {
        addNewEvent(Event(name, lastname, middleName, date, withoutYear))
    }

    private fun addNewEvent(event: Event) {
        viewModelScope.launch {
            state = AddEventState.Loading
            event.calculateParameters()
            state = when (val result = repository.insertEvent(event)) {
                CompleteResult.Complete -> AddEventState.Saved
                is CompleteResult.Error -> AddEventState.Error(result.exception)
            }
        }
    }
}