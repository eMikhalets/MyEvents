package com.emikhalets.mydates.utils.activity_result

import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ImagePicker(
    private val fragment: Fragment,
    private val onResult: (uri: Uri) -> Unit,
) {

    private val getContent: ActivityResultLauncher<Array<String>> =
        fragment.requireActivity().activityResultRegistry.register(
            "image_picker",
            fragment.viewLifecycleOwner,
            ActivityResultContracts.OpenDocument()
        ) { uri: Uri? ->
            uri?.let {
                val context = fragment.requireContext()
                val contentResolver = fragment.requireActivity().contentResolver
                CoroutineScope(Dispatchers.IO).launch {
                    contentResolver.takePersistableUriPermission(uri, getPermissionFlag())
                    CacheImageHelper.saveCachePhoto(uri, context, contentResolver)?.let {
                        withContext(Dispatchers.Main) {
                            onResult(it)
                        }
                    }
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