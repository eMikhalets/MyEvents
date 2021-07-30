package com.emikhalets.mydates.ui.events_list

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.emikhalets.mydates.R
import com.emikhalets.mydates.data.database.entities.Event
import com.emikhalets.mydates.ui.app_components.AppBottomBar
import com.emikhalets.mydates.ui.app_components.AppTopBar
import com.emikhalets.mydates.ui.app_components.EventListItem
import com.emikhalets.mydates.ui.theme.AppTheme
import com.emikhalets.mydates.utils.buildEventsList
import com.emikhalets.mydates.utils.navigation.NavActions
import java.util.*

@Composable
fun EventsListsScreen(
    navController: NavHostController,
    viewModel: EventsListVM = hiltViewModel()
) {
    val state = remember { viewModel.state }
    if (state == EventsListState.Init) viewModel.loadAllEvents(Date().time)

    EventsListsScreen(
        navController = navController,
        state = state
    )
}

@Composable
private fun EventsListsScreen(
    navController: NavHostController,
    state: EventsListState
) {
    Scaffold(
        backgroundColor = MaterialTheme.colors.surface,
        topBar = {
            AppTopBar(
                navController = navController,
                title = stringResource(R.string.title_events_list),
                showBackIcon = false,
                showSettingsIcon = true
            )
        },
        bottomBar = {
            AppBottomBar(navController = navController)
        }
    ) {
        when (state) {
            is EventsListState.Error -> {
            }
            is EventsListState.Events -> {
                EventsList(navController, state.events)
            }
            EventsListState.Empty -> {
                EmptyEvents()
            }
            EventsListState.Init -> {
            }
        }
    }
}

@Composable
private fun EmptyEvents() {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.events_list_empty),
            style = MaterialTheme.typography.h4,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
private fun EventsList(
    navController: NavHostController,
    events: List<Event>
) {
    LazyColumn(modifier = Modifier.padding(start = 8.dp, end = 8.dp)) {
        items(events) { event ->
            when (event.eventType) {
                0 -> EventDivider(
                    month = stringArrayResource(R.array.months)[event.age]
                )
                else -> EventListItem(
                    event = event,
                    onClick = NavActions(navController).toEventDetails
                )
            }

        }
    }
}

@Composable
private fun EventDivider(
    month: String
) {
    Text(
        text = month,
        style = MaterialTheme.typography.h4
    )
}

@Preview(showBackground = true)
@Composable
private fun EventsListsScreenPreview() {
    AppTheme {
        EventsListsScreen(
            navController = rememberNavController(),
            state = EventsListState.Events(buildEventsList())
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, backgroundColor = 1)
@Composable
private fun EventsListsScreenDarkPreview() {
    AppTheme {
        EventsListsScreen(
            navController = rememberNavController(),
            state = EventsListState.Events(buildEventsList())
        )
    }
}