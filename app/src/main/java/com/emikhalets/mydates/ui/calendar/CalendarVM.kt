package com.emikhalets.mydates.ui.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emikhalets.mydates.data.database.ListResult
import com.emikhalets.mydates.data.repositories.DatabaseRepository
import com.emikhalets.mydates.utils.dayOfYear
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class CalendarVM @Inject constructor(
    private val repository: DatabaseRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<CalendarState>(CalendarState.Init)
    val state: StateFlow<CalendarState> = _state

    var selectedDate = Calendar.getInstance()

    fun loadAllEvents() {
        _state.value = CalendarState.Init
        viewModelScope.launch {
            when (val result = repository.getAllEvents()) {
                ListResult.EmptyList -> _state.value = CalendarState.EmptyAllEvents
                is ListResult.Error -> _state.value = CalendarState.Error(result.exception)
                is ListResult.Success -> _state.value = CalendarState.AllEvents(result.data)
            }
        }
    }

    fun getDayEvents(date: Calendar) {
        _state.value = CalendarState.Init
        viewModelScope.launch {
            val now = Calendar.getInstance()
            var daysLeft = date.dayOfYear() - now.dayOfYear()
            if (daysLeft < 0) daysLeft += now.getActualMaximum(Calendar.DAY_OF_YEAR)
            when (val result = repository.getAllByDaysLeft(daysLeft)) {
                ListResult.EmptyList -> _state.value = CalendarState.EmptyDayEvents
                is ListResult.Error -> _state.value = CalendarState.Error(result.exception)
                is ListResult.Success -> _state.value = CalendarState.DayEvents(result.data)
            }
        }
    }
}