package com.emikhalets.mydates.ui.add_event

import androidx.lifecycle.ViewModel
import com.emikhalets.mydates.data.database.CompleteResult
import com.emikhalets.mydates.data.repositories.RoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddBirthdayVM @Inject constructor(
    private val repository: RoomRepository
) : ViewModel() {

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
                    val result = repository.deleteEvent(action.dateItem)
                    state.postValue(result.reduceDeleting())
                }
                is DateDetailsAction.UpdateDateItem -> {
                    action.dateItem.computeDaysLeftAndAge()
                    val result = repository.updateEvent(action.dateItem)
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