package com.emikhalets.mydates.utils.navigation

import androidx.compose.runtime.Composable
import androidx.core.os.bundleOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.emikhalets.mydates.data.database.entities.Event
import com.emikhalets.mydates.ui.add_event.AddEventScreen
import com.emikhalets.mydates.ui.event_details.EventDetailsScreen
import com.emikhalets.mydates.ui.events_list.EventsListsScreen
import com.emikhalets.mydates.ui.settings.SettingsScreen
import com.emikhalets.mydates.utils.EventType
import com.emikhalets.mydates.utils.navigation.AppDestinations.ADD_EVENT
import com.emikhalets.mydates.utils.navigation.AppDestinations.EVENTS_LIST
import com.emikhalets.mydates.utils.navigation.AppDestinations.EVENT_DETAILS
import com.emikhalets.mydates.utils.navigation.AppDestinations.SETTINGS

private const val ARGS_EVENT = "argument_event"
private const val ARGS_EVENT_TYPE = "argument_event_type"

object AppDestinations {
    const val EVENTS_LIST = "events_list"
    const val ADD_EVENT = "add_event"
    const val EVENT_DETAILS = "event_details"
    const val SETTINGS = "settings"
}

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = EVENTS_LIST
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(EVENTS_LIST) {
            EventsListsScreen(
                navController = navController
            )
        }
        composable(ADD_EVENT) {
            navController.previousBackStackEntry?.arguments?.getSerializable(ARGS_EVENT_TYPE)?.let {
                AddEventScreen(
                    navController = navController,
                    eventType = it as EventType
                )
            }
//            navController.getArgParcelable<EventType>(ARGS_EVENT_TYPE) { eventType ->
//                AddEventScreen(
//                    navController = navController,
//                    eventType = eventType
//                )
//            }
            composable(EVENT_DETAILS) {
                navController.getArgParcelable<Event>(ARGS_EVENT) { event ->
                    EventDetailsScreen(
                        navController = navController,
                        event = event
                    )
                }
            }
            composable(SETTINGS) {
                SettingsScreen(
                    navController = navController
                )
            }
        }
    }
}

fun NavHostController.navigateToEventsList() {
    if (this.currentDestination?.route != EVENTS_LIST) navigate(EVENTS_LIST)
}

fun NavHostController.navigateToAddEvent(eventType: EventType) {
    navigate(ADD_EVENT, bundleOf(ARGS_EVENT_TYPE to eventType))
}

fun NavHostController.navigateToEventDetails(event: Event) {
    navigate(EVENT_DETAILS, bundleOf(ARGS_EVENT to event))
}

fun NavHostController.navigateToSettings() {
    navigate(SETTINGS)
}

fun NavHostController.navigateToBack() {
    navigateUp()
}