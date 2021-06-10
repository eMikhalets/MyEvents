package com.emikhalets.mydates.ui.date_details

import com.emikhalets.mydates.data.database.entities.DateItem
import com.emikhalets.mydates.mvi.MviAction
import com.emikhalets.mydates.mvi.MviIntent
import com.emikhalets.mydates.mvi.MviState

sealed class DateDetailsIntent : MviIntent() {
    data class ClickSaveDateItem(val dateItem: DateItem) : DateDetailsIntent()
    data class ClickDeleteDateItem(val dateItem: DateItem) : DateDetailsIntent()
}

sealed class DateDetailsAction : MviAction() {
    data class UpdateDateItem(val dateItem: DateItem) : DateDetailsAction()
    data class DeleteDateItem(val dateItem: DateItem) : DateDetailsAction()
}

sealed class DateDetailsState : MviState() {
    object ResultDateSaved : DateDetailsState()
    object ResultDateDeleted : DateDetailsState()
    data class Error(val message: String) : DateDetailsState()
}