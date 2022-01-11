package com.emikhalets.mydates.utils.activity_result

import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PhotoTaker(
    private val fragment: Fragment,
    private val onResult: (uri: Uri) -> Unit,
) {

    private var originalUri: Uri? = null

    private val getContent: ActivityResultLauncher<Uri?> =
        fragment.requireActivity().activityResultRegistry.register(
            "take_photo",
            fragment.viewLifecycleOwner,
            ActivityResultContracts.TakePicture()
        ) { success ->
            if (success && originalUri != null) {
                val context = fragment.requireContext()
                val contentResolver = fragment.requireActivity().contentResolver
                CoroutineScope(Dispatchers.IO).launch {
                    CacheImageHelper.saveCachePhoto(originalUri!!, context, contentResolver)?.let {
                        withContext(Dispatchers.Main) {
                            onResult(it)
                        }
                    }
                }
            }
        }

    fun takePhoto() {
        originalUri = CacheImageHelper.getNewImageUri(fragment.requireActivity().contentResolver)
        getContent.launch(originalUri)
    }
}