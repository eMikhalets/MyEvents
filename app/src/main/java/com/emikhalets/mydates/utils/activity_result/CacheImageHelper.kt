package com.emikhalets.mydates.utils.activity_result

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

object CacheImageHelper {

    fun getNewImageUri(contentResolver: ContentResolver): Uri? {
        val filename = getPhotoFileName()
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                put(MediaStore.MediaColumns.RELATIVE_PATH, getPhotosDirectory())
            }
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        } else {
            val directory = Environment.getExternalStoragePublicDirectory(getPhotosDirectory())
            val file = File(directory, filename)
            Uri.fromFile(file)
        }
    }

    fun saveCachePhoto(originalUri: Uri, context: Context, contentResolver: ContentResolver): Uri? {
        val file = File(context.externalCacheDir, getPhotoFileName())
        val fileUri = Uri.fromFile(file)

        val bytes = ByteArrayOutputStream().run {
            getBitmapByUri(originalUri, contentResolver)
                .compress(Bitmap.CompressFormat.JPEG, 10, this)
            this.toByteArray()
        }

        context.contentResolver.openFileDescriptor(fileUri, "w")?.use { descriptor ->
            FileOutputStream(descriptor.fileDescriptor).use { stream ->
                stream.write(bytes)
            }
        }

        return fileUri
    }

    private fun getBitmapByUri(uri: Uri, contentResolver: ContentResolver): Bitmap {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap(contentResolver, uri)
        }
    }

    private fun getPhotoFileName(): String {
        val formatter = SimpleDateFormat("yyyy_MM_dd_hh_mm_ss", Locale.getDefault())
        val name = "IMAGE_${formatter.format(Date())}"
        val suffix = ".jpg"
        return "$name$suffix"
    }

    private fun getPhotosDirectory(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            "${Environment.DIRECTORY_PICTURES}/MyEvents"
        } else {
            "MyEvents/${Environment.DIRECTORY_PICTURES}"
        }
    }
}