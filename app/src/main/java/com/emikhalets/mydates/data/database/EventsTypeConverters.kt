package com.emikhalets.mydates.data.database

import androidx.room.TypeConverter

class EventsTypeConverters {

    private val listSeparator = ", "

    @TypeConverter
    fun fromListToString(list: List<String>): String {
        return if (list.isNotEmpty()) {
            list.joinToString(separator = listSeparator)
        } else {
            ""
        }
    }

    @TypeConverter
    fun fromStringToList(string: String): List<String> {
        return if (string.isNotEmpty()) {
            string.split(listSeparator)
        } else {
            emptyList()
        }
    }
}