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
import com.emikhalets.mydates.utils.navigation.AppDestinations.ADD_EVENT
import com.emikhalets.mydates.utils.navigation.AppDestinations.EVENTS_LIST
import com.emikhalets.mydates.utils.navigation.AppDestinations.EVENT_DETAILS
import com.emikhalets.mydates.utils.navigation.AppDestinations.SETTINGS

private const val ARGS_EVENT = "argument_event"

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
            AddEventScreen(
                navController = navController
            )
        }
        composable(EVENT_DETAILS) {
            navController.getArgument<Event>(ARGS_EVENT) { event ->
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

fun NavHostController.toEventsList() {
    if (this.currentDestination?.route != EVENTS_LIST) navigate(EVENTS_LIST)
}

fun NavHostController.toAddEvent() {
    navigate(ADD_EVENT)
}

fun NavHostController.toEventDetails(event: Event) {
    navigate(EVENT_DETAILS, bundleOf(ARGS_EVENT to event))
}

fun NavHostController.toSettings() {
    navigate(SETTINGS)
}

fun NavHostController.back() {
    navigateUp()
}