package com.emikhalets.mydates.ui.dates_list

import com.emikhalets.mydates.data.database.CompleteResult
import com.emikhalets.mydates.data.database.ListResult
import com.emikhalets.mydates.data.database.entities.DateItem
import com.emikhalets.mydates.data.repositories.RoomRepository
import com.emikhalets.mydates.mvi.MviViewModel
import com.emikhalets.mydates.utils.computeDaysLeftAndAge
import com.emikhalets.mydates.utils.groupDateItemList
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DatesListViewModel @Inject constructor(
    private val repository: RoomRepository
) : MviViewModel<DatesListIntent, DatesListAction, DatesListState>() {

    override fun intentToAction(intent: DatesListIntent): DatesListAction {
        return when (intent) {
            DatesListIntent.LoadDatesList -> DatesListAction.GetAllDates
            is DatesListIntent.ClickAddDateItem -> DatesListAction.AddDateItem(intent.dateItem)
            DatesListIntent.UpdateDatesList -> DatesListAction.UpdateDatesList
        }
    }

    override fun handleAction(action: DatesListAction) {
        launch {
            state.postValue(DatesListState.Loading)
            when (action) {
                DatesListAction.GetAllDates -> {
                    val result = repository.getAllDates()
                    state.postValue(result.reduce())
                }
                is DatesListAction.AddDateItem -> {
                    action.dateItem.computeDaysLeftAndAge()
                    val result = repository.insertDate(action.dateItem)
                    state.postValue(result.reduce())
                }
                DatesListAction.UpdateDatesList -> {
                    val result = repository.updateAllDates()
                    state.postValue(result.updateReduce())
                }
            }
        }
    }

    private fun ListResult<List<DateItem>>.reduce(): DatesListState {
        return when (this) {
            ListResult.EmptyList -> DatesListState.ResultEmptyList
            is ListResult.Success -> DatesListState.ResultDatesList(groupDateItemList(data))
            is ListResult.Error -> DatesListState.Error(message)
        }
    }

    private fun ListResult<List<DateItem>>.updateReduce(): DatesListState {
        return when (this) {
            ListResult.EmptyList -> DatesListState.ResultEmptyList
            is ListResult.Success -> DatesListState.ResultDateUpdated(groupDateItemList(data))
            is ListResult.Error -> DatesListState.Error(message)
        }
    }

    private fun CompleteResult<Nothing>.reduce(): DatesListState {
        return when (this) {
            CompleteResult.Complete -> DatesListState.ResultDateAdded
            is CompleteResult.Error -> DatesListState.Error(message)
        }
    }
}