package com.emikhalets.mydates.ui.event_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emikhalets.mydates.data.database.CompleteResult
import com.emikhalets.mydates.data.database.entities.Event
import com.emikhalets.mydates.data.repositories.DatabaseRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class EventDetailsVM @Inject constructor(
    private val repository: DatabaseRepository,
) : ViewModel() {

    private val _state = MutableLiveData<EventDetailsState>(EventDetailsState.Init)
    val state: LiveData<EventDetailsState> = _state

    val contacts = mutableListOf<String>()

    fun deleteEvent(event: Event) {
        viewModelScope.launch {
            when (val result = repository.deleteEvent(event)) {
                is CompleteResult.Error -> _state.postValue(EventDetailsState.Error(result.exception))
                CompleteResult.Complete -> _state.postValue(EventDetailsState.Deleted)
            }
        }
    }

    fun updateEvent(event: Event) {
        _state.value = EventDetailsState.Init
        if (event.name.isEmpty()) {
            _state.postValue(EventDetailsState.EmptyNameError)
            return
        }

        viewModelScope.launch {
            event.calculateParameters()
            when (val result = repository.updateEvent(event)) {
                is CompleteResult.Error -> _state.postValue(EventDetailsState.Error(result.exception))
                CompleteResult.Complete -> _state.postValue(EventDetailsState.Saved)
            }
        }
    }

    fun addContact(contact: String) {
        viewModelScope.launch {
            if (contacts.contains(contact)) {
                _state.postValue(EventDetailsState.ContactAlreadyAdded)
            } else {
                contacts.add(contact)
                _state.postValue(EventDetailsState.ContactsChanged(contacts))
            }
        }
    }

    fun removeContact(contact: String) {
        viewModelScope.launch {
            contacts.remove(contact)
            _state.postValue(EventDetailsState.ContactsChanged(contacts))
        }
    }
}