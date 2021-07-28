package com.emikhalets.mydates.ui.calendar

import com.emikhalets.mydates.data.database.entities.Event

sealed class CalendarState {
    object Init : CalendarState()
    object EmptyAllEvents : CalendarState()
    object EmptyDayEvents : CalendarState()
    data class DayEvents(val events: List<Event>) : CalendarState()
    data class AllEvents(val events: List<Event>) : CalendarState()
    data class Error(val ex: Exception?, val message: String = ex?.message.toString()) : CalendarState()
}
