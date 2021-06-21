package com.emikhalets.mydates.ui.settings

import androidx.lifecycle.ViewModel
import com.emikhalets.mydates.data.repositories.RoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsVM @Inject constructor(
    private val repository: RoomRepository
) : ViewModel() {
}