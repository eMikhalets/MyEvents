package com.emikhalets.mydates.ui.events_list

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.emikhalets.mydates.R
import com.emikhalets.mydates.data.database.entities.Event
import com.emikhalets.mydates.ui.app_components.AppLoader
import com.emikhalets.mydates.ui.app_components.AppScaffold
import com.emikhalets.mydates.ui.app_components.EventListItem
import com.emikhalets.mydates.ui.theme.AppTheme
import com.emikhalets.mydates.utils.buildEventsList
import com.emikhalets.mydates.utils.navigation.toEventDetails

@Composable
fun EventsListsScreen(
    navController: NavHostController,
    viewModel: EventsListVM = hiltViewModel()
) {
    if (viewModel.state == EventsListState.Init) viewModel.loadAllEvents(LocalContext.current)

    EventsListsScreen(
        navController = navController,
        state = viewModel.state
    )
}

@Composable
private fun EventsListsScreen(
    navController: NavHostController,
    state: EventsListState
) {
    AppScaffold(
        navController = navController,
        topBarTitle = stringResource(R.string.title_events_list),
        showBackButton = false,
        showSettingsButton = true,
        showBottomBar = true
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
            EventsListState.Loading -> {
                AppLoader()
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
    LazyColumn(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp)
            .fillMaxSize()
    ) {
        items(events) { event ->
            Column {
                when (event.eventType) {
                    0 -> EventDivider(
                        month = stringArrayResource(R.array.months)[event.age]
                    )
                    else -> EventListItem(
                        event = event,
                        onClick = { navController.toEventDetails(it) }
                    )
                }
                if (event.id == events.last().id) Spacer(modifier = Modifier.size(8.dp))
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
        style = MaterialTheme.typography.h5,
        modifier = Modifier.padding(start = 16.dp)
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