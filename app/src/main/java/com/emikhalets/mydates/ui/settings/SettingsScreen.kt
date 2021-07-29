package com.emikhalets.mydates.ui.settings

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emikhalets.mydates.R
import com.emikhalets.mydates.ui.app_components.AppTopBar
import com.emikhalets.mydates.ui.theme.AppTheme
import com.emikhalets.mydates.utils.SettingLanguage
import com.emikhalets.mydates.utils.SettingTheme
import com.emikhalets.mydates.utils.getName

@Composable
fun SettingsScreen() {
    var language by remember { mutableStateOf(SettingLanguage.RUSSIAN) }
    var expandLanguage by remember { mutableStateOf(false) }

    var theme by remember { mutableStateOf(SettingTheme.LIGHT) }
    var expandTheme by remember { mutableStateOf(false) }

    var notificationsHour by remember { mutableStateOf(0) }
    var notificationsMinute by remember { mutableStateOf(0) }

    var notificationsAll by remember { mutableStateOf(false) }
    var notificationsMonth by remember { mutableStateOf(false) }
    var notificationsWeek by remember { mutableStateOf(false) }
    var notificationsThreeDay by remember { mutableStateOf(false) }
    var notificationsTwoDay by remember { mutableStateOf(false) }
    var notificationsDay by remember { mutableStateOf(false) }
    var notificationsToday by remember { mutableStateOf(false) }

    SettingsScreen(
        language = language,
        languageChange = { language = it },
        expandLanguage = expandLanguage,
        expandLanguageChange = { expandLanguage = it },
        theme = theme,
        themeChange = { theme = it },
        expandTheme = expandTheme,
        expandThemeChange = { expandTheme = it },
        notificationsAll = notificationsAll,
        notificationsAllChange = { notificationsAll = it },
        notificationsHour = notificationsHour,
        notificationsMinute = notificationsMinute,
        notificationsTimeChange = {},
        notificationsMonth = notificationsMonth,
        notificationsMontChange = { notificationsMonth = it },
        notificationsWeek = notificationsWeek,
        notificationsWeekChange = { notificationsWeek = it },
        notificationsThreeDay = notificationsThreeDay,
        notificationsThreeDayChange = { notificationsThreeDay = it },
        notificationsTwoDay = notificationsTwoDay,
        notificationsTwoDayChange = { notificationsTwoDay = it },
        notificationsDay = notificationsDay,
        notificationsDayChange = { notificationsDay = it },
        notificationsToday = notificationsToday,
        notificationsTodayChange = { notificationsToday = it },
        onRestartNotificationsClick = {},
        onImportClick = {},
        onExportClick = {}
    )
}

@Composable
fun SettingsScreen(
    language: SettingLanguage,
    languageChange: (SettingLanguage) -> Unit,
    expandLanguage: Boolean,
    expandLanguageChange: (Boolean) -> Unit,
    theme: SettingTheme,
    themeChange: (SettingTheme) -> Unit,
    expandTheme: Boolean,
    expandThemeChange: (Boolean) -> Unit,
    notificationsAll: Boolean,
    notificationsAllChange: (Boolean) -> Unit,
    notificationsHour: Int,
    notificationsMinute: Int,
    notificationsTimeChange: () -> Unit,
    notificationsMonth: Boolean,
    notificationsMontChange: (Boolean) -> Unit,
    notificationsWeek: Boolean,
    notificationsWeekChange: (Boolean) -> Unit,
    notificationsThreeDay: Boolean,
    notificationsThreeDayChange: (Boolean) -> Unit,
    notificationsTwoDay: Boolean,
    notificationsTwoDayChange: (Boolean) -> Unit,
    notificationsDay: Boolean,
    notificationsDayChange: (Boolean) -> Unit,
    notificationsToday: Boolean,
    notificationsTodayChange: (Boolean) -> Unit,
    onRestartNotificationsClick: () -> Unit,
    onImportClick: () -> Unit,
    onExportClick: () -> Unit
) {
    Scaffold(
        backgroundColor = MaterialTheme.colors.surface,
        topBar = {
            AppTopBar(
                title = stringResource(R.string.title_settings),
                rootScreen = false
            )
        }
    ) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            SettingsSection(
                title = stringResource(R.string.settings_general)
            ) {
                SettingsExpandable(
                    title = stringResource(R.string.settings_language),
                    value = language.getName(),
                    expanded = expandLanguage,
                    onClick = { expandLanguageChange(true) }
                ) {
                    SettingsItem(
                        text = stringResource(R.string.settings_language_ru),
                        onClick = {
                            languageChange(SettingLanguage.RUSSIAN)
                            expandLanguageChange(false)
                        }
                    )
                    SettingsItem(
                        text = stringResource(R.string.settings_language_en),
                        onClick = {
                            languageChange(SettingLanguage.ENGLISH)
                            expandLanguageChange(false)
                        }
                    )
                }
                SettingsExpandable(
                    title = stringResource(R.string.settings_theme),
                    value = theme.getName(),
                    expanded = expandTheme,
                    onClick = { expandThemeChange(true) }
                ) {
                    SettingsItem(
                        text = stringResource(R.string.settings_theme_light),
                        onClick = {
                            themeChange(SettingTheme.LIGHT)
                            expandThemeChange(false)
                        }
                    )
                    SettingsItem(
                        text = stringResource(R.string.settings_theme_dark),
                        onClick = {
                            themeChange(SettingTheme.DARK)
                            expandThemeChange(false)
                        }
                    )
                }
            }
            SettingsSection(
                title = stringResource(R.string.settings_notifications)
            ) {
                SettingsSwitchItem(
                    text = stringResource(R.string.settings_notifications_all),
                    checked = notificationsAll,
                    checkedChange = notificationsAllChange
                )
                SettingsTime(
                    title = stringResource(R.string.settings_notifications_time),
                    time = "$notificationsHour:$notificationsMinute",
                    expanded = notificationsAll,
                    onClick = notificationsTimeChange
                )
                SettingsSwitchItem(
                    text = stringResource(R.string.settings_notifications_month),
                    checked = notificationsMonth,
                    checkedChange = notificationsMontChange
                )
                SettingsSwitchItem(
                    text = stringResource(R.string.settings_notifications_week),
                    checked = notificationsWeek,
                    checkedChange = notificationsWeekChange
                )
                SettingsSwitchItem(
                    text = stringResource(R.string.settings_notifications_three_day),
                    checked = notificationsThreeDay,
                    checkedChange = notificationsThreeDayChange
                )
                SettingsSwitchItem(
                    text = stringResource(R.string.settings_notifications_two_day),
                    checked = notificationsTwoDay,
                    checkedChange = notificationsTwoDayChange
                )
                SettingsSwitchItem(
                    text = stringResource(R.string.settings_notifications_day),
                    checked = notificationsDay,
                    checkedChange = notificationsDayChange
                )
                SettingsSwitchItem(
                    text = stringResource(R.string.settings_notifications_today),
                    checked = notificationsToday,
                    checkedChange = notificationsTodayChange
                )
                SettingsItem(
                    text = stringResource(R.string.settings_restart_notifications),
                    onClick = { onRestartNotificationsClick() }
                )
            }
            SettingsSection(
                title = stringResource(R.string.settings_backup)
            ) {
                SettingsItem(
                    text = stringResource(R.string.settings_backup_import),
                    onClick = { onImportClick() }
                )
                SettingsItem(
                    text = stringResource(R.string.settings_backup_export),
                    onClick = { onExportClick() }
                )
            }
            Spacer(modifier = Modifier.size(8.dp))
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = 4.dp,
        modifier = modifier
            .padding(start = 8.dp, end = 8.dp, top = 8.dp)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(start = 8.dp)
            )
            content()
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun SettingsExpandable(
    title: String,
    value: String,
    expanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .clickable { onClick() }
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Row {
            Text(
                text = title,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.body1
            )
        }
        AnimatedVisibility(visible = expanded) {
            Column {
                content()
            }
        }
    }
}

