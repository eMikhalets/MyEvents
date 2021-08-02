package com.emikhalets.mydates.ui.add_event

sealed class AddEventState {
    object Init : AddEventState()
    object Added : AddEventState()
    object Loading : AddEventState()
    object EmptyName : AddEventState()
    data class Error(val ex: Exception?, val message: String = ex?.message.toString()) : AddEventState()
}
