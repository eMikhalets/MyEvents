package com.emikhalets.mydates.ui.event_details

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emikhalets.mydates.R
import com.emikhalets.mydates.data.database.entities.Event
import com.emikhalets.mydates.ui.app_components.*
import com.emikhalets.mydates.ui.theme.AppTheme
import com.emikhalets.mydates.utils.pluralsResource
import com.emikhalets.mydates.utils.toDateString

@Composable
fun EventDetailsScreen() {
    val event by remember { mutableStateOf<Event?>(null) }

    if (event != null) EventDetailsScreen(
        event = event!!,
        onSaveEvent = {},
        onDeleteEvent = {}
    )
}

@Composable
private fun EventDetailsScreen(
    event: Event,
    onSaveEvent: () -> Unit,
    onDeleteEvent: () -> Unit
) {
    Scaffold(
        backgroundColor = MaterialTheme.colors.surface,
        topBar = {
            AppTopBar(
                title = stringResource(R.string.title_event_details),
                rootScreen = false
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            EventDetailsHeader(
                fullName = event.fullName(),
                dateString = event.date.toDateString(),
                daysLeft = event.daysLeft,
                age = event.age
            )
            Spacer(modifier = Modifier.size(16.dp))
            AppTextFieldMultiline(
                label = stringResource(R.string.event_details_text_notes),
                value = event.notes,
                onValueChange = { event.notes = it },
                modifier = Modifier.fillMaxWidth()
            )
            EventDetailsEdit(
                event = event,
                date = event.date,
                onDateChange = { event.date = it }
            )
            Spacer(modifier = Modifier.size(40.dp))
            AppButton(
                text = stringResource(R.string.event_details_text_save),
                onClick = onSaveEvent,
                modifier = Modifier
                    .padding(start = 52.dp, end = 52.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.size(16.dp))
            AppTextButton(
                text = stringResource(R.string.event_details_text_delete),
                onClick = onDeleteEvent,
                modifier = Modifier
                    .padding(start = 52.dp, end = 52.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.size(32.dp))
        }
    }
}

@Composable
fun EventDetailsHeader(
    fullName: String,
    dateString: String,
    daysLeft: Int,
    age: Int
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(
            painter = painterResource(R.drawable.ic_birthday),
            contentDescription = "Event type icon",
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.size(16.dp))
        Text(
            text = fullName,
            style = MaterialTheme.typography.h5,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.size(16.dp))
        Text(
            text = stringResource(R.string.birthday_date, dateString),
            style = MaterialTheme.typography.body1,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = pluralsResource(R.plurals.days_left, daysLeft),
                style = MaterialTheme.typography.body1
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = pluralsResource(R.plurals.age, age),
                style = MaterialTheme.typography.body1
            )
        }
    }
}

@Composable
fun EventDetailsEdit(
    event: Event,
    date: Long,
    onDateChange: (Long) -> Unit
) {
    Column {
        Spacer(modifier = Modifier.size(16.dp))
        AppTextField(
            label = stringResource(R.string.event_details_text_name),
            value = event.name,
            onValueChange = { event.name = it },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(16.dp))
        AppTextField(
            label = stringResource(R.string.add_event_text_lastname),
            value = event.lastName,
            onValueChange = { event.lastName = it },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(16.dp))
        AppTextField(
            label = stringResource(R.string.add_event_text_middle_name),
            value = event.middleName,
            onValueChange = { event.middleName = it },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(16.dp))
        AppDateField(
            label = stringResource(R.string.add_event_text_date),
            value = date.toDateString(),
            onValueChange = {},
            onClick = {},
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(16.dp))
        AppCheckbox(
            label = stringResource(R.string.add_event_text_year),
            checked = event.withoutYear,
            onCheckedChange = { event.withoutYear = it },
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EventDetailsScreenPreview() {
    AppTheme {
        EventDetailsScreen(
            event = Event(
                id = 0,
                name = "Иван",
                lastName = "Иванов",
                middleName = "Иванович",
                date = 1627551042000,
                daysLeft = 36,
                age = 60,
                eventType = 2,
                group = "",
                notes = "text text text text text text text text text text text text text",
                withoutYear = false
            ),
            onSaveEvent = {},
            onDeleteEvent = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EventDetailsScreenDarkPreview() {
    AppTheme(darkTheme = true) {
        EventDetailsScreen(
            event = Event(
                id = 0,
                name = "Иван",
                lastName = "Иванов",
                middleName = "Иванович",
                date = 1627551042000,
                daysLeft = 36,
                age = 60,
                eventType = 2,
                group = "",
                notes = "text text text text text text text text text text text text text",
                withoutYear = false
            ),
            onSaveEvent = {},
            onDeleteEvent = {}
        )
    }
}