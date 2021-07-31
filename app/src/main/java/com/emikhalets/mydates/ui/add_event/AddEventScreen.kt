package com.emikhalets.mydates.ui.add_event

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.emikhalets.mydates.R
import com.emikhalets.mydates.ui.app_components.*
import com.emikhalets.mydates.ui.theme.AppTheme
import com.emikhalets.mydates.utils.EventType
import com.emikhalets.mydates.utils.getEventTypeImage
import com.emikhalets.mydates.utils.getEventTypeName
import com.emikhalets.mydates.utils.navigation.navigateToBack
import java.util.*

@Composable
fun AddEventScreen(
    navController: NavHostController,
    eventType: EventType,
    viewModel: AddEventVM = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }
    var lastname by remember { mutableStateOf("") }
    var middleName by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(Date().time) }
    var dateString by remember { mutableStateOf("") }
    var withoutYear by remember { mutableStateOf(false) }

    AddEventScreen(
        navController = navController,
        eventType = eventType,
        state = viewModel.state,
        name = name,
        lastname = lastname,
        middleName = middleName,
        date = date,
        dateString = dateString,
        withoutYear = withoutYear,
        onNameChange = { name = it },
        onLastnameChange = { lastname = it },
        onMiddleNameChange = { middleName = it },
        onDateChange = { date = it },
        onDateStringChange = { dateString = it },
        onWithoutYearChange = { withoutYear = it }
    )
}

@Composable
private fun AddEventScreen(
    navController: NavHostController,
    eventType: EventType,
    state: AddEventState,
    name: String,
    lastname: String,
    middleName: String,
    date: Long,
    dateString: String,
    withoutYear: Boolean,
    onNameChange: (String) -> Unit,
    onLastnameChange: (String) -> Unit,
    onMiddleNameChange: (String) -> Unit,
    onDateChange: (Long) -> Unit,
    onDateStringChange: (String) -> Unit,
    onWithoutYearChange: (Boolean) -> Unit
) {
    AppScaffold(
        navController = navController,
        topBarTitle = stringResource(R.string.title_add_event),
        showBackButton = true,
        showSettingsButton = true,
        showBottomBar = false,
        onAddEventClick = {}
    ) {
        when (state) {
            is AddEventState.Error -> {
            }
            AddEventState.Saved -> {
                navController.navigateToBack()
            }
            AddEventState.Loading -> {
                AppLoader()
            }
            AddEventState.Init -> {
                AddEventContent(
                    eventType = eventType,
                    name = name,
                    lastname = lastname,
                    middleName = middleName,
                    date = date,
                    dateString = dateString,
                    withoutYear = withoutYear,
                    onNameChange = onNameChange,
                    onLastnameChange = onLastnameChange,
                    onMiddleNameChange = onMiddleNameChange,
                    onDateChange = onDateChange,
                    onDateStringChange = onDateStringChange,
                    onWithoutYearChange = onWithoutYearChange
                )
            }
        }
    }
}

@Composable
private fun AddEventContent(
    eventType: EventType,
    name: String,
    lastname: String,
    middleName: String,
    date: Long,
    dateString: String,
    withoutYear: Boolean,
    onNameChange: (String) -> Unit,
    onLastnameChange: (String) -> Unit,
    onMiddleNameChange: (String) -> Unit,
    onDateChange: (Long) -> Unit,
    onDateStringChange: (String) -> Unit,
    onWithoutYearChange: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
    ) {
        AddEventTitle(
            title = eventType.getEventTypeName(),
            image = eventType.getEventTypeImage()
        )
        Spacer(modifier = Modifier.size(16.dp))
        AppTextField(
            label = stringResource(R.string.add_event_text_name),
            value = name,
            onValueChange = onNameChange,
            modifier = Modifier.fillMaxWidth()
        )
        if (eventType == EventType.BIRTHDAY) {
            Spacer(modifier = Modifier.size(16.dp))
            AppTextField(
                label = stringResource(R.string.add_event_text_lastname),
                value = lastname,
                onValueChange = onLastnameChange,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.size(16.dp))
            AppTextField(
                label = stringResource(R.string.add_event_text_middle_name),
                value = middleName,
                onValueChange = onMiddleNameChange,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.size(16.dp))
        AppDateField(
            label = stringResource(R.string.add_event_text_date),
            value = dateString,
            onValueChange = onDateStringChange,
            onClick = {},
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(16.dp))
        AppCheckbox(
            label = stringResource(R.string.add_event_text_year),
            checked = withoutYear,
            onCheckedChange = onWithoutYearChange,
            modifier = Modifier.padding(start = 16.dp)
        )
        Spacer(modifier = Modifier.size(40.dp))
        AppButton(
            text = stringResource(R.string.event_details_text_save),
            onClick = {},
            modifier = Modifier
                .padding(start = 52.dp, end = 52.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
private fun AddEventTitle(
    title: String,
    image: Painter
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(
            painter = image,
            contentDescription = "",
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.size(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.h4,
            modifier = Modifier.align(Alignment.CenterVertically)

        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AddEventScreenPreview() {
    AppTheme {
        AddEventScreen(
            navController = rememberNavController(),
            eventType = EventType.BIRTHDAY,
            state = AddEventState.Init,
            name = "Иван",
            lastname = "Иванов",
            middleName = "Иванович",
            date = 0,
            dateString = "21 октября 2015",
            withoutYear = false,
            onNameChange = {},
            onLastnameChange = {},
            onMiddleNameChange = {},
            onDateChange = {},
            onDateStringChange = {},
            onWithoutYearChange = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AddEventScreenDarkPreview() {
    AppTheme(darkTheme = true) {
        AddEventScreen(
            navController = rememberNavController(),
            eventType = EventType.ANNIVERSARY,
            state = AddEventState.Init,
            name = "Иван",
            lastname = "Иванов",
            middleName = "Иванович",
            date = 0,
            dateString = "21 октября 2015",
            withoutYear = false,
            onNameChange = {},
            onLastnameChange = {},
            onMiddleNameChange = {},
            onDateChange = {},
            onDateStringChange = {},
            onWithoutYearChange = {}
        )
    }
}