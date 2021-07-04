package com.emikhalets.mydates.utils

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.emikhalets.mydates.data.database.entities.Event
import com.emikhalets.mydates.ui.events_list.EventsListFragmentDirections

fun Fragment.navigateBack() {
    findNavController().popBackStack()
}

fun Fragment.navigateEventsToAddEvent(eventType: EventType) {
    val action = EventsListFragmentDirections.actionEventsToAddBirthday(eventType)
    findNavController().navigate(action)
}

fun Fragment.navigateEventsToEventDetails(event: Event) {
    val action = EventsListFragmentDirections.actionEventsToEventDetails(event)
    findNavController().navigate(action)
}

fun Fragment.navigateEventsToSettings() {
    val action = EventsListFragmentDirections.actionEventsToSettings()
    findNavController().navigate(action)
}