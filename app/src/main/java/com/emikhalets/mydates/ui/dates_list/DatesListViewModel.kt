package com.emikhalets.mydates.ui.dates_list

import com.emikhalets.mydates.data.database.ListResult
import com.emikhalets.mydates.data.database.entities.DateItem
import com.emikhalets.mydates.data.repositories.RoomRepository
import com.emikhalets.mydates.mvi.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DatesListViewModel @Inject constructor(
    private val repository: RoomRepository
) : MviViewModel<DatesListIntent, DatesListAction, DatesListState>() {

    override fun intentToAction(intent: DatesListIntent): DatesListAction {
        return when (intent) {
            DatesListIntent.LoadDatesList -> DatesListAction.GetAllDates
        }
    }

    override fun handleAction(action: DatesListAction) {
        launch {
            when (action) {
                DatesListAction.GetAllDates -> {
//                    val result = repository.getAllDates()
//                    state.postValue(result.reduce())
                    // TODO: delete after testing
                    val result = listOf(
                        DateItem("asdasdasd"),
                        DateItem("erteryeryery"),
                        DateItem("bnmbvnmnbm")
                    )
                    state.postValue(DatesListState.ResultDatesList(result))
                }
            }
        }
    }

    private fun ListResult<List<DateItem>>.reduce(): DatesListState {
        return when (this) {
            ListResult.EmptyList -> DatesListState.ResultEmptyList
            is ListResult.Success -> DatesListState.ResultDatesList(data)
            is ListResult.Error -> DatesListState.Error(message)
        }
    }
}