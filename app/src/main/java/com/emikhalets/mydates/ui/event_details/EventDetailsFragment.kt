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
import com.emikhalets.mydates.utils.activity_result.ContactPicker
import com.emikhalets.mydates.utils.activity_result.ImagePicker
import com.emikhalets.mydates.utils.activity_result.PhotoTaker
import com.emikhalets.mydates.utils.enums.ContactPickerType
import com.emikhalets.mydates.utils.enums.EventType
import com.emikhalets.mydates.utils.enums.EventType.Companion.getTypeDate
import com.emikhalets.mydates.utils.enums.EventType.Companion.getTypeName
import com.emikhalets.mydates.utils.enums.PhotoPickerType
import com.emikhalets.mydates.utils.extentions.*

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
        initActivityResult()
        insertEventData()
        clickListeners()
        textWatchers()
    }

    private fun initActivityResult() {
        imagePicker = ImagePicker(
            registry = requireActivity().activityResultRegistry,
            lifecycleOwner = viewLifecycleOwner,
            context = requireContext(),
            contentResolver = requireActivity().contentResolver,
            onResult = { uri -> insertImage(uri) }
        )
        photoTaker = PhotoTaker(
            registry = requireActivity().activityResultRegistry,
            lifecycleOwner = viewLifecycleOwner,
            context = requireContext(),
            contentResolver = requireActivity().contentResolver,
            onResult = { uri -> insertImage(uri) }
        )
        contactPicker = ContactPicker(
            registry = requireActivity().activityResultRegistry,
            lifecycleOwner = viewLifecycleOwner,
            contentResolver = requireActivity().contentResolver,
            onResult = { contact -> viewModel.addContact(contact) }
        )
    }

    private fun insertEventData() {
        event = args.event
        binding.apply {
            setViewsForEventType(EventType.get(event.eventType))
            imagePhoto.setImageUri(event.imageUri, requireActivity().contentResolver)
            textFullName.text = event.fullName()
            textDaysLeft.text = if (event.daysLeft == 0) {
                getString(R.string.today)
            } else {
                resources.getQuantityString(
                    R.plurals.days_left, event.daysLeft, event.daysLeft
                )
            }
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

    @SuppressLint("ClickableViewAccessibility")
    private fun clickListeners() {
        binding.apply {
            cardPhoto.setOnClickListener {
                AppDialogManager.showPhotoPicker(requireContext()) {
                    when (it) {
                        PhotoPickerType.TAKE_PHOTO -> photoTaker.takePhoto()
                        PhotoPickerType.SELECT_IMAGE -> imagePicker.getImage()
                    }
                }
            }
            inputDate.setOnClickListener {
                AppDialogManager.showDatePickerDialog(requireContext(), event.date) { timestamp ->
                    applyNewDate(timestamp)
                    binding.inputDate.setDateText(event.date, binding.checkYear.isChecked)
                    btnSave.isEnabled = true
                }
            }
            checkYear.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) textAge.visibility = View.GONE
                else textAge.visibility = View.VISIBLE
                event.withoutYear = isChecked
                binding.inputDate.setDateText(event.date, isChecked)
                btnSave.isEnabled = true
            }
            btnDelete.setOnClickListener {
                AppDialogManager.showDeleteDialog(
                    requireContext(),
                    getString(R.string.dialog_delete_event)
                ) {
                    viewModel.deleteEvent(event)
                }
            }
            btnSave.setOnClickListener {
                binding.inputName.error = null
                viewModel.updateEvent(event)
            }
            root.setOnTouchListener { _, _ ->
                hideSoftKeyboard()
                clearFocus()
                false
            }

            binding.layoutContacts.textAddContact.setOnClickListener {
                AppDialogManager.showContactPicker(requireContext()) { type, contact ->
                    when (type) {
                        ContactPickerType.SELF_INPUT -> viewModel.addContact(contact)
                        ContactPickerType.SELECTION -> contactPicker.pickContact()
                    }
                }
            }
        }
    }

    private fun textWatchers() {
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
        }
    }

    private fun applyNewDate(ts: Long) {
        event.date = ts
        event.calculateParameters()
        binding.apply {
            inputDate.text = ts.formatDate("d MMMM YYYY")
            if (event.daysLeft == 0) textDaysLeft.text = getString(R.string.today)
            else textDaysLeft.text = resources.getQuantityString(
                R.plurals.days_left, event.daysLeft, event.daysLeft
            )
            textAge.text = resources.getQuantityString(
                R.plurals.age, event.age, event.age
            )
            btnSave.isEnabled = true
        }
    }

    private fun clearFocus() {
        binding.apply {
            inputName.clearFocus()
            inputLastname.clearFocus()
            inputMiddleName.clearFocus()
            inputNotes.clearFocus()
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
            is EventDetailsState.Error -> {
                toast(state.message)
            }
            is EventDetailsState.EmptyNameError -> {
                binding.inputName.error = getString(R.string.required_field)
            }
            EventDetailsState.Deleted -> {
                AppNavigationManager.back(this)
            }
            EventDetailsState.Saved -> {
                toast(R.string.event_saved)
            }
            EventDetailsState.Init -> {
            }
            EventDetailsState.ContactAlreadyAdded -> toast(R.string.contact_already_added)
            is EventDetailsState.ContactsChanged -> onContactChanged(state.contacts)
        }
    }

    private fun onContactChanged(contacts: List<String>) {
        contactsAdapter.submitList(contacts)
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