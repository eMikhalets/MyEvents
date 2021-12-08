package com.emikhalets.mydates.utils.extentions

import java.util.*

fun Calendar.day(): Int {
    return get(Calendar.DAY_OF_MONTH)
}

fun Calendar.dayOfYear(): Int {
    return get(Calendar.DAY_OF_YEAR)
}

fun Calendar.month(): Int {
    return get(Calendar.MONTH)
}

fun Calendar.monthMaxDay(): Int {
    return getActualMaximum(Calendar.DAY_OF_MONTH)
}

fun Calendar.nextMonthMaxDay(): Int {
    val next = Calendar.getInstance()
    next.set(year(), month() + 1, day())
    return next.getActualMaximum(Calendar.DAY_OF_MONTH)
}

fun Calendar.year(): Int {
    return get(Calendar.YEAR)
}

fun Calendar.plusMonthAndLastDay(): Calendar {
    if (month() + 1 > 11) {
        set(Calendar.YEAR, year() + 1)
        set(Calendar.MONTH, Calendar.JANUARY)
    } else {
        set(Calendar.MONTH, month() + 1)
    }
    set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DAY_OF_MONTH))
    return this
}

fun Calendar.minusMonthAndFirstDay(): Calendar {
    if (month() - 1 < 0) {
        set(Calendar.YEAR, year() - 1)
        set(Calendar.MONTH, Calendar.DECEMBER)
    } else {
        set(Calendar.MONTH, month() - 1)
    }
    set(Calendar.DAY_OF_MONTH, 1)
    return this
}

fun Long.toCalendar(year: Int = Calendar.getInstance().year()): Calendar {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    calendar.set(Calendar.YEAR, year)
    return calendar
}