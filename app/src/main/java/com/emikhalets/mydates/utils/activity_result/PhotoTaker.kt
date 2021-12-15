package com.emikhalets.mydates.utils.activity_result

import android.content.ContentResolver
import android.content.ContentValues
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.LifecycleOwner
import com.emikhalets.mydates.BuildConfig
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*

class PhotoTaker(
    registry: ActivityResultRegistry,
    lifecycleOwner: LifecycleOwner,
    private val contentResolver: ContentResolver,
    private val onResult: (uri: Uri) -> Unit,
) {

    private val getContent: ActivityResultLauncher<Void?> =
        registry.register(
            "take_photo",
            lifecycleOwner,
            ActivityResultContracts.TakePicturePreview()
        ) { bitmap ->
            bitmap?.let {
                saveBitmapAndGetUri(bitmap)?.let(onResult)
            }
        }

    fun takePhoto() {
        getContent.launch(null)
    }

    private fun saveBitmapAndGetUri(bitmap: Bitmap): Uri? {
        val fileName = "${Date().time}.jpg"
        var imageUri: Uri?
        var stream: OutputStream?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            contentResolver.also { resolver ->
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, getPhotosDirectory())
                }

                imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                stream = imageUri?.let { resolver.openOutputStream(it) }
            }
        } else {
            val imagesDir = Environment.getExternalStoragePublicDirectory(getPhotosDirectory())
            val image = File(imagesDir, fileName)
            stream = FileOutputStream(image)
            imageUri = Uri.fromFile(image)
        }

        // TODO maybe compress in png?
        stream?.use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        }
        stream?.close()
        return imageUri
    }

    private fun getPhotosDirectory(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            "${Environment.DIRECTORY_PICTURES}/MyEvents"
        } else {
            "MyEvents/${Environment.DIRECTORY_PICTURES}"
        }
    }
}