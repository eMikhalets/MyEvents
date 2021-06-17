package com.emikhalets.mydates.ui.event_details

import com.emikhalets.mydates.data.database.entities.Event
import com.emikhalets.mydates.mvi.MviAction
import com.emikhalets.mydates.mvi.MviIntent
import com.emikhalets.mydates.mvi.MviState

sealed class DateDetailsIntent : MviIntent() {
    data class ClickSaveDateItem(val event: Event) : DateDetailsIntent()
    data class ClickDeleteDateItem(val event: Event) : DateDetailsIntent()
}

sealed class DateDetailsAction : MviAction() {
    data class UpdateDateItem(val event: Event) : DateDetailsAction()
    data class DeleteDateItem(val event: Event) : DateDetailsAction()
}

sealed class DateDetailsState : MviState() {
    object ResultDateSaved : DateDetailsState()
    object ResultDateDeleted : DateDetailsState()
    data class Error(val message: String) : DateDetailsState()
}