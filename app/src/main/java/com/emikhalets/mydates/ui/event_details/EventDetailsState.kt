package com.emikhalets.mydates.ui.event_details

sealed class EventDetailsState {
    object Init : EventDetailsState()
    object Saved : EventDetailsState()
    object Deleted : EventDetailsState()
    object EmptyNameError : EventDetailsState()
    object ContactAlreadyAdded : EventDetailsState()
    data class ContactsChanged(val contacts: List<String>) : EventDetailsState()
    data class Error(val ex: Exception?, val message: String = ex?.message.toString()) : EventDetailsState()
}
