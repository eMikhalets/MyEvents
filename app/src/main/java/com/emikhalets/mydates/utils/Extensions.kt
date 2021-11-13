package com.emikhalets.mydates.utils

import com.emikhalets.mydates.data.database.entities.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

fun Calendar.day() = this.get(Calendar.DAY_OF_MONTH)
fun Calendar.dayOfYear() = this.get(Calendar.DAY_OF_YEAR)
fun Calendar.month() = this.get(Calendar.MONTH)
fun Calendar.year() = this.get(Calendar.YEAR)

fun Event.monthNumber(): Int {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this.date
    return calendar.month()
}

fun Event.calculateParameters(): Event {
    val now = Calendar.getInstance()
    val date = Calendar.getInstance()
    date.timeInMillis = this.date

    this.age = now.year() - date.year()
    date.set(Calendar.YEAR, now.year())
    this.daysLeft = date.dayOfYear() - now.dayOfYear()

    when {
        date.month() < now.month() -> {
            this.age++
            date.set(Calendar.YEAR, now.year() + 1)
            this.daysLeft += now.getActualMaximum(Calendar.DAY_OF_YEAR)
        }
        date.month() == now.month() -> {
            if (date.day() < now.day()) {
                this.age++
                date.set(Calendar.YEAR, now.year() + 1)
                this.daysLeft += now.getActualMaximum(Calendar.DAY_OF_YEAR)
            }
        }
    }

    return this
}

suspend fun sortWithDividers(events: List<Event>): List<Event> = withContext(Dispatchers.IO) {
    val sorted = events.sortedBy { it.daysLeft }
    val result = mutableListOf<Event>()

    var insert = 0
    result.add(insert++, Event(sorted.first().monthNumber()))
    for (i in 0..(sorted.size - 2)) {
        result.add(insert++, sorted[i])
        val current = sorted[i].monthNumber()
        val next = sorted[i + 1].monthNumber()
        if (current != next) result.add(insert++, Event(next))
    }
    result.add(insert, sorted.last())

    result
}

fun Long.formatDate(pattern: String = "d MMMM y", withoutYear: Boolean = false): String {
    return try {
        val date = Date(this)
        var formatter = SimpleDateFormat(pattern, Locale.getDefault())
        if (withoutYear) formatter = SimpleDateFormat(
            pattern.substring(0, pattern.length - 5),
            Locale.getDefault()
        )
        formatter.format(date)
    } catch (ex: Exception) {
        ex.printStackTrace()
        "-"
    }
}

fun Long.toCalendar(year: Int = Calendar.getInstance().year()): Calendar {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    calendar.set(Calendar.YEAR, year)
    return calendar
}

fun launchMainScope(block: suspend CoroutineScope.() -> Unit) {
    CoroutineScope(Dispatchers.Main).launch { block() }
}