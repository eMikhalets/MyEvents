package com.emikhalets.mydates.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.emikhalets.mydates.data.database.entities.Event
import com.emikhalets.mydates.ui.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

const val APP_EVENTS_ALARM_REQUEST_CODE = 7
const val APP_UPDATE_ALARM_REQUEST_CODE = 8
const val EVENTS_UPDATE_HOUR = 0
const val EVENTS_UPDATE_MINUTE = 10

const val APP_SHARED_PREFERENCES = "my_dates_shared_preferences"
const val APP_SP_UPDATE_EVENTS_TIME = "my_dates_sp_update_event_time"
const val APP_SP_EVENT_HOUR = "my_dates_sp_event_hour"
const val APP_SP_EVENT_MINUTE = "my_dates_sp_event_minute"
const val APP_SP_NOTIF_ALL_FLAG = "my_dates_sp_notif_all_flag"
const val APP_SP_NOTIF_MONTH_FLAG = "my_dates_sp_notif_month_flag"
const val APP_SP_NOTIF_WEEK_FLAG = "my_dates_sp_notif_week_flag"
const val APP_SP_NOTIF_TWO_DAY_FLAG = "my_dates_sp_two_day_month_flag"
const val APP_SP_NOTIF_DAY_FLAG = "my_dates_sp_notif_day_flag"
const val APP_SP_NOTIF_TODAY_FLAG = "my_dates_sp_notif_today_flag"

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

fun Fragment.toast(@StringRes resource: Int) =
    Toast.makeText(requireContext(), resource, Toast.LENGTH_SHORT).show()

fun Fragment.toast(message: String) =
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

fun Long.toCalendar(year: Int = Calendar.getInstance().year()): Calendar {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    calendar.set(Calendar.YEAR, year)
    return calendar
}

fun Calendar.day() = this.get(Calendar.DAY_OF_MONTH)
fun Calendar.dayOfYear() = this.get(Calendar.DAY_OF_YEAR)
fun Calendar.month() = this.get(Calendar.MONTH)
fun Calendar.year() = this.get(Calendar.YEAR)

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

fun Event.monthNumber(): Int {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this.date
    return calendar.month()
}

fun Fragment.hideSoftKeyboard() {
    val inputMethodManager =
        activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    if (inputMethodManager.isAcceptingText) {
        inputMethodManager.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
    }
}

fun Fragment.setTitle(@StringRes stringRes: Int) {
    (activity as MainActivity).supportActionBar?.title = getString(stringRes)
}