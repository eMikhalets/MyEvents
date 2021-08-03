package com.emikhalets.mydates.ui.event_details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.emikhalets.mydates.R
import com.emikhalets.mydates.data.database.entities.Event
import com.emikhalets.mydates.databinding.FragmentEventDetailsBinding
import com.emikhalets.mydates.utils.*
import com.emikhalets.mydates.utils.EventType.Companion.getTypeDate
import com.emikhalets.mydates.utils.EventType.Companion.getTypeImageLarge
import com.emikhalets.mydates.utils.EventType.Companion.getTypeName
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EventDetailsFragment : Fragment(R.layout.fragment_event_details) {

    private val binding by viewBinding(FragmentEventDetailsBinding::bind)
    private val viewModel: EventDetailsVM by viewModels()
    private val args: EventDetailsFragmentArgs by navArgs()

    private lateinit var event: Event

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle(R.string.title_event_details)
        insertEventData()
        clickListeners()
        textWatchers()
        observe()
    }

    private fun insertEventData() {
        event = args.event
        binding.apply {
            setViewsForEventType(EventType.get(event.eventType))
            textFullName.text = event.fullName()
            if (event.daysLeft == 0) textDaysLeft.text = getString(R.string.today)
            else textDaysLeft.text = resources.getQuantityString(
                R.plurals.days_left, event.daysLeft, event.daysLeft
            )
            textAge.text = resources.getQuantityString(
                R.plurals.age, event.age, event.age
            )
            inputNotes.setText(event.notes)
            inputName.setText(event.name)
            inputLastname.setText(event.lastName)
            inputMiddleName.setText(event.middleName)
            inputDate.setDate(event.date, event.withoutYear)
            checkYear.isChecked = event.withoutYear
            if (event.withoutYear) textAge.visibility = View.GONE
            else textAge.visibility = View.VISIBLE
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun clickListeners() {
        binding.apply {
            inputDate.setOnClickListener {
                startDatePickerDialog(event.date) { timestamp ->
                    applyNewDate(timestamp)
                    binding.inputDate.setDate(event.date, binding.checkYear.isChecked)
                    btnSave.isEnabled = true
                }
            }
            checkYear.setOnCheckedChangeListener { _, isChecked ->
                event.withoutYear = isChecked
                binding.inputDate.setDate(event.date, isChecked)
                btnSave.isEnabled = true
            }
            btnDelete.setOnClickListener {
                startDeleteDialog(getString(R.string.dialog_delete_event)) {
                    viewModel.deleteEvent(event)
                }
            }
            btnSave.setOnClickListener {
                onSaveClick()
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

    private fun onSaveClick() {
        binding.layInputName.error = null
        val name = binding.inputName.text.toString()
        val lastname = binding.inputLastname.text.toString()
        val middleName = binding.inputMiddleName.text.toString()
        val withoutYear = binding.checkYear.isChecked
        viewModel.updateEvent(
            eventType = EventType.get(event.eventType),
            name = name,
            lastname = lastname,
            middleName = middleName,
            date = event.date,
            withoutYear = withoutYear
        )
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
        binding.textFullName.apply {
            text = eventType.getTypeName(requireContext())
            setDrawableTop(eventType.getTypeImageLarge())
        }

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
                navigateBack()
            }
            EventDetailsState.Saved -> {
                toast(R.string.event_saved)
            }
            EventDetailsState.Init -> {
            }
        }
    }
}