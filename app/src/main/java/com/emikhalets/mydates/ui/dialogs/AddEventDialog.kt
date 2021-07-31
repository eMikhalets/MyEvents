package com.emikhalets.mydates.ui.dialogs

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.emikhalets.mydates.R
import com.emikhalets.mydates.ui.theme.AppTheme
import com.emikhalets.mydates.utils.EventType
import com.emikhalets.mydates.utils.navigation.navigateToAddEvent

@Composable
fun AddEventDialog(
    navController: NavHostController,
    showDialog: Boolean,
    onShowDialogChange: (Boolean) -> Unit
) {
    if (showDialog) {
        Dialog(
            onDismissRequest = { onShowDialogChange(false) },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = false
            )
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colors.surface,
                        shape = RoundedCornerShape(12.dp)
                    )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(R.string.add_event_text_new_event),
                        style = MaterialTheme.typography.h5,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.size(16.dp))
                    Row {
                        AddEventCard(
                            title = stringResource(R.string.add_event_text_anniversary),
                            image = painterResource(R.drawable.ic_anniversary),
                            onClick = {
                                onShowDialogChange(false)
                                navController.navigateToAddEvent(EventType.ANNIVERSARY)
                            },
                            modifier = Modifier.weight(1f)
                        )
                        AddEventCard(
                            title = stringResource(R.string.add_event_text_birthday),
                            image = painterResource(R.drawable.ic_birthday),
                            onClick = {
                                onShowDialogChange(false)
                                navController.navigateToAddEvent(EventType.BIRTHDAY)
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AddEventCard(
    title: String,
    image: Painter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp,
        modifier = modifier
            .padding(4.dp)
            .clickable { onClick() }
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {
            Image(
                painter = image,
                contentDescription = ""
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()

            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddEventDialogPreview() {
    AppTheme {
        AddEventDialog(
            navController = rememberNavController(),
            showDialog = true,
            onShowDialogChange = {}
        )
    }
}