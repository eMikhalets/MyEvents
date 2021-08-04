package com.emikhalets.mydates.utils.enums

import android.content.Context
import com.emikhalets.mydates.R

enum class Theme(val value: Int) {
    LIGHT(0),
    DARK(1);

    companion object {

        fun get(value: Int): Theme {
            return when (value) {
                0 -> LIGHT
                1 -> DARK
                else -> LIGHT
            }
        }

        fun Theme.getThemeName(context: Context): String {
            return when (this) {
                LIGHT -> context.getString(R.string.settings_theme_light)
                DARK -> context.getString(R.string.settings_theme_dark)
            }
        }
    }
}