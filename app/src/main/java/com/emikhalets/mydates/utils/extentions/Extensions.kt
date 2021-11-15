package com.emikhalets.mydates.utils.extentions

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

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

fun launchMainScope(block: suspend CoroutineScope.() -> Unit) {
    CoroutineScope(Dispatchers.Main).launch { block() }
}