package com.emikhalets.mydates.ui.events_list

import com.emikhalets.mydates.data.database.entities.Event

sealed class EventsListState {
    object Init : EventsListState()
    object Loading : EventsListState()
    object Empty : EventsListState()
    data class Events(val events: List<Event>) : EventsListState()
    data class Error(val ex: Exception?, val message: String = ex?.message.toString()) : EventsListState()
}