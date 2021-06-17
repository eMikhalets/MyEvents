package com.emikhalets.mydates.utils

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.emikhalets.mydates.ui.events_list.EventsListFragmentDirections

fun Fragment.navigateToAddAnniversary() {
    val action = EventsListFragmentDirections.actionEventsToAddAnniversary()
    findNavController().navigate(action)
}

fun Fragment.navigateToAddBirthday() {
    val action = EventsListFragmentDirections.actionEventsToAddBirthday()
    findNavController().navigate(action)
}

fun Fragment.navigateToAnniversaryDetails() {
    val action = EventsListFragmentDirections.actionEventsToAnniversaryDetails()
    findNavController().navigate(action)
}

fun Fragment.navigateToBirthdayDetails() {
    val action = EventsListFragmentDirections.actionEventsToBirthdayDetails()
    findNavController().navigate(action)
}