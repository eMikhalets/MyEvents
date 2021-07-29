package com.emikhalets.mydates.ui.app_components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.emikhalets.mydates.ui.theme.AppTheme

@Composable
fun AppDatePickerDialog(
    timestamp: Long,
    onDateSelected: (Long) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
}


@Preview(showBackground = true)
@Composable
fun AppDatePickerDialogPreview() {
    AppTheme {
        AppDatePickerDialog(1627551042000, {}, {})
    }
}