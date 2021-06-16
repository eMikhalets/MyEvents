package com.emikhalets.mydates.ui.events_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emikhalets.mydates.data.database.CompleteResult
import com.emikhalets.mydates.data.database.ListResult
import com.emikhalets.mydates.data.database.entities.Event
import com.emikhalets.mydates.data.repositories.RoomRepository
import com.emikhalets.mydates.utils.calculateParameters
import com.emikhalets.mydates.utils.groupDateItemList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventsListViewModel @Inject constructor(
    private val repository: RoomRepository
) : ViewModel() {

    private val _events = MutableLiveData<List<Event>>()
    val events get(): LiveData<List<Event>> = _events

    private val _eventAdd = MutableLiveData<Event>()
    val eventAdd get(): LiveData<Event> = _eventAdd

    private val _error = MutableLiveData<String>()
    val error get(): LiveData<String> = _error

    fun loadAllEvents() {
        viewModelScope.launch {
            when (val result = repository.getAllEvents()) {
                ListResult.EmptyList -> _error.postValue("Empty events list")
                is ListResult.Error -> _error.postValue(result.message)
                is ListResult.Success -> _events.postValue(result.data)
            }

        }
    }


    override fun intentToAction(intent: DatesListIntent): DatesListAction {
        return when (intent) {
            DatesListIntent.LoadDatesList -> DatesListAction.GetAllDates
            is DatesListIntent.ClickAddDateItem -> DatesListAction.AddDateItem(intent.event)
            DatesListIntent.UpdateDatesList -> DatesListAction.UpdateDatesList
        }
    }

    override fun handleAction(action: DatesListAction) {
        launch {
            state.postValue(DatesListState.Loading)
            when (action) {
                DatesListAction.GetAllDates -> {
                    val result = repository.getAllEvents()
                    state.postValue(result.reduce())
                }
                is DatesListAction.AddDateItem -> {
                    action.event.calculateParameters()
                    val result = repository.insertEvent(action.event)
                    state.postValue(result.reduce())
                }
                DatesListAction.UpdateDatesList -> {
                    val result = repository.updateEvents()
                    state.postValue(result.updateReduce())
                }
            }
        }
    }

    private fun ListResult<List<Event>>.reduceEvents(): DatesListState {
        return when (this) {
            ListResult.EmptyList -> DatesListState.ResultEmptyList
            is ListResult.Success -> DatesListState.ResultDatesList(groupDateItemList(data))
            is ListResult.Error -> DatesListState.Error(message)
        }
    }

    private fun ListResult<List<Event>>.updateReduce(): DatesListState {
        return when (this) {
            ListResult.EmptyList -> DatesListState.ResultEmptyList
            is ListResult.Success -> DatesListState.ResultDateUpdated(groupDateItemList(data))
            is ListResult.Error -> DatesListState.Error(message)
        }
    }

    private fun CompleteResult<Nothing>.reduce(): DatesListState {
        return when (this) {
            CompleteResult.Complete -> DatesListState.ResultDateAdded
            is CompleteResult.Error -> DatesListState.Error(message)
        }
    }
}