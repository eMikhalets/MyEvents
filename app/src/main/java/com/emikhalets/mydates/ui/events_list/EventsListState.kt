package com.emikhalets.mydates.ui.events_list

import com.emikhalets.mydates.data.database.entities.Event
import com.emikhalets.mydates.ui.common.AppState

sealed class EventsListState : AppState() {
    object Init : EventsListState()
    object EmptyAllEvents : EventsListState()
    object EmptyDayEvents : EventsListState()
    data class DayEvents(val events: List<Event>) : EventsListState()
    data class AllEvents(val events: List<Event>) : EventsListState()
    data class Error(val ex: Exception?, val message: String = ex?.message.toString()) : EventsListState()
}
