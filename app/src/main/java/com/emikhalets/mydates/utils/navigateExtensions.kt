package com.emikhalets.mydates.utils

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.emikhalets.mydates.data.database.entities.Event
import com.emikhalets.mydates.ui.events_list.EventsListFragmentDirections

fun Fragment.navigateBack() {
    findNavController().popBackStack()
}

fun Fragment.navigateEventsToAddAnniversary() {
    val action = EventsListFragmentDirections.actionEventsToAddAnniversary()
    findNavController().navigate(action)
}

fun Fragment.navigateEventsToAddBirthday() {
    val action = EventsListFragmentDirections.actionEventsToAddBirthday()
    findNavController().navigate(action)
}

fun Fragment.navigateEventsToAnniversaryDetails(event: Event) {
    val action = EventsListFragmentDirections.actionEventsToAnniversaryDetails(event)
    findNavController().navigate(action)
}

fun Fragment.navigateEventsToBirthdayDetails(event: Event) {
    val action = EventsListFragmentDirections.actionEventsToBirthdayDetails(event)
    findNavController().navigate(action)
}

fun Fragment.navigateEventsToSettings() {
    val action = EventsListFragmentDirections.actionEventsToSettings()
    findNavController().navigate(action)
}