package com.emikhalets.mydates.ui.app_components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.emikhalets.mydates.utils.navigation.back
import com.emikhalets.mydates.utils.navigation.toAddEvent
import com.emikhalets.mydates.utils.navigation.toEventsList
import com.emikhalets.mydates.utils.navigation.toSettings

@Composable
fun AppScaffold(
    navController: NavHostController,
    topBarTitle: String,
    showBackButton: Boolean,
    showSettingsButton: Boolean,
    showBottomBar: Boolean,
    content: @Composable () -> Unit
) {
    Scaffold(
        backgroundColor = MaterialTheme.colors.surface,
        topBar = {
            AppTopBar(
                navController = navController,
                title = topBarTitle,
                showBackIcon = showBackButton,
                showSettingsIcon = showSettingsButton
            )
        },
        bottomBar = {
            if (showBottomBar) AppBottomBar(navController = navController)
        },
    ) {
        Column(Modifier.padding(it)) {
            content()
        }
    }
}

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
            Icon(
                imageVector = Icons.Rounded.ArrowBack,
                contentDescription = "Arrow back icon",
                tint = MaterialTheme.colors.onPrimary,
                modifier = Modifier
                    .clickable { navController.back() }
                    .padding(top = 14.dp, bottom = 14.dp)
                    .size(56.dp)
            )
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
                    .clickable { navController.toSettings() }
                    .padding(top = 14.dp, bottom = 14.dp)
                    .size(56.dp)
            )
        }
    }
}

@Composable
fun AppBottomBar(navController: NavHostController) {
    BottomAppBar(
        backgroundColor = MaterialTheme.colors.primary,
        elevation = 0.dp
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .clickable { navController.toEventsList() }
                .fillMaxSize()
                .weight(1f)
        ) {
            Icon(
                imageVector = Icons.Rounded.List,
                contentDescription = "Home screen icon",
                tint = MaterialTheme.colors.onPrimary,
                modifier = Modifier.size(32.dp)
            )
        }
        AppAddButton(onClick = { navController.toAddEvent() })
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .clickable {}
                .fillMaxSize()
                .weight(1f)
        ) {
            Icon(
                imageVector = Icons.Rounded.CalendarToday,
                contentDescription = "Calendar screen icon",
                tint = MaterialTheme.colors.onPrimary,
                modifier = Modifier.size(32.dp)
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