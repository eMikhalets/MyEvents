package com.emikhalets.mydates.utils

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.emikhalets.mydates.data.database.entities.Event
import com.emikhalets.mydates.ui.events_list.EventsListFragmentDirections

fun Fragment.navigateBack() {
    findNavController().popBackStack()
}

fun Fragment.navigateToAddAnniversary() {
    val action = EventsListFragmentDirections.actionEventsToAddAnniversary()
    findNavController().navigate(action)
}

fun Fragment.navigateToAddBirthday() {
    val action = EventsListFragmentDirections.actionEventsToAddBirthday()
    findNavController().navigate(action)
}

fun Fragment.navigateToAnniversaryDetails(event: Event) {
    val action = EventsListFragmentDirections.actionEventsToAnniversaryDetails(event)
    findNavController().navigate(action)
}

fun Fragment.navigateToBirthdayDetails(event: Event) {
    val action = EventsListFragmentDirections.actionEventsToBirthdayDetails(event)
    findNavController().navigate(action)
}