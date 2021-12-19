package com.emikhalets.mydates.ui.add_event

sealed class AddEventState {
    object Init : AddEventState()
    object Added : AddEventState()
    object EmptyNameError : AddEventState()
    object ContactAlreadyAdded : AddEventState()
    data class ContactsChanged(val contacts: List<String>) : AddEventState()
    data class Error(val ex: Exception?, val message: String = ex?.message.toString()) : AddEventState()
}
