package com.emikhalets.mydates.utils.extentions

import android.content.ContentResolver
import android.net.Uri
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import com.emikhalets.mydates.R
import java.io.FileNotFoundException

fun TextView.setDrawableStart(@DrawableRes resource: Int) {
    this.setCompoundDrawablesRelativeWithIntrinsicBounds(resource, 0, 0, 0)
}

fun EditText.setDateText(date: Long, withoutYear: Boolean) {
    if (withoutYear) this.setText(date.formatDate("d MMMM"))
    else this.setText(date.formatDate("d MMMM y"))
}

fun ImageView.setImageUri(uri: String?, contentResolver: ContentResolver) {
    if (uri.isNullOrEmpty()) {
        setImageResource(R.drawable.ic_photo)
    } else {
        try {
            val validUri = Uri.parse(uri)
            // Detect is file exist
            val stream = contentResolver.openInputStream(validUri)
            stream?.close()
            setImageURI(validUri)
        } catch (ex: FileNotFoundException) {
            setImageResource(R.drawable.ic_photo)
        }
    }
}