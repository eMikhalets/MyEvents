package com.emikhalets.mydates.ui.event_details

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.emikhalets.mydates.R
import com.emikhalets.mydates.data.database.entities.Event
import com.emikhalets.mydates.ui.app_components.*
import com.emikhalets.mydates.ui.theme.AppTheme
import com.emikhalets.mydates.utils.buildEvent
import com.emikhalets.mydates.utils.pluralsResource
import com.emikhalets.mydates.utils.toDateString

@Composable
fun EventDetailsScreen(
    navController: NavHostController,
    event: Event
) {
    Log.d("TAG", "${event.fullName()}")
    EventDetailsScreen(
        navController = navController,
        event = event,
        onSaveEvent = {},
        onDeleteEvent = {}
    )
}

@Composable
private fun EventDetailsScreen(
    navController: NavHostController,
    event: Event,
    onSaveEvent: () -> Unit,
    onDeleteEvent: () -> Unit
) {
    Scaffold(
        backgroundColor = MaterialTheme.colors.surface,
        topBar = {
            AppTopBar(
                navController = navController,
                title = stringResource(R.string.title_event_details),
                showBackIcon = true,
                showSettingsIcon = true
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
            AppTextField(
                label = stringResource(R.string.event_details_text_notes),
                value = event.notes,
                singleLine = true,
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
private fun EventDetailsHeader(
    fullName: String,
    dateString: String,
    daysLeft: Int,
    age: Int
) {
    Column(modifier = Modifier.fillMaxWidth()) {
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
        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
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
private fun EventDetailsEdit(
    event: Event,
    date: Long,
    onDateChange: (Long) -> Unit
) {
    Column {
        Spacer(modifier = Modifier.size(16.dp))
        AppTextField(
            label = stringResource(R.string.event_details_text_name),
            value = event.name,
            singleLine = false,
            onValueChange = { event.name = it },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(16.dp))
        AppTextField(
            label = stringResource(R.string.add_event_text_lastname),
            value = event.lastName,
            singleLine = false,
            onValueChange = { event.lastName = it },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(16.dp))
        AppTextField(
            label = stringResource(R.string.add_event_text_middle_name),
            value = event.middleName,
            singleLine = false,
            onValueChange = { event.middleName = it },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(16.dp))
        AppTextField(
            label = stringResource(R.string.add_event_text_date),
            value = date.toDateString(),
            singleLine = true,
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
private fun EventDetailsScreenPreview() {
    AppTheme {
        EventDetailsScreen(
            rememberNavController(),
            event = buildEvent(),
            onSaveEvent = {},
            onDeleteEvent = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EventDetailsScreenDarkPreview() {
    AppTheme(darkTheme = true) {
        EventDetailsScreen(
            rememberNavController(),
            event = buildEvent(),
            onSaveEvent = {},
            onDeleteEvent = {}
        )
    }
}