package com.emikhalets.mydates.utils

import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.LifecycleOwner

class DocumentPicker(
    registry: ActivityResultRegistry,
    lifecycleOwner: LifecycleOwner,
    private val onResult: (uri: Uri) -> Unit
) {

    private val openDocument: ActivityResultLauncher<Array<String>> =
        registry.register(
            DOCUMENT_PICKER,
            lifecycleOwner,
            ActivityResultContracts.OpenDocument()
        ) { uri: Uri? -> uri?.let(onResult) }

    fun openFile() {
        openDocument.launch(arrayOf("application/json"))
    }

    private companion object {
        const val DOCUMENT_PICKER = "DocumentPicker"
    }
}