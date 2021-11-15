package com.emikhalets.mydates.utils.extentions

import android.widget.EditText
import android.widget.TextView
import androidx.annotation.DrawableRes

fun TextView.setDrawableTop(@DrawableRes resource: Int) {
    this.setCompoundDrawablesRelativeWithIntrinsicBounds(0, resource, 0, 0)
}

fun TextView.setDrawableStart(@DrawableRes resource: Int) {
    this.setCompoundDrawablesRelativeWithIntrinsicBounds(resource, 0, 0, 0)
}

fun EditText.setDateText(date: Long, withoutYear: Boolean) {
    if (withoutYear) this.setText(date.formatDate("d MMMM"))
    else this.setText(date.formatDate("d MMMM y"))
}