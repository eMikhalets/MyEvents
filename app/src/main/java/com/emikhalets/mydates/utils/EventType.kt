package com.emikhalets.mydates.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.emikhalets.mydates.R

enum class EventType(val value: Int) {
    ANNIVERSARY(1),
    BIRTHDAY(2);

    companion object {
        fun get(typeCode: Int): EventType {
            return when (typeCode) {
                1 -> ANNIVERSARY
                2 -> BIRTHDAY
                else -> BIRTHDAY
            }
        }
    }
}

@Composable
fun EventType.getEventTypeName(): String {
    return when (this) {
        EventType.ANNIVERSARY -> stringResource(R.string.anniversary)
        EventType.BIRTHDAY -> stringResource(R.string.birthday)
    }
}

@Composable
fun EventType.getEventTypeImage(): Painter {
    return when (this) {
        EventType.ANNIVERSARY -> painterResource(R.drawable.ic_anniversary)
        EventType.BIRTHDAY -> painterResource(R.drawable.ic_birthday)
    }
}

@Composable
fun SettingLanguage.getName(): String {
    return when (this) {
        SettingLanguage.RUSSIAN -> stringResource(R.string.settings_language_ru)
        SettingLanguage.ENGLISH -> stringResource(R.string.settings_language_en)
    }
}

@Composable
fun SettingTheme.getName(): String {
    return when (this) {
        SettingTheme.LIGHT -> stringResource(R.string.settings_theme_light)
        SettingTheme.DARK -> stringResource(R.string.settings_theme_dark)
    }
}