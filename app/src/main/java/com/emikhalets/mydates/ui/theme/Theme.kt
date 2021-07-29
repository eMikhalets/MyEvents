package com.emikhalets.mydates.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = if (darkTheme) DarkColors else LightColors,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}

private val LightColors = lightColors(
    primary = Blue800,
    primaryVariant = Blue900,
    secondary = Blue700,
    background = Color.White,
    surface = Grey100,
    error = Red700,
    onPrimary = Grey100,
    onSecondary = Grey900,
    onBackground = Grey900,
    onSurface = Grey900,
    onError = Grey100
)

private val DarkColors = darkColors(
    primary = Grey800,
    primaryVariant = Grey900,
    secondary = Grey600,
    background = Grey700,
    surface = Grey900,
    error = Red700,
    onPrimary = Grey100,
    onSecondary = Grey200,
    onBackground = Color.White,
    onSurface = Grey100,
    onError = Grey100
)