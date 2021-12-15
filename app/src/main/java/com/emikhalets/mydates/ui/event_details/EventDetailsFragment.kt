package com.emikhalets.mydates.ui.event_details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.emikhalets.mydates.R
import com.emikhalets.mydates.data.database.entities.Event
import com.emikhalets.mydates.databinding.FragmentEventDetailsBinding
import com.emikhalets.mydates.ui.base.BaseFragment
import com.emikhalets.mydates.utils.AppDialogManager
import com.emikhalets.mydates.utils.AppNavigationManager
import com.emikhalets.mydates.utils.activity_result.ImagePicker
import com.emikhalets.mydates.utils.enums.EventType
import com.emikhalets.mydates.utils.enums.EventType.Companion.getTypeDate
import com.emikhalets.mydates.utils.enums.EventType.Companion.getTypeName
import com.emikhalets.mydates.utils.extentions.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class EventDetailsFragment : BaseFragment(R.layout.fragment_event_details) {

    private val binding by viewBinding(FragmentEventDetailsBinding::bind)
    private val viewModel by viewModels<EventDetailsVM> { viewModelFactory }
    private val args: EventDetailsFragmentArgs by navArgs()
    private lateinit var imagePicker: ImagePicker

    private lateinit var event: Event

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initActivityResult()
        insertEventData()
        clickListeners()
        textWatchers()
        observe()
    }

    private fun initActivityResult() {
        imagePicker = ImagePicker(
            registry = requireActivity().activityResultRegistry,
            lifecycleOwner = viewLifecycleOwner,
            contentResolver = requireActivity().contentResolver,
            onResult = { uri ->
                event.imageUri = uri.toString()
                binding.imagePhoto.setImageURI(uri)
            }
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
            inputNotes.setText(event.notes)
            inputName.setText(event.name)
            inputLastname.setText(event.lastName)
            inputMiddleName.setText(event.middleName)
            inputDate.setDateText(event.date, event.withoutYear)
            checkYear.isChecked = event.withoutYear
            textDate.isGone = event.withoutYear
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun clickListeners() {
        binding.apply {
            cardPhoto.setOnClickListener {
                imagePicker.getImage()
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
                binding.layInputName.error = null
                viewModel.updateEvent(event)
            }
            root.setOnTouchListener { _, _ ->
                hideSoftKeyboard()
                clearFocus()
                false
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

    private fun observe() {
        lifecycleScope.launch {
            viewModel.state.collect { renderState(it) }
        }
    }

    private fun applyNewDate(ts: Long) {
        event.date = ts
        event.calculateParameters()
        binding.apply {
            inputDate.setText(ts.formatDate("d MMMM YYYY"))
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
                cardLastname.visibility = View.GONE
                cardMiddleName.visibility = View.GONE
            }
        }
    }

    private fun renderState(state: EventDetailsState) {
        when (state) {
            is EventDetailsState.Error -> {
                toast(state.message)
            }
            is EventDetailsState.EmptyNameError -> {
                binding.layInputName.error = getString(R.string.required_field)
            }
            EventDetailsState.Deleted -> {
                AppNavigationManager.back(this)
            }
            EventDetailsState.Saved -> {
                toast(R.string.event_saved)
            }
            EventDetailsState.Init -> {
            }
        }
    }
}