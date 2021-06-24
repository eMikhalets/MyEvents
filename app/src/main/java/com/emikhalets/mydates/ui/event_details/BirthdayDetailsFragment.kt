package com.emikhalets.mydates.ui.event_details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.emikhalets.mydates.R
import com.emikhalets.mydates.data.database.entities.Event
import com.emikhalets.mydates.databinding.FragmentBirthdayDetailsBinding
import com.emikhalets.mydates.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BirthdayDetailsFragment : Fragment(R.layout.fragment_birthday_details) {

    private val binding by viewBinding(FragmentBirthdayDetailsBinding::bind)
    private val viewModel: EventDetailsVM by viewModels()
    private val args: BirthdayDetailsFragmentArgs by navArgs()

    private lateinit var event: Event

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        insertEventData()
        clickListeners()
        textWatchers()
        observe()
    }

    private fun insertEventData() {
        event = args.event
        binding.apply {
            textFullName.text = event.fullName()
            textInfo.text = getString(
                R.string.birthday_date, event.date.dateFormat("d MMMM")
            )
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
            inputDate.setDate(event.withoutYear)
            checkYear.isChecked = event.withoutYear
            if (event.withoutYear) textAge.visibility = View.GONE
            else textAge.visibility = View.VISIBLE
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun clickListeners() {
        binding.apply {
            inputDate.setOnDrawableEndClick {
                startDatePickerDialog(event.date) { timestamp ->
                    applyNewDate(timestamp)
                    binding.inputDate.setDate(binding.checkYear.isChecked)
                    btnSave.isEnabled = true
                }
            }
            checkYear.setOnCheckedChangeListener { _, isChecked ->
                event.withoutYear = isChecked
                binding.inputDate.setDate(isChecked)
                btnSave.isEnabled = true
            }
            btnDelete.setOnClickListener {
                startDeleteDialog(getString(R.string.dialog_delete_event)) {
                    viewModel.deleteEvent(event)
                }
            }
            btnSave.setOnClickListener {
                if (validateFields()) viewModel.updateEvent(event)
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
        viewModel.eventUpdate.observe(viewLifecycleOwner) { toast(R.string.event_saved) }
        viewModel.eventDelete.observe(viewLifecycleOwner) { navigateBack() }
    }

    private fun validateFields(): Boolean {
        return binding.inputName.text.toString().isNotEmpty()
    }

    private fun EditText.setDate(withoutYear: Boolean) {
        if (withoutYear) this.setText(event.date.dateFormat("d MMMM"))
        else this.setText(event.date.dateFormat("d MMMM YYYY"))
    }

    private fun applyNewDate(ts: Long) {
        event.date = ts
        event.calculateParameters()
        binding.apply {
            inputDate.setText(ts.dateFormat("d MMMM YYYY"))
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
}