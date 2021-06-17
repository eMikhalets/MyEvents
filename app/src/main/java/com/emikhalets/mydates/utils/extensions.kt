package com.emikhalets.mydates.utils

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.emikhalets.mydates.data.database.entities.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("ClickableViewAccessibility")
inline fun EditText.setOnDrawableEndClick(crossinline callback: () -> Unit) {
    this.setOnTouchListener { v, event ->
        if (event.action == MotionEvent.ACTION_UP) {
            if (event.rawX >= v.right - this.totalPaddingRight) {
                event.action = MotionEvent.ACTION_CANCEL
                callback.invoke()
            }
        }
        true
    }
}

fun Long.dateFormat(pattern: String): String {
    return try {
        val date = Date(this)
        val formatter = SimpleDateFormat(pattern, Locale.getDefault())
        formatter.format(date)
    } catch (ex: Exception) {
        ex.printStackTrace()
        "Error"
    }
}

fun Fragment.toast(resource: Int) =
    Toast.makeText(requireContext(), resource, Toast.LENGTH_SHORT).show()

fun Fragment.toast(message: String) =
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

fun Calendar.day() = this.get(Calendar.DAY_OF_MONTH)
fun Calendar.dayOfYear() = this.get(Calendar.DAY_OF_YEAR)
fun Calendar.month() = this.get(Calendar.MONTH)
fun Calendar.year() = this.get(Calendar.YEAR)

fun Event.calculateParameters() {
    val now = Calendar.getInstance()
    val date = Calendar.getInstance()
    date.timeInMillis = this.date

    this.age = now.year() - date.year()
    date.set(Calendar.YEAR, now.year())
    this.daysLeft = date.dayOfYear() - now.dayOfYear()

    when {
        date.month() < now.month() -> {
            this.age++
            this.daysLeft += now.getActualMaximum(Calendar.DAY_OF_YEAR)
        }
        date.month() == now.month() -> {
            if (date.day() < now.day()) {
                this.age++
                this.daysLeft += now.getActualMaximum(Calendar.DAY_OF_YEAR)
            }
        }
    }
}

suspend fun sortWithDividers(events: List<Event>): List<Event> = withContext(Dispatchers.IO) {
    events.forEach { it.calculateParameters() }
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

fun Event.monthNumber(): Int {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this.date
    return calendar.month()
}