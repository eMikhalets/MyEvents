package com.emikhalets.mydates.utils.activity_result

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContract
import androidx.lifecycle.LifecycleOwner
import java.util.*

class DocumentCreator(
    registry: ActivityResultRegistry,
    lifecycleOwner: LifecycleOwner,
    private val onResult: (uri: Uri) -> Unit,
) {

    private val createDocument: ActivityResultLauncher<String> =
        registry.register(
            DOCUMENT_CREATOR,
            lifecycleOwner,
            CreateFileContract()
        ) { uri: Uri? -> uri?.let(onResult) }

    fun createFile() {
        createDocument.launch("")
    }

    private companion object {
        const val DOCUMENT_CREATOR = "DocumentCreator"
    }

    class CreateFileContract : ActivityResultContract<String, Uri?>() {

        override fun createIntent(context: Context, input: String): Intent {
            return Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                type = "*/json"
                putExtra(Intent.EXTRA_TITLE, "MyEvents_backup_${Date().time}.json")
            }
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
            if (intent == null || resultCode != Activity.RESULT_OK) return null
            return intent.data
        }
    }
}