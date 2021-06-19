package com.emikhalets.mydates.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
class SettingsVM @Inject constructor(
    private val repository: RoomRepository
) : ViewModel() {

    private val _eventAdd = MutableLiveData<Boolean>()
    val eventAdd get(): LiveData<Boolean> = _eventAdd

    private val _error = MutableLiveData<String>()
    val error get(): LiveData<String> = _error

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
            event.calculateParameters()
            when (val result = repository.insertEvent(event)) {
                CompleteResult.Complete -> _eventAdd.postValue(true)
                is CompleteResult.Error -> _error.postValue(result.message)
            }
        }
    }
}