package com.emikhalets.mydates.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val LightColors = AppColors(
    background = Color(0xFFF3F8FE),
    bar = Color(0xFF3E4685),
    button = Color(0xFF3E4685),
    buttonBar = Color(0xFFF3F8FE),
    textField = Color(0xFFFFFFFF),
    card = Color(0xFFFFFFFF),
    onBackground = Color(0xFF393939),
    onBar = Color(0xFFFFFFFF),
    onButton = Color(0xFFFFFFFF),
    onButtonBar = Color(0xFF3E4685),
    onCard = Color(0xFF393939),
    onTextField = Color(0xFF393939),
    onTextFieldHint = Color(0xFF868686),
    isDark = false
)

private val DarkColors = AppColors(
    background = Color(0xFF2D2D2D),
    bar = Color(0xFF424242),
    button = Color(0xFFFFC592),
    buttonBar = Color(0xFFFFC592),
    textField = Color(0xFF424242),
    card = Color(0xFF424242),
    onBackground = Color(0xFFE8E8E8),
    onBar = Color(0xFFE8E8E8),
    onButton = Color(0xFF483E36),
    onButtonBar = Color(0xFF483E36),
    onCard = Color(0xFFB2B2B2),
    onTextField = Color(0xFFE8E8E8),
    onTextFieldHint = Color(0xFFB2B2B2),
    isDark = false
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    val sysUiController = rememberSystemUiController()
    SideEffect {
        sysUiController.setSystemBarsColor(
            color = colors.bar
        )
    }

    ProvideAppColors(colors) {
        MaterialTheme(
            colors = debugColors(darkTheme),
            typography = AppTypography,
            shapes = AppShapes,
            content = content
        )
    }
}

object AppTheme {
    val colors: AppColors
        @Composable
        get() = LocalAppColors.current
}

@Composable
private fun ProvideAppColors(
    colors: AppColors,
    content: @Composable () -> Unit
) {
    val colorPalette = remember { colors.copy() }
    colorPalette.update(colors)
    CompositionLocalProvider(LocalAppColors provides colorPalette, content = content)
}

private val LocalAppColors = staticCompositionLocalOf { LightColors }

/**
 * A Material [Colors] implementation which sets all colors to [debugColor] to discourage usage of
 * [MaterialTheme.colors] in preference to [AppTheme.colors].
 */
private fun debugColors(
    darkTheme: Boolean,
    debugColor: Color = Color.Magenta
) = Colors(
    primary = debugColor,
    primaryVariant = debugColor,
    secondary = debugColor,
    secondaryVariant = debugColor,
    background = debugColor,
    surface = debugColor,
    error = debugColor,
    onPrimary = debugColor,
    onSecondary = debugColor,
    onBackground = debugColor,
    onSurface = debugColor,
    onError = debugColor,
    isLight = !darkTheme
)