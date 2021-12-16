package com.emikhalets.mydates.utils.activity_result

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.LifecycleOwner

class ImagePicker(
    registry: ActivityResultRegistry,
    lifecycleOwner: LifecycleOwner,
    private val context: Context,
    private val contentResolver: ContentResolver,
    private val onResult: (uri: Uri) -> Unit,
) {

    private val getContent: ActivityResultLauncher<Array<String>> =
        registry.register(
            "image_picker",
            lifecycleOwner,
            ActivityResultContracts.OpenDocument()
        ) { uri: Uri? ->
            uri?.let {
                contentResolver.takePersistableUriPermission(uri, getPermissionFlag())
                CacheImageHelper.saveCachePhoto(uri, context, contentResolver)?.let {
                    onResult(it)
                }
            }
        }

    fun getImage() {
        getContent.launch(arrayOf("image/*"))
    }

    private fun getPermissionFlag(): Int {
        return Intent.FLAG_GRANT_READ_URI_PERMISSION
    }
}