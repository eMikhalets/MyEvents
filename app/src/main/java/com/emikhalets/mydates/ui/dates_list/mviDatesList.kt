package com.emikhalets.mydates.ui.dates_list

import com.emikhalets.mydates.data.database.entities.DateItem
import com.emikhalets.mydates.mvi.MviAction
import com.emikhalets.mydates.mvi.MviIntent
import com.emikhalets.mydates.mvi.MviState

sealed class DatesListIntent : MviIntent() {
    object LoadDatesList : DatesListIntent()
    object UpdateDatesList : DatesListIntent()
    data class ClickAddDateItem(val dateItem: DateItem) : DatesListIntent()
}

sealed class DatesListAction : MviAction() {
    object GetAllDates : DatesListAction()
    object UpdateDatesList : DatesListAction()
    data class AddDateItem(val dateItem: DateItem) : DatesListAction()
}

sealed class DatesListState : MviState() {
    object ResultDateAdded : DatesListState()
    object ResultEmptyList : DatesListState()
    data class ResultDateUpdated(val data: List<DateItem>) : DatesListState()
    data class ResultDatesList(val data: List<DateItem>) : DatesListState()
    data class Error(val message: String) : DatesListState()
}