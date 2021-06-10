package com.emikhalets.mydates.ui.date_details

import com.emikhalets.mydates.data.database.CompleteResult
import com.emikhalets.mydates.data.repositories.RoomRepository
import com.emikhalets.mydates.mvi.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DateDetailsViewModel @Inject constructor(
    private val repository: RoomRepository
) : MviViewModel<DateDetailsIntent, DateDetailsAction, DateDetailsState>() {

    override fun intentToAction(intent: DateDetailsIntent): DateDetailsAction {
        return when (intent) {
            is DateDetailsIntent.ClickDeleteDateItem -> DateDetailsAction.DeleteDateItem(intent.dateItem)
            is DateDetailsIntent.ClickSaveDateItem -> DateDetailsAction.UpdateDateItem(intent.dateItem)
        }
    }

    override fun handleAction(action: DateDetailsAction) {
        launch {
            when (action) {
                is DateDetailsAction.DeleteDateItem -> {
                    val result = repository.deleteDate(action.dateItem)
                    state.postValue(result.reduceDeleting())
                }
                is DateDetailsAction.UpdateDateItem -> {
                    val result = repository.updateDate(action.dateItem)
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