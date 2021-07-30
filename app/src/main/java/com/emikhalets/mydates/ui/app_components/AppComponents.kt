package com.emikhalets.mydates.ui.app_components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForwardIos
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emikhalets.mydates.R
import com.emikhalets.mydates.data.database.entities.Event
import com.emikhalets.mydates.ui.theme.AppTheme
import com.emikhalets.mydates.utils.*

@Composable
fun AppCheckbox(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
        Text(
            text = label,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onSurface,
            modifier = Modifier.padding(start = 8.dp, end = 8.dp)
        )
    }
}

@Composable
fun EventListItem(
    event: Event,
    onClick: (Event) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = 4.dp,
        backgroundColor = MaterialTheme.colors.background,
        modifier = modifier
            .padding(top = 8.dp)
            .fillMaxWidth()
            .clickable { onClick(event) }
    ) {
        Row(modifier = modifier.padding(8.dp)) {
            Image(
                painter = EventType.get(event.eventType).getEventTypeImage(),
                contentDescription = "Event type image",
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = event.fullName(),
                    style = MaterialTheme.typography.h5,
                )
                Text(
                    text = EventType.get(event.eventType).getEventTypeName(),
                    style = MaterialTheme.typography.body1,
                )
                Row {
                    Text(
                        text = pluralsResource(R.plurals.days_left, event.daysLeft),
                        style = MaterialTheme.typography.body1,
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = pluralsResource(R.plurals.age, event.age),
                        style = MaterialTheme.typography.body1,
                    )
                }
            }
            Spacer(modifier = Modifier.size(8.dp))
            Icon(
                imageVector = Icons.Rounded.ArrowForwardIos,
                contentDescription = "Forward arrow icon",
                tint = MaterialTheme.colors.surface,
                modifier = Modifier
                    .height(80.dp)
                    .width(40.dp)
                    .scale(scaleX = 1f, scaleY = 2f)
                    .align(Alignment.CenterVertically)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AppCheckboxPreview() {
    AppTheme {
        AppCheckbox("Preview", true, {})
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, backgroundColor = 1)
@Composable
private fun AppCheckboxDarkPreview() {
    AppTheme {
        AppCheckbox("Preview", true, {})
    }
}

@Preview(showBackground = true)
@Composable
private fun EventListItemPreview() {
    AppTheme {
        EventListItem(buildEvent(), {})
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, backgroundColor = 1)
@Composable
private fun EventListItemDarkPreview() {
    AppTheme {
        EventListItem(buildEvent(), {})
    }
}