package com.emikhalets.mydates.ui.event_details

import com.emikhalets.mydates.data.database.CompleteResult
import com.emikhalets.mydates.data.repositories.RoomRepository
import com.emikhalets.mydates.mvi.MviViewModel
import com.emikhalets.mydates.utils.calculateParameters
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DateDetailsViewModel @Inject constructor(
    private val repository: RoomRepository
) : MviViewModel<DateDetailsIntent, DateDetailsAction, DateDetailsState>() {

    override fun intentToAction(intent: DateDetailsIntent): DateDetailsAction {
        return when (intent) {
            is DateDetailsIntent.ClickDeleteDateItem -> DateDetailsAction.DeleteDateItem(intent.event)
            is DateDetailsIntent.ClickSaveDateItem -> DateDetailsAction.UpdateDateItem(intent.event)
        }
    }

    override fun handleAction(action: DateDetailsAction) {
        launch {
            when (action) {
                is DateDetailsAction.DeleteDateItem -> {
                    val result = repository.deleteEvent(action.event)
                    state.postValue(result.reduceDeleting())
                }
                is DateDetailsAction.UpdateDateItem -> {
                    action.event.calculateParameters()
                    val result = repository.updateEvent(action.event)
                    state.postValue(result.reduceUpdating())
                }
            }
        }
    }

    private fun CompleteResult<Nothing>.reduceDeleting(): DateDetailsState {
        return when (this) {
            CompleteResult.Complete -> DateDetailsState.ResultDateDeleted
            is CompleteResult.Error -> DateDetailsState.Error(message)
        }
    }

    private fun CompleteResult<Nothing>.reduceUpdating(): DateDetailsState {
        return when (this) {
            CompleteResult.Complete -> DateDetailsState.ResultDateSaved
            is CompleteResult.Error -> DateDetailsState.Error(message)
        }
    }
}