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

fun DateItem.computeDaysLeft() {
    val now = Calendar.getInstance()
    val date = Calendar.getInstance()
    date.timeInMillis = this.date
    date.set(Calendar.YEAR, now.get(Calendar.YEAR))
    var remaining = now.get(Calendar.DAY_OF_YEAR) - date.get(Calendar.DAY_OF_YEAR)
    if (remaining < 0) remaining += now.getActualMaximum(Calendar.DAY_OF_YEAR)
    this.daysLeft = remaining
}

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