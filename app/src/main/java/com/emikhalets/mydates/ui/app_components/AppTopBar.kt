package com.emikhalets.mydates.ui.app_components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Arrow back icon",
                    tint = MaterialTheme.colors.onPrimary,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .clickable { onBackClick() }
                )
                Spacer(
                    modifier = Modifier.size(16.dp)
                )
            }
            Text(
                text = title,
                style = MaterialTheme.typography.h5,
                color = MaterialTheme.colors.onPrimary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppTopBarPreview() {
    AppTheme {
        AppTopBar(" Some Title", false)
    }
}


@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun AppTopBarDarkPreview() {
    AppTheme {
        AppTopBar(" Some Title", false)
    }
}