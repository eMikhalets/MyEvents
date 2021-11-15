package com.emikhalets.mydates.utils

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.emikhalets.mydates.data.database.entities.Event
import com.emikhalets.mydates.ui.calendar.CalendarFragment
import com.emikhalets.mydates.ui.calendar.CalendarFragmentDirections
import com.emikhalets.mydates.ui.events_list.EventsListFragment
import com.emikhalets.mydates.ui.events_list.EventsListFragmentDirections
import com.emikhalets.mydates.utils.enums.EventType

object AppNavigationManager {

    fun back(fragment: Fragment) {
        fragment.findNavController().popBackStack()
    }

    fun toAddEvent(navController: NavController, eventType: EventType) {
        val action = EventsListFragmentDirections.actionEventsToAddEvent(eventType)
        navController.navigate(action)
    }

    fun toEventDetails(fragment: EventsListFragment, event: Event) {
        val action = EventsListFragmentDirections.actionEventsToEventDetails(event)
        fragment.findNavController().navigate(action)
    }

    fun toSettings(fragment: EventsListFragment) {
        val action = EventsListFragmentDirections.actionEventsToSettings()
        fragment.findNavController().navigate(action)
    }

    fun toEventDetails(fragment: CalendarFragment, event: Event) {
        val action = CalendarFragmentDirections.actionCalendarToEventDetails(event)
        fragment.findNavController().navigate(action)
    }

    fun toSettings(fragment: CalendarFragment) {
        val action = CalendarFragmentDirections.actionCalendarToSettings()
        fragment.findNavController().navigate(action)
    }
}