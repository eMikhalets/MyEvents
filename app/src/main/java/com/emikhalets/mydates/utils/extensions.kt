package com.emikhalets.mydates.utils

import java.text.SimpleDateFormat
import java.util.*

fun Long.format(): String {
    val date = Date(this)
    val formatter = SimpleDateFormat("d MMMM", Locale.getDefault())
    return formatter.format(date)
}