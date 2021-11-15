package com.emikhalets.mydates.utils.extentions

import java.text.SimpleDateFormat
import java.util.*

fun Calendar.day(): Int {
    return this.get(Calendar.DAY_OF_MONTH)
}

fun Calendar.dayOfYear(): Int {
    return this.get(Calendar.DAY_OF_YEAR)
}

fun Calendar.month(): Int {
    return this.get(Calendar.MONTH)
}

fun Calendar.year(): Int {
    return this.get(Calendar.YEAR)
}

fun Long.toCalendar(year: Int = Calendar.getInstance().year()): Calendar {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    calendar.set(Calendar.YEAR, year)
    return calendar
}