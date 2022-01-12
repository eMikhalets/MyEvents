package com.emikhalets.mydates.utils.activity_result

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.*

class DocumentCreator(
    fragment: Fragment,
    private val onResult: (uri: Uri) -> Unit,
) {

    private val createDocument: ActivityResultLauncher<String> =
        fragment.requireActivity().activityResultRegistry.register(
            "create_document",
            fragment.viewLifecycleOwner,
            CreateFileContract()
        ) { uri: Uri? -> uri?.let(onResult) }

    fun createFile() {
        createDocument.launch("")
    }

    class CreateFileContract : ActivityResultContract<String, Uri?>() {

        override fun createIntent(context: Context, input: String): Intent {
            return Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                type = "application/json"
                putExtra(Intent.EXTRA_TITLE, getFileName())
            }
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
            if (intent == null || resultCode != Activity.RESULT_OK) return null
            return intent.data
        }

        private fun getFileName(): String {
            val formatter = SimpleDateFormat("yyyy_MM_dd_hh_mm_ss", Locale.getDefault())
            val name = "MyEvents_Backup_${formatter.format(Date())}"
            val suffix = ".json"
            return "$name$suffix"
        }
    }
}