package com.emikhalets.mydates.ui.dates_list

import com.emikhalets.mydates.data.database.entities.DateItem
import com.emikhalets.mydates.mvi.MviAction
import com.emikhalets.mydates.mvi.MviIntent
import com.emikhalets.mydates.mvi.MviState

sealed class DatesListIntent : MviIntent() {
    object LoadDatesList : DatesListIntent()
}

sealed class DatesListAction : MviAction() {
    object GetAllDates : DatesListAction()
}

sealed class DatesListState : MviState() {
    object ResultEmptyList : DatesListState()
    data class ResultDatesList(val data: List<DateItem>) : DatesListState()
    data class Error(val message: String) : DatesListState()
}