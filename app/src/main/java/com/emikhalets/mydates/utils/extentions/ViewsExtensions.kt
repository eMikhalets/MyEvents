package com.emikhalets.mydates.utils.extentions

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import com.emikhalets.mydates.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.FileNotFoundException
import java.util.*
import kotlin.math.max

fun TextView.setDrawableStart(@DrawableRes resource: Int) {
    this.setCompoundDrawablesRelativeWithIntrinsicBounds(resource, 0, 0, 0)
}

fun EditText.setDateText(date: Long, withoutYear: Boolean) {
    if (withoutYear) this.setText(date.formatDate("d MMMM"))
    else this.setText(date.formatDate("d MMMM y"))
}

fun ImageView.setImageUri(uri: String?, contentResolver: ContentResolver) {
    CoroutineScope(Dispatchers.IO).launch {
        if (uri.isNullOrEmpty()) {
            withContext(Dispatchers.Main) {
                setImageResource(R.drawable.ic_photo)
            }
        } else {
            try {
                val validUri = Uri.parse(uri)
                // TODO image decoding is too long. Need to create and save compressed photos
                val original = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    val source = ImageDecoder.createSource(contentResolver, validUri)
                    ImageDecoder.decodeBitmap(source)
                } else {
                    MediaStore.Images.Media.getBitmap(contentResolver, validUri)
                }
                val measure = max(original.width, original.height) / 300
                val bitmap = if (measure > 1) {
                    Bitmap.createScaledBitmap(
                        original, original.width / measure,
                        original.height / measure, true
                    )
                } else {
                    original
                }
                withContext(Dispatchers.Main) {
                    setImageBitmap(bitmap)
                }
            } catch (ex: FileNotFoundException) {
                withContext(Dispatchers.Main) {
                    setImageResource(R.drawable.ic_photo)
                }
            }
        }
    }
}