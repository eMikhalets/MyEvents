package com.emikhalets.mydates.ui.add_event

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    private val _state = MutableLiveData<AddEventState>(AddEventState.Init)
    val state: LiveData<AddEventState> = _state

    private val contacts = mutableListOf<String>()
    var date = Date().time

    fun saveNewEvent(
        eventType: EventType,
        name: String,
        lastname: String,
        middleName: String,
        withoutYear: Boolean,
        imageUri: String
    ) {
        _state.value = AddEventState.Init
        if (name.isEmpty()) {
            _state.value = AddEventState.EmptyNameError
            return
        }

        viewModelScope.launch {
            val event = when (eventType) {
                EventType.ANNIVERSARY -> Event(name, date, withoutYear, imageUri, contacts)
                EventType.BIRTHDAY -> Event(name, lastname, middleName, date, withoutYear, imageUri, contacts)
            }
            event.calculateParameters()
            when (val result = repository.insertEvent(event)) {
                CompleteResult.Complete -> _state.value = AddEventState.Added
                is CompleteResult.Error -> AddEventState.Error(result.exception)
            }
        }
    }

    fun addContact(contact: String) {
        viewModelScope.launch {
            if (contacts.contains(contact)) {
                _state.value = AddEventState.ContactAlreadyAdded
            } else {
                contacts.add(contact)
                _state.value = AddEventState.ContactsChanged(contacts)
            }
        }
    }

    fun removeContact(contact: String) {
        viewModelScope.launch {
            contacts.remove(contact)
            _state.value = AddEventState.ContactsChanged(contacts)
        }
    }
}