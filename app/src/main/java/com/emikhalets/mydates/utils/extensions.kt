package com.emikhalets.mydates.utils

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.emikhalets.mydates.data.database.entities.DateItem
import java.text.SimpleDateFormat
import java.util.*

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

fun Fragment.toast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}

fun DateItem.computeDaysLeftAndAge() {
    val now = Calendar.getInstance()
    val date = Calendar.getInstance()
    date.timeInMillis = this.date

    this.day = date.day()
    this.month = date.month()
    this.year = date.year()
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

fun DateItem.computeDaysLeftAndAgeReturn(): DateItem {
    this.computeDaysLeftAndAge()
    return this
}

fun Calendar.day() = this.get(Calendar.DAY_OF_MONTH)
fun Calendar.dayOfYear() = this.get(Calendar.DAY_OF_YEAR)
fun Calendar.month() = this.get(Calendar.MONTH)
fun Calendar.year() = this.get(Calendar.YEAR)

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