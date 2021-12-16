package com.emikhalets.mydates.utils.activity_result

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.LifecycleOwner

class PhotoTaker(
    registry: ActivityResultRegistry,
    lifecycleOwner: LifecycleOwner,
    private val context: Context,
    private val contentResolver: ContentResolver,
    private val onResult: (uri: Uri) -> Unit,
) {

    private var originalUri: Uri? = null

    private val getContent: ActivityResultLauncher<Uri?> =
        registry.register(
            "take_photo",
            lifecycleOwner,
            ActivityResultContracts.TakePicture()
        ) { success ->
            if (success && originalUri != null) {
                CacheImageHelper.saveCachePhoto(originalUri!!, context, contentResolver)?.let {
                    onResult(it)
                }
            }
        }

    fun takePhoto() {
        originalUri = CacheImageHelper.getNewImageUri(contentResolver)
        getContent.launch(originalUri)
    }
}