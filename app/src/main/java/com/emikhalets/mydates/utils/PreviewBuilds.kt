package com.emikhalets.mydates.utils

import androidx.compose.runtime.Composable
import com.emikhalets.mydates.data.database.entities.Event

@Composable
fun buildEvent(): Event {
    return Event(
        id = 0,
        name = "Иван",
        lastName = "Иванов",
        middleName = "Иванович",
        date = 1627551042000,
        daysLeft = 36,
        age = 60,
        eventType = 2,
        group = "",
        notes = "text text text text text text text text text text text text text",
        withoutYear = false
    )
}

@Composable
fun buildEventsList(): List<Event> {
    val list = mutableListOf<Event>()
    repeat(10) { list.add(buildEvent()) }
    return list
}