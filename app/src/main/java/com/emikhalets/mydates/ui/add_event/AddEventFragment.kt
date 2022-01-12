package com.emikhalets.mydates.ui.add_event

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.emikhalets.mydates.R
import com.emikhalets.mydates.databinding.FragmentAddEventBinding
import com.emikhalets.mydates.ui.base.BaseFragment
import com.emikhalets.mydates.utils.AppDialogManager
import com.emikhalets.mydates.utils.AppNavigationManager
import com.emikhalets.mydates.utils.AppPermissionManager
import com.emikhalets.mydates.utils.activity_result.ContactPicker
import com.emikhalets.mydates.utils.activity_result.ImagePicker
import com.emikhalets.mydates.utils.activity_result.PhotoTaker
import com.emikhalets.mydates.utils.enums.ContactPickerType
import com.emikhalets.mydates.utils.enums.EventType
import com.emikhalets.mydates.utils.enums.EventType.Companion.getTypeImage
import com.emikhalets.mydates.utils.enums.EventType.Companion.getTypeName
import com.emikhalets.mydates.utils.enums.PhotoPickerType
import com.emikhalets.mydates.utils.extentions.formatDate
import com.emikhalets.mydates.utils.extentions.hideSoftKeyboard
import com.emikhalets.mydates.utils.extentions.setDateText
import com.emikhalets.mydates.utils.extentions.setDrawableStart
import com.emikhalets.mydates.utils.extentions.toast

class AddEventFragment : BaseFragment(R.layout.fragment_add_event) {

    private val binding by viewBinding(FragmentAddEventBinding::bind)
    private val viewModel by viewModels<AddEventVM> { viewModelFactory }
    private val args: AddEventFragmentArgs by navArgs()
    private var imageUri: String = ""
    private lateinit var imagePicker: ImagePicker
    private lateinit var photoTaker: PhotoTaker
    private lateinit var contactPicker: ContactPicker
    private lateinit var contactsAdapter: ContactsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state.observe(viewLifecycleOwner) { renderState(it) }

        imagePicker = ImagePicker(this) { uri -> insertImage(uri) }
        photoTaker = PhotoTaker(this) { uri -> insertImage(uri) }
        contactPicker = ContactPicker(this) { contact -> viewModel.addContact(contact) }

        prepareViews()
        listeners()
    }

    private fun prepareViews() {
        binding.apply {
            inputDate.text = viewModel.date.formatDate()
            textTitle.apply {
                text = args.eventType.getTypeName(requireContext())
                setDrawableStart(args.eventType.getTypeImage())
            }

            if (args.eventType == EventType.ANNIVERSARY) {
                inputLastname.visibility = View.GONE
                inputMiddleName.visibility = View.GONE
            }

            contactsAdapter = ContactsAdapter(
                deleteClick = { viewModel.removeContact(it) }
            )
            layoutContacts.listContacts.adapter = contactsAdapter
        }
    }

    private fun listeners() {
        binding.apply {
            cardPhoto.setOnClickListener {
                AppDialogManager.showPhotoPicker(requireContext()) {
                    when (it) {
                        PhotoPickerType.TAKE_PHOTO -> photoTaker.takePhoto()
                        PhotoPickerType.SELECT_IMAGE -> imagePicker.getImage()
                    }
                }
            }

            inputName.doAfterTextChanged {
                inputName.error = null
            }

            inputDate.setOnClickListener {
                AppDialogManager.showDatePicker(requireContext(), viewModel.date) { timestamp ->
                    viewModel.date = timestamp
                    inputDate.setDateText(viewModel.date, checkYear.isChecked)
                }
            }

            checkYear.setOnCheckedChangeListener { _, isChecked ->
                inputDate.setDateText(viewModel.date, isChecked)
            }

            btnSave.setOnClickListener {
                hideSoftKeyboard()
                viewModel.saveNewEvent(
                    eventType = args.eventType,
                    name = inputName.text,
                    lastname = inputLastname.text,
                    middleName = inputMiddleName.text,
                    withoutYear = checkYear.isChecked,
                    imageUri = imageUri
                )
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
        }
    }

    private fun renderState(state: AddEventState) {
        when (state) {
            AddEventState.Init -> Unit
            AddEventState.Added -> AppNavigationManager.back(this)
            AddEventState.EmptyNameError -> setNameEmptyError()
            AddEventState.ContactAlreadyAdded -> toast(R.string.contact_already_added)
            is AddEventState.ContactsChanged -> setNewContactsList(state.contacts)
            is AddEventState.Error -> toast(state.message)
        }
    }

    private fun setNewContactsList(contacts: List<String>) {
        contactsAdapter.submitList(contacts.toMutableList())
    }

    private fun setNameEmptyError() {
        binding.inputName.error = getString(R.string.required_field)
    }

    private fun insertImage(uri: Uri) {
        imageUri = uri.toString()
        binding.imagePhoto.load(uri)
    }
}