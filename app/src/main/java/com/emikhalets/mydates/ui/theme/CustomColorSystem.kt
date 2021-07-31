package com.emikhalets.mydates.ui.theme

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

/**
 * Application custom color palette
 */
@Stable
class AppColors(
    background: Color,
    bar: Color,
    button: Color,
    buttonBar: Color,
    textField: Color,
    card: Color,
    onBackground: Color,
    onBar: Color,
    onButton: Color,
    onButtonBar: Color,
    onCard: Color,
    onTextField: Color,
    onTextFieldHint: Color,
    isDark: Boolean
) {
    var background by mutableStateOf(background)
        private set
    var bar by mutableStateOf(bar)
        private set
    var button by mutableStateOf(button)
        private set
    var buttonBar by mutableStateOf(buttonBar)
        private set
    var textField by mutableStateOf(textField)
        private set
    var card by mutableStateOf(card)
        private set
    var onBackground by mutableStateOf(onBackground)
        private set
    var onBar by mutableStateOf(onBar)
        private set
    var onButton by mutableStateOf(onButton)
        private set
    var onButtonBar by mutableStateOf(onButtonBar)
        private set
    var onCard by mutableStateOf(onCard)
        private set
    var onTextField by mutableStateOf(onTextField)
        private set
    var onTextFieldHint by mutableStateOf(onTextFieldHint)
        private set
    var isDark by mutableStateOf(isDark)
        private set

    fun update(other: AppColors) {
        background = other.background
        bar = other.bar
        button = other.button
        buttonBar = other.buttonBar
        textField = other.textField
        card = other.card
        onBackground = other.onBackground
        onBar = other.onBar
        onButton = other.onButton
        onButtonBar = other.onButtonBar
        onCard = other.onCard
        onTextField = other.onTextField
        onTextFieldHint = other.onTextFieldHint
        isDark = other.isDark
    }

    fun copy(): AppColors = AppColors(
        background = background,
        bar = bar,
        button = button,
        buttonBar = buttonBar,
        textField = textField,
        card = card,
        onBackground = onBackground,
        onBar = onBar,
        onButton = onButton,
        onButtonBar = onButtonBar,
        onCard = onCard,
        onTextField = onTextField,
        onTextFieldHint = onTextFieldHint,
        isDark = isDark
    )
}