package com.emikhalets.mydates.utils.enums

import android.content.Context
import com.emikhalets.mydates.R

enum class Language(val value: String) {
    RUSSIAN("ru"),
    ENGLISH("en");

    companion object {

        fun get(value: String): Language {
            return when (value) {
                "ru" -> RUSSIAN
                "en" -> ENGLISH
                else -> RUSSIAN
            }
        }

        fun Language.getLanguageName(context: Context): String {
            return when (this) {
                RUSSIAN -> context.getString(R.string.settings_language_ru)
                ENGLISH -> context.getString(R.string.settings_language_en)
            }
        }
    }
}