@Composable
private fun SettingsItem(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = MaterialTheme.typography.body1,
        modifier = modifier
            .clickable { onClick() }
            .padding(8.dp)
            .fillMaxWidth()
    )
}

@Composable
private fun SettingsSwitchItem(
    text: String,
    checked: Boolean,
    checkedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.body1,
            modifier = modifier.weight(1f)
        )
        Switch(
            checked = checked,
            onCheckedChange = checkedChange,
            modifier = modifier.align(Alignment.CenterVertically)
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun SettingsTime(
    title: String,
    time: String,
    expanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(visible = expanded) {
        Column(
            modifier = modifier
                .clickable { onClick() }
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Row {
                Text(
                    text = title,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = time,
                    style = MaterialTheme.typography.body1
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenScreenPreview() {
    AppTheme {
        SettingsScreen(
            language = SettingLanguage.RUSSIAN,
            languageChange = {},
            expandLanguage = false,
            expandLanguageChange = {},
            theme = SettingTheme.LIGHT,
            themeChange = {},
            expandTheme = false,
            expandThemeChange = {},
            notificationsAll = false,
            notificationsAllChange = {},
            notificationsHour = 11,
            notificationsMinute = 0,
            notificationsTimeChange = {},
            notificationsMonth = false,
            notificationsMontChange = {},
            notificationsWeek = false,
            notificationsWeekChange = {},
            notificationsThreeDay = false,
            notificationsThreeDayChange = {},
            notificationsTwoDay = false,
            notificationsTwoDayChange = {},
            notificationsDay = false,
            notificationsDayChange = {},
            notificationsToday = false,
            notificationsTodayChange = {},
            onRestartNotificationsClick = {},
            onImportClick = {},
            onExportClick = {}
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, backgroundColor = 1)
@Composable
private fun SettingsScreenDarkPreview() {
    AppTheme {
        SettingsScreen(
            language = SettingLanguage.RUSSIAN,
            languageChange = {},
            expandLanguage = false,
            expandLanguageChange = {},
            theme = SettingTheme.LIGHT,
            themeChange = {},
            expandTheme = false,
            expandThemeChange = {},
            notificationsAll = false,
            notificationsAllChange = {},
            notificationsHour = 11,
            notificationsMinute = 0,
            notificationsTimeChange = {},
            notificationsMonth = false,
            notificationsMontChange = {},
            notificationsWeek = false,
            notificationsWeekChange = {},
            notificationsThreeDay = false,
            notificationsThreeDayChange = {},
            notificationsTwoDay = false,
            notificationsTwoDayChange = {},
            notificationsDay = false,
            notificationsDayChange = {},
            notificationsToday = false,
            notificationsTodayChange = {},
            onRestartNotificationsClick = {},
            onImportClick = {},
            onExportClick = {}
        )
    }
}