package com.emikhalets.mydates.ui.events_list

import android.content.res.Configuration
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emikhalets.mydates.R
import com.emikhalets.mydates.data.database.entities.Event
import com.emikhalets.mydates.ui.app_components.AppBottomBar
import com.emikhalets.mydates.ui.app_components.AppTopBar
import com.emikhalets.mydates.ui.app_components.EventListItem
import com.emikhalets.mydates.ui.theme.AppTheme
import com.emikhalets.mydates.utils.buildEventsList

@Composable
fun EventsListsScreen() {
    val events = remember { mutableStateListOf<Event>() }

    EventsListsScreen(
        events = events,
        onClickEvent = {}
    )
}

@Composable
private fun EventsListsScreen(
    events: List<Event>,
    onClickEvent: (Event) -> Unit
) {
    Scaffold(
        backgroundColor = MaterialTheme.colors.surface,
        topBar = {
            AppTopBar(
                title = stringResource(R.string.title_home),
                rootScreen = true
            )
        },
        bottomBar = {
            AppBottomBar(
                onHomeClick = {},
                onCalendarClick = {}
            )
        }
    ) {
        LazyColumn(modifier = Modifier.padding(start = 8.dp, end = 8.dp)) {
            items(events) { event ->
                EventListItem(
                    event = event,
                    onClick = onClickEvent
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EventsListsScreenPreview() {
    AppTheme {
        EventsListsScreen(
            events = buildEventsList(),
            onClickEvent = {}
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, backgroundColor = 1)
@Composable
private fun EventsListsScreenDarkPreview() {
    AppTheme {
        EventsListsScreen(
            events = buildEventsList(),
            onClickEvent = {}
        )
    }
}