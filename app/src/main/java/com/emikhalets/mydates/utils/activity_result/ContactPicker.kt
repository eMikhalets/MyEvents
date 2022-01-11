package com.emikhalets.mydates.utils.activity_result

import android.provider.ContactsContract
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment

class ContactPicker(
    private val fragment: Fragment,
    private val onResult: (contact: String) -> Unit,
) {

    private val getContent: ActivityResultLauncher<Void?> =
        fragment.requireActivity().activityResultRegistry.register(
            "pick_contact",
            fragment.viewLifecycleOwner,
            ActivityResultContracts.PickContact()
        ) { uri ->
            uri?.let {
                val uriId = uri.toString().split("/").last()
                val idTag = ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                val numberTag = ContactsContract.CommonDataKinds.Phone.NUMBER
                val contentResolver = fragment.requireActivity().contentResolver
                val projection = arrayOf(idTag, numberTag)
                val cursor = contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    projection,
                    null,
                    null,
                    null
                )

                cursor?.moveToFirst()
                while (cursor?.isAfterLast == false) {
                    val idIndex = cursor.getColumnIndex(idTag)
                    val contactId = cursor.getString(idIndex)

                    if (contactId == uriId) {
                        val phoneIndex = cursor.getColumnIndex(numberTag)
                        val phone = cursor.getString(phoneIndex)
                        onResult(phone)
                        break
                    }

                    cursor.moveToNext()
                }
                cursor?.close()
            }
        }

    fun pickContact() {
        getContent.launch(null)
    }
}