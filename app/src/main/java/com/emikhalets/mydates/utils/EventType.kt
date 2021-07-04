package com.emikhalets.mydates.utils

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
    }
}