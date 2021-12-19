package com.emikhalets.mydates.utils.activity_result

import android.content.ContentResolver
import android.provider.ContactsContract
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.LifecycleOwner

class ContactPicker(
    registry: ActivityResultRegistry,
    lifecycleOwner: LifecycleOwner,
    private val contentResolver: ContentResolver,
    private val onResult: (contact: String) -> Unit,
) {

    private val getContent: ActivityResultLauncher<Void?> =
        registry.register(
            "take_photo",
            lifecycleOwner,
            ActivityResultContracts.PickContact()
        ) { uri ->
            uri?.let {
                val projection = arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER)
                val cursor = contentResolver
                    .query(uri, projection, null, null, null)
                cursor?.moveToFirst()

                if (cursor?.moveToNext() == true) {
                    val phoneIndex = cursor
                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                    val phone = cursor.getString(phoneIndex)
                    onResult(phone)
                }
            }
        }

    fun pickContact() {
        getContent.launch(null)
    }
}