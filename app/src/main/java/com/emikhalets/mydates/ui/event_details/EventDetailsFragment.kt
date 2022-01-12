package com.emikhalets.mydates.ui.event_details

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.emikhalets.mydates.R
import com.emikhalets.mydates.data.database.entities.Event
import com.emikhalets.mydates.databinding.FragmentEventDetailsBinding
import com.emikhalets.mydates.ui.base.BaseFragment
import com.emikhalets.mydates.utils.AppDialogManager
import com.emikhalets.mydates.utils.AppNavigationManager
import com.emikhalets.mydates.utils.AppPermissionManager
import com.emikhalets.mydates.utils.activity_result.ContactPicker
import com.emikhalets.mydates.utils.activity_result.ImagePicker
import com.emikhalets.mydates.utils.activity_result.PhotoTaker
import com.emikhalets.mydates.utils.enums.ContactPickerType
import com.emikhalets.mydates.utils.enums.EventType
import com.emikhalets.mydates.utils.enums.EventType.Companion.getTypeDate
import com.emikhalets.mydates.utils.enums.EventType.Companion.getTypeName
import com.emikhalets.mydates.utils.enums.PhotoPickerType
import com.emikhalets.mydates.utils.extentions.formatDate
import com.emikhalets.mydates.utils.extentions.hideSoftKeyboard
import com.emikhalets.mydates.utils.extentions.setDateText
import com.emikhalets.mydates.utils.extentions.setImageUri
import com.emikhalets.mydates.utils.extentions.toast
import com.emikhalets.mydates.utils.views.CardEditText

class EventDetailsFragment : BaseFragment(R.layout.fragment_event_details) {

    private val binding by viewBinding(FragmentEventDetailsBinding::bind)
    private val viewModel by viewModels<EventDetailsVM> { viewModelFactory }
    private val args: EventDetailsFragmentArgs by navArgs()
    private lateinit var imagePicker: ImagePicker
    private lateinit var photoTaker: PhotoTaker
    private lateinit var contactPicker: ContactPicker
    private lateinit var contactsAdapter: ContactsAdapter

    private lateinit var event: Event

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state.observe(viewLifecycleOwner) { renderState(it) }

        imagePicker = ImagePicker(this) { uri -> insertImage(uri) }
        photoTaker = PhotoTaker(this) { uri -> insertImage(uri) }
        contactPicker = ContactPicker(this) { contact -> viewModel.addContact(contact) }

        insertEventData()
        listeners()
    }

    private fun insertEventData() {
        event = args.event
        binding.apply {
            setViewsForEventType(EventType.get(event.eventType))
            imagePhoto.setImageUri(event.imageUri, requireActivity().contentResolver)
            textFullName.text = event.fullName()
            textDaysLeft.text = setDaysLeft(event.daysLeft)
            textAge.text = resources.getQuantityString(
                R.plurals.age, event.age, event.age
            )
            inputNotes.text = event.notes
            inputName.text = event.name
            inputLastname.text = event.lastName
            inputMiddleName.text = event.middleName
            inputDate.setDateText(event.date, event.withoutYear)
            checkYear.isChecked = event.withoutYear
            textDate.isGone = event.withoutYear

            contactsAdapter = ContactsAdapter(
                phoneClick = { invokePhoneCall(it) },
                smsClick = { invokeSendSms(it) },
                deleteClick = { viewModel.removeContact(it) },
            )
            layoutContacts.listContacts.adapter = contactsAdapter
            if (event.contacts.isNotEmpty()) {
                contactsAdapter.submitList(event.contacts)
                viewModel.contacts.addAll(event.contacts)
            }
        }
    }

    private fun listeners() {
        binding.apply {
            inputNotes.doAfterTextChanged {
                event.notes = it.toString()
                btnSave.isEnabled = true
            }

            inputName.doAfterTextChanged {
                event.name = it.toString()
                btnSave.isEnabled = true
            }

            inputLastname.doAfterTextChanged {
                event.lastName = it.toString()
                btnSave.isEnabled = true
            }

            inputMiddleName.doAfterTextChanged {
                event.middleName = it.toString()
                btnSave.isEnabled = true
            }

            cardPhoto.setOnClickListener {
                AppDialogManager.showPhotoPicker(requireContext()) {
                    when (it) {
                        PhotoPickerType.TAKE_PHOTO -> photoTaker.takePhoto()
                        PhotoPickerType.SELECT_IMAGE -> imagePicker.getImage()
                    }
                }
            }

            inputDate.setOnClickListener {
                AppDialogManager.showDatePicker(requireContext(), event.date) { timestamp ->
                    applyNewDate(timestamp)
                    inputDate.setDateText(event.date, checkYear.isChecked)
                    btnSave.isEnabled = true
                }
            }

            checkYear.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) textAge.visibility = View.GONE
                else textAge.visibility = View.VISIBLE
                event.withoutYear = isChecked
                inputDate.setDateText(event.date, isChecked)
                btnSave.isEnabled = true
            }

            layoutContacts.textAddContact.setOnClickListener {
                AppDialogManager.showContactPicker(requireContext()) { type, contact ->
                    when (type) {
                        ContactPickerType.SELF_INPUT -> {
                            viewModel.addContact(contact)
                        }
                        ContactPickerType.SELECTION -> {
                            if (AppPermissionManager.isContactsGranted(requireContext())) {
                                contactPicker.pickContact()
                            } else {
                                toast(getString(R.string.no_contacts_permission))
                            }
                        }
                    }
                }
            }

            btnSave.setOnClickListener {
                hideSoftKeyboard()
                inputName.error = null
                viewModel.updateEvent(event)
            }

            btnDelete.setOnClickListener {
                hideSoftKeyboard()
                AppDialogManager.showDeleteEvent(
                    context = requireContext(),
                    content = getString(R.string.dialog_delete_event),
                    callback = { viewModel.deleteEvent(event) }
                )
            }
        }
    }

    private fun applyNewDate(ts: Long) {
        event.date = ts
        event.calculateParameters()
        binding.apply {
            inputDate.text = ts.formatDate("d MMMM YYYY")
            textDaysLeft.text = setDaysLeft(event.daysLeft)
            textAge.text = resources.getQuantityString(
                R.plurals.age, event.age, event.age
            )
            btnSave.isEnabled = true
        }
    }

    private fun setDaysLeft(daysLeft: Int): String {
        return if (daysLeft == 0) {
            getString(R.string.today)
        } else {
            resources.getQuantityString(R.plurals.days_left, daysLeft, daysLeft)
        }
    }

    private fun setViewsForEventType(eventType: EventType) {
        binding.textDate.text = eventType.getTypeDate(requireContext(), event.date)
        binding.textFullName.text = eventType.getTypeName(requireContext())

        if (eventType == EventType.ANNIVERSARY) {
            binding.apply {
                inputLastname.visibility = View.GONE
                inputMiddleName.visibility = View.GONE
            }
        }
    }

    private fun renderState(state: EventDetailsState) {
        when (state) {
            EventDetailsState.Init -> Unit
            is EventDetailsState.Error -> toast(state.message)
            is EventDetailsState.EmptyNameError -> setEmptyError(binding.inputName)
            EventDetailsState.Deleted -> AppNavigationManager.back(this)
            EventDetailsState.Saved -> toast(R.string.event_saved)
            EventDetailsState.ContactAlreadyAdded -> toast(R.string.contact_already_added)
            is EventDetailsState.ContactsChanged -> onContactChanged(state.contacts)
        }
    }

    private fun setEmptyError(view: CardEditText) {
        view.error = getString(R.string.required_field)
    }

    private fun onContactChanged(contacts: List<String>) {
        contactsAdapter.submitList(contacts.toMutableList())
        event.contacts = contacts
    }

    private fun insertImage(uri: Uri) {
        lifecycleScope.launchWhenCreated {
            event.imageUri = uri.toString()
            binding.imagePhoto.load(uri)
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun invokePhoneCall(phone: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phone")
        }
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(intent)
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun invokeSendSms(phone: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            data = Uri.parse("address:$phone")
            type = "vnd.android-dir/mms-sms"
        }
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(intent)
        }
    }
}