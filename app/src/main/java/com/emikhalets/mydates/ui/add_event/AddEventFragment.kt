package com.emikhalets.mydates.ui.add_event

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.emikhalets.mydates.R
import com.emikhalets.mydates.databinding.FragmentAddEventBinding
import com.emikhalets.mydates.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AddEventFragment : Fragment(R.layout.fragment_add_event) {

    private val binding by viewBinding(FragmentAddEventBinding::bind)
    private val viewModel: AddEventVM by viewModels()
    private val args: AddEventFragmentArgs by navArgs()

    private var date = 0L

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle(R.string.title_add_event)
        prepareEventData()
        clickListeners()
        observe()
    }

    private fun prepareEventData() {
        date = Calendar.getInstance().timeInMillis
        binding.inputDate.setText(date.toDateString("d MMMM YYYY"))
        setViewsForEventType(args.eventType)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun clickListeners() {
        binding.inputDate.setOnDrawableEndClick {
            startDatePickerDialog(date) { timestamp ->
                date = timestamp
                binding.inputDate.setDate(binding.checkYear.isChecked)
            }
        }
        binding.checkYear.setOnCheckedChangeListener { _, isChecked ->
            binding.inputDate.setDate(isChecked)
        }
        binding.btnSave.setOnClickListener {
            onSaveClick()
        }
        binding.root.setOnTouchListener { _, _ ->
            hideSoftKeyboard()
            clearFocus()
            false
        }
    }

    private fun observe() {
        viewModel.eventAdd.observe(viewLifecycleOwner) { navigateBack() }
    }

    private fun validateFields(): Boolean {
        return binding.inputName.text.toString().isNotEmpty()
    }

    private fun clearFocus() {
        binding.apply {
            inputName.clearFocus()
            inputLastname.clearFocus()
            inputMiddleName.clearFocus()
        }
    }

    private fun EditText.setDate(withoutYear: Boolean) {
        if (withoutYear) this.setText(date.toDateString("d MMMM"))
        else this.setText(date.toDateString("d MMMM YYYY"))
    }

    private fun onSaveClick() {
        if (validateFields()) when (args.eventType) {
            EventType.ANNIVERSARY -> viewModel.addNewAnniversary(
                binding.inputName.text.toString(),
                date,
                binding.checkYear.isChecked
            )
            EventType.BIRTHDAY -> viewModel.addNewBirthday(
                binding.inputName.text.toString(),
                binding.inputLastname.text.toString(),
                binding.inputMiddleName.text.toString(),
                date,
                binding.checkYear.isChecked
            )
        }
        else toast(R.string.fields_empty)
    }

    private fun setViewsForEventType(eventType: EventType) {
        when (eventType) {
            EventType.ANNIVERSARY -> {
                binding.apply {
                    imageLabel.setImageResource(R.drawable.ic_anniversary)
                    textLabel.text = getString(R.string.add_event_text_anniversary)
                    textLastname.visibility = View.GONE
                    textMiddleName.visibility = View.GONE
                    inputLastname.visibility = View.GONE
                    inputMiddleName.visibility = View.GONE
                }
            }
            EventType.BIRTHDAY -> {
                binding.apply {
                    imageLabel.setImageResource(R.drawable.ic_birthday)
                    textLabel.text = getString(R.string.add_event_text_birthday)
                }
            }
        }
    }
}