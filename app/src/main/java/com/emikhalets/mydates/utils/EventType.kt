package com.emikhalets.mydates.utils

import android.content.Context
import com.emikhalets.mydates.R

enum class EventType(val value: Int) {
    ANNIVERSARY(1),
    BIRTHDAY(2);

    companion object {

        fun get(typeCode: Int): EventType {
            return when (typeCode) {
                1 -> ANNIVERSARY
                2 -> BIRTHDAY
                else -> BIRTHDAY
            }
        }

        fun EventType.getTypeName(context: Context): String {
            return when (this) {
                ANNIVERSARY -> context.getString(R.string.anniversary)
                BIRTHDAY -> context.getString(R.string.birthday)
            }
        }

        fun EventType.getTypeDate(context: Context, date: Long): String {
            return when (this) {
                ANNIVERSARY -> context.getString(
                    R.string.anniversary_date,
                    date.formatDate("d MMMM")
                )
                BIRTHDAY -> context.getString(
                    R.string.birthday_date,
                    date.formatDate("d MMMM")
                )
            }
        }

        fun EventType.getTypeImage(): Int {
            return when (this) {
                ANNIVERSARY -> R.drawable.ic_anniversary
                BIRTHDAY -> R.drawable.ic_birthday
            }
        }

        fun EventType.getTypeImageLarge(): Int {
            return when (this) {
                ANNIVERSARY -> R.drawable.ic_anniversary_large
                BIRTHDAY -> R.drawable.ic_birthday_large
            }
        }
    }
}