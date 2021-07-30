package com.emikhalets.mydates.utils

import androidx.fragment.app.Fragment
import com.emikhalets.mydates.data.database.entities.Event

fun Fragment.navigateBack() {
//    findNavController().popBackStack()
}

fun Fragment.navigateEventsToAddEvent(eventType: EventType) {
//    val action = EventsListFragmentDirections.actionEventsToAddEvent(eventType)
//    findNavController().navigate(action)
}

fun Fragment.navigateEventsToEventDetails(event: Event) {
//    val action = EventsListFragmentDirections.actionEventsToEventDetails(event)
//    findNavController().navigate(action)
}

fun Fragment.navigateEventsToSettings() {
//    val action = EventsListFragmentDirections.actionEventsToSettings()
//    findNavController().navigate(action)
}

fun Fragment.navigateCalendarToEvent(event: Event) {
//    val action = CalendarFragmentDirections.actionCalendarToEventDetails(event)
//    findNavController().navigate(action)
}