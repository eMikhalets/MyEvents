package com.emikhalets.mydates.utils.extentions

import android.content.ContentResolver
import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import coil.load
import com.emikhalets.mydates.R
import com.emikhalets.mydates.utils.views.CardEditText
import java.io.FileNotFoundException

fun TextView.setDrawableStart(@DrawableRes resource: Int) {
    this.setCompoundDrawablesRelativeWithIntrinsicBounds(resource, 0, 0, 0)
}

fun CardEditText.setDateText(date: Long, withoutYear: Boolean) {
    if (withoutYear) this.text = date.formatDate("d MMMM")
    else this.text = date.formatDate("d MMMM y")
}

fun ImageView.setImageUri(uri: String?, contentResolver: ContentResolver) {
    if (uri.isNullOrEmpty()) {
        setImageResource(R.drawable.ic_photo)
    } else {
        try {
            val validUri = Uri.parse(uri)
            val stream = contentResolver.openInputStream(validUri)
            stream?.close()
            load(validUri)
        } catch (ex: FileNotFoundException) {
            setImageResource(R.drawable.ic_photo)
        }
    }
}