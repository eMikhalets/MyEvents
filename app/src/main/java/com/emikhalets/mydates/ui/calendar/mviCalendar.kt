package com.emikhalets.mydates.ui.calendar

import com.emikhalets.mydates.data.database.entities.DateItem
import com.emikhalets.mydates.mvi.MviAction
import com.emikhalets.mydates.mvi.MviIntent
import com.emikhalets.mydates.mvi.MviState

sealed class CalendarIntent : MviIntent() {
    object LoadAllDates : CalendarIntent()
    data class LoadDatesByDayMonth(val day: Int, val month: Int) : CalendarIntent()
}

sealed class CalendarAction : MviAction() {
    object GetAllDates : CalendarAction()
    data class GetDatesByDayMonth(val day: Int, val month: Int) : CalendarAction()
}

sealed class CalendarState : MviState() {
    object ResultEmptyList : CalendarState()
    data class ResultDatesByDate(val data: List<DateItem>) : CalendarState()
    data class ResultAllDatesList(val data: List<DateItem>) : CalendarState()
    data class Error(val message: String) : CalendarState()
}