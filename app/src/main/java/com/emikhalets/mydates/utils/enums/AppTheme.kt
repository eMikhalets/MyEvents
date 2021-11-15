package com.emikhalets.mydates.utils.enums

import android.content.Context
import com.emikhalets.mydates.R

enum class AppTheme(val themeRes: Int) {
    LIGHT(R.style.Theme_MyDates_Light),
    DARK(R.style.Theme_MyDates_Dark);

    companion object {
        private val themesResMap = values().associateBy(AppTheme::themeRes)
        fun get(themeRes: Int): AppTheme = themesResMap[themeRes] ?: LIGHT

        fun AppTheme.getThemeName(context: Context): String {
            return when (this) {
                LIGHT -> context.getString(R.string.settings_theme_light)
                DARK -> context.getString(R.string.settings_theme_dark)
            }
        }
    }
}