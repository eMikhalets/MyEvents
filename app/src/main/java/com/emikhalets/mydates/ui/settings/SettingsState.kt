package com.emikhalets.mydates.ui.settings

sealed class SettingsState {
    object Init : SettingsState()
    object Loading : SettingsState()
    object Exported : SettingsState()
    object Imported : SettingsState()
    object ExportingError : SettingsState()
    object ImportingError : SettingsState()
    data class Error(
        val ex: Exception?,
        val message: String = ex?.message.toString()
    ) : SettingsState()
}
