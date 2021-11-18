package com.emikhalets.mydates.utils.enums

import android.content.Context
import com.emikhalets.mydates.R

enum class Language(val langCode: String) {
    RUSSIAN("ru"),
    ENGLISH("en");

    companion object {
        private val langCodeMap = values().associateBy(Language::langCode)
        fun get(langCode: String): Language = langCodeMap[langCode] ?: RUSSIAN

        fun Language.getName(context: Context): String {
            return when (this) {
                RUSSIAN -> context.getString(R.string.settings_language_ru)
                ENGLISH -> context.getString(R.string.settings_language_en)
            }
        }
    }
}