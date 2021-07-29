package com.emikhalets.mydates.ui.app_components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emikhalets.mydates.ui.theme.AppTheme

@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(16.dp),
        contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 16.dp),
        modifier = modifier
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.button
        )
    }
}

@Composable
fun AppTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    TextButton(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(16.dp),
        contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 16.dp),
        modifier = modifier
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.button,
            color = MaterialTheme.colors.onSurface
        )
    }
}

@Composable
fun AppAddButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        shape = CircleShape,
        modifier = modifier.size(52.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.onPrimary,
            contentColor = MaterialTheme.colors.primary
        )
    ) {
        Icon(
            imageVector = Icons.Rounded.Add,
            contentDescription = "Add icon",
            modifier = Modifier.scale(1.5f)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AppButtonPreview() {
    AppTheme {
        AppButton("Preview button", {})
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun AppButtonDarkPreview() {
    AppTheme {
        AppButton("Preview button", {})
    }
}

@Preview(showBackground = true)
@Composable
private fun AppTextButtonPreview() {
    AppTheme {
        AppTextButton("Preview button", {})
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES, backgroundColor = 1)
@Composable
private fun AppTextButtonDarkPreview() {
    AppTheme {
        AppTextButton("Preview button", {})
    }
}

@Preview(showBackground = true)
@Composable
private fun AppAddButtonPreview() {
    AppTheme {
        AppAddButton({})
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES, backgroundColor = 1)
@Composable
private fun AppAddButtonDarkPreview() {
    AppTheme {
        AppAddButton({})
    }
}