package com.emikhalets.mydates.ui.app_components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.List
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emikhalets.mydates.ui.theme.AppTheme

@Composable
fun AppTopBar(
    title: String,
    rootScreen: Boolean,
    onBackClick: () -> Unit = {}
) {
    TopAppBar(
        backgroundColor = MaterialTheme.colors.primary,
        elevation = 0.dp
    ) {
        Row {
            if (!rootScreen) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = "Arrow back icon",
                    tint = MaterialTheme.colors.onPrimary,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .clickable { onBackClick() }
                )
            }
            Spacer(modifier = Modifier.size(16.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.h5,
                color = MaterialTheme.colors.onPrimary
            )
        }
    }
}

@Composable
fun AppBottomBar(
    onHomeClick: () -> Unit,
    onCalendarClick: () -> Unit
) {
    BottomAppBar(
        backgroundColor = MaterialTheme.colors.primary,
        elevation = 0.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable { onHomeClick() }
                .weight(1f)
        ) {
            Icon(
                imageVector = Icons.Rounded.List,
                contentDescription = "Home screen icon",
                tint = MaterialTheme.colors.onPrimary,
                modifier = Modifier
                    .size(32.dp)
                    .align(Alignment.Center)
            )
        }
        AppAddButton(onClick = {})
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable { onCalendarClick() }
                .weight(1f)
        ) {
            Icon(
                imageVector = Icons.Rounded.CalendarToday,
                contentDescription = "Calendar screen icon",
                tint = MaterialTheme.colors.onPrimary,
                modifier = Modifier
                    .size(32.dp)
                    .align(Alignment.Center)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AppTopBarPreview() {
    AppTheme {
        AppTopBar("Some Title", false)
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun AppTopBarDarkPreview() {
    AppTheme {
        AppTopBar("Some Title", false)
    }
}

@Preview(showBackground = true)
@Composable
private fun AppBottomBarPreview() {
    AppTheme {
        AppBottomBar({}, {})
    }
}


@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun AppBottomBarDarkPreview() {
    AppTheme {
        AppBottomBar({}, {})
    }
}