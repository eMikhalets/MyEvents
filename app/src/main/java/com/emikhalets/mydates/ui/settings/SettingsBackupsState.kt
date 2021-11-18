package com.emikhalets.mydates.ui.settings

sealed class SettingsBackupsState {
    object Init : SettingsBackupsState()
    object Loading : SettingsBackupsState()
    object Exported : SettingsBackupsState()
    object Imported : SettingsBackupsState()
    object ExportingError : SettingsBackupsState()
    object ImportingError : SettingsBackupsState()
    data class Error(
        val ex: Exception?,
        val message: String = ex?.message.toString()
    ) : SettingsBackupsState()
}
