package com.emikhalets.mydates.utils

import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.emikhalets.mydates.data.database.entities.Event
import com.emikhalets.mydates.ui.calendar.CalendarFragmentDirections
import com.emikhalets.mydates.ui.events_list.EventsListFragmentDirections

fun Fragment.navigateBack() {
    findNavController().popBackStack()
}

fun View.navigateEventsToAddEvent(eventType: EventType) {
    val action = EventsListFragmentDirections.actionEventsToAddEvent(eventType)
    findNavController(this).navigate(action)
}

fun Fragment.navigateEventsToEventDetails(event: Event) {
    val action = EventsListFragmentDirections.actionEventsToEventDetails(event)
    findNavController().navigate(action)
}

fun Fragment.navigateEventsToSettings() {
    val action = EventsListFragmentDirections.actionEventsToSettings()
    findNavController().navigate(action)
}

fun Fragment.navigateCalendarToEvent(event: Event) {
    val action = CalendarFragmentDirections.actionCalendarToEventDetails(event)
    findNavController().navigate(action)
}