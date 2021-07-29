package com.emikhalets.mydates.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.emikhalets.mydates.R

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