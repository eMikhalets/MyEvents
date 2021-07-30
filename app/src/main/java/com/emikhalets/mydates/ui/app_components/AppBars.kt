package com.emikhalets.mydates.ui.app_components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Box
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.emikhalets.mydates.ui.theme.AppTheme
import com.emikhalets.mydates.utils.navigation.NavActions

@Composable
fun AppTopBar(
    navController: NavHostController,
    title: String,
    showBackIcon: Boolean,
    showSettingsIcon: Boolean
) {
    TopAppBar(
        backgroundColor = MaterialTheme.colors.primary,
        elevation = 0.dp
    ) {
        if (showBackIcon) {
            Row(modifier = Modifier.clickable { NavActions(navController).back }) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = "Arrow back icon",
                    tint = MaterialTheme.colors.onPrimary,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }
        Spacer(modifier = Modifier.size(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.h5,
            color = MaterialTheme.colors.onPrimary,
            modifier = Modifier.weight(1f)
        )
        if (showSettingsIcon) {
            Icon(
                imageVector = Icons.Rounded.Settings,
                contentDescription = "Settings icon",
                tint = MaterialTheme.colors.onPrimary,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .clickable { NavActions(navController).toSettings }
            )
        }
        Spacer(modifier = Modifier.size(16.dp))
    }
}

@Composable
fun AppBottomBar(navController: NavHostController) {
    BottomAppBar(
        backgroundColor = MaterialTheme.colors.primary,
        elevation = 0.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable { NavActions(navController).toEventsList }
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
        AppAddButton(onClick = NavActions(navController).toAddEvent)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    //TODO: navigation to calendar view
                }
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
        AppTopBar(
            title = "Some Title",
            navController = rememberNavController(),
            showBackIcon = true,
            showSettingsIcon = true
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun AppTopBarDarkPreview() {
    AppTheme {
        AppTopBar(
            title = "Some Title",
            navController = rememberNavController(),
            showBackIcon = true,
            showSettingsIcon = true
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AppBottomBarPreview() {
    AppTheme {
        AppBottomBar(rememberNavController())
    }
}


@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun AppBottomBarDarkPreview() {
    AppTheme {
        AppBottomBar(rememberNavController())
    }
}