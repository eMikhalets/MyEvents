package com.emikhalets.mydates.utils.activity_result

import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment

class DocumentPicker(
    fragment: Fragment,
    private val onResult: (uri: Uri) -> Unit,
) {

    private val openDocument: ActivityResultLauncher<Array<String>> =
        fragment.requireActivity().activityResultRegistry.register(
            "pick_document",
            fragment.viewLifecycleOwner,
            ActivityResultContracts.OpenDocument()
        ) { uri: Uri? -> uri?.let(onResult) }

    fun openFile() {
        openDocument.launch(
            arrayOf(
                "application/json",
                "text/plain",
            )
        )
    }
}