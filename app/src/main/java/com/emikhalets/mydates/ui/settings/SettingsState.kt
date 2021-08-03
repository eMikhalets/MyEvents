package com.emikhalets.mydates.ui.settings

import com.emikhalets.mydates.data.database.entities.Event

sealed class SettingsState {
    object Init : SettingsState()
    object Loading : SettingsState()
    object EmptyEvents : SettingsState()
    data class Events(val events: List<Event>) : SettingsState()
    data class Error(val ex: Exception?, val message: String = ex?.message.toString()) : SettingsState()
}
