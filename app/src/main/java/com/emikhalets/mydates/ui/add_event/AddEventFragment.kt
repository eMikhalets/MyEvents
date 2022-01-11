package com.emikhalets.mydates.ui.add_event

import android.annotation.SuppressLint
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
import com.emikhalets.mydates.utils.extentions.*
import com.emikhalets.mydates.utils.views.CardEditText

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

        binding.inputDate.text = viewModel.date.formatDate()
        binding.textTitle.apply {
            text = args.eventType.getTypeName(requireContext())
            setDrawableStart(args.eventType.getTypeImage())
        }

        if (args.eventType == EventType.ANNIVERSARY) {
            binding.apply {
                inputLastname.visibility = View.GONE
                inputMiddleName.visibility = View.GONE
            }
        }

        contactsAdapter = ContactsAdapter(
            deleteClick = { viewModel.removeContact(it) }
        )
        binding.layoutContacts.listContacts.adapter = contactsAdapter

        clickListeners()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun clickListeners() {
        binding.cardPhoto.setOnClickListener {
            AppDialogManager.showPhotoPicker(requireContext()) {
                when (it) {
                    PhotoPickerType.TAKE_PHOTO -> photoTaker.takePhoto()
                    PhotoPickerType.SELECT_IMAGE -> imagePicker.getImage()
                }
            }
        }

        binding.inputDate.setOnClickListener {
            AppDialogManager.showDatePickerDialog(requireContext(), viewModel.date) { timestamp ->
                viewModel.date = timestamp
                binding.inputDate.setDateText(viewModel.date, binding.checkYear.isChecked)
            }
        }

        binding.checkYear.setOnCheckedChangeListener { _, isChecked ->
            binding.inputDate.setDateText(viewModel.date, isChecked)
        }

        binding.btnSave.setOnClickListener {
            binding.inputName.error = null
            viewModel.saveNewEvent(
                eventType = args.eventType,
                name = binding.inputName.text,
                lastname = binding.inputLastname.text,
                middleName = binding.inputMiddleName.text,
                withoutYear = binding.checkYear.isChecked,
                imageUri = imageUri
            )
        }

        binding.root.setOnTouchListener { _, _ ->
            hideSoftKeyboard()
            clearFocus()
            false
        }

        binding.layoutContacts.textAddContact.setOnClickListener {
            AppDialogManager.showContactPicker(requireContext()) { type, contact ->
                when (type) {
                    ContactPickerType.SELF_INPUT -> {
                        viewModel.addContact(contact)
                    }
                    ContactPickerType.SELECTION -> {
                        if (AppPermissionManager.isContactsGranted(requireContext())) {
                            contactPicker.pickContact()
                        } else {
                            // TODO move to strings
                            toast("Нет разрешения на чтение контактов")
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
            AddEventState.ContactAlreadyAdded -> toast(R.string.contact_already_added)
            is AddEventState.ContactsChanged -> setNewContactsList(state.contacts)
            is AddEventState.Error -> toast(state.message)
            AddEventState.EmptyNameError -> setEmptyError(binding.inputName)
        }
    }

    private fun setNewContactsList(contacts: List<String>) {
        contactsAdapter.submitList(contacts.toMutableList())
    }

    private fun setEmptyError(view: CardEditText) {
        view.error = getString(R.string.required_field)
    }

    private fun clearFocus() {
        binding.apply {
            inputName.clearFocus()
            inputLastname.clearFocus()
            inputMiddleName.clearFocus()
        }
    }

    private fun insertImage(uri: Uri) {
        imageUri = uri.toString()
        binding.imagePhoto.load(uri)
    }
}