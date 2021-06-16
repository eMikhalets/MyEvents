package com.emikhalets.mydates.ui.calendar

import com.emikhalets.mydates.data.database.ListResult
import com.emikhalets.mydates.data.database.entities.Event
import com.emikhalets.mydates.data.repositories.RoomRepository
import com.emikhalets.mydates.mvi.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val repository: RoomRepository
) : MviViewModel<CalendarIntent, CalendarAction, CalendarState>() {

    override fun intentToAction(intent: CalendarIntent): CalendarAction {
        return when (intent) {
            CalendarIntent.LoadAllDates -> CalendarAction.GetAllDates
            is CalendarIntent.LoadDatesByDayMonth -> CalendarAction.GetDatesByDayMonth(
                intent.day,
                intent.month
            )
        }
    }

    override fun handleAction(action: CalendarAction) {
        launch {
            when (action) {
                CalendarAction.GetAllDates -> {
                    val result = repository.getAllEvents()
                    state.postValue(result.reduceAllDates())
                }
                is CalendarAction.GetDatesByDayMonth -> {
                    val result = repository.getItemsByDayMonth(action.day, action.month)
                    state.postValue(result.reduceDatesByDate())
                }
            }
        }
    }

    private fun ListResult<List<Event>>.reduceAllDates(): CalendarState {
        return when (this) {
            ListResult.EmptyList -> CalendarState.ResultEmptyList
            is ListResult.Success -> CalendarState.ResultAllDatesList(data)
            is ListResult.Error -> CalendarState.Error(message)
        }
    }

    private fun ListResult<List<Event>>.reduceDatesByDate(): CalendarState {
        return when (this) {
            ListResult.EmptyList -> CalendarState.ResultEmptyList
            is ListResult.Success -> CalendarState.ResultDatesByDate(data)
            is ListResult.Error -> CalendarState.Error(message)
        }
    }
}