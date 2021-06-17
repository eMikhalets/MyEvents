package com.emikhalets.mydates.ui.add_event

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.emikhalets.mydates.R
import com.emikhalets.mydates.databinding.FragmentAddBirthdayBinding
import com.emikhalets.mydates.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AddBirthdayFragment : Fragment(R.layout.fragment_add_birthday) {

    private val binding by viewBinding(FragmentAddBirthdayBinding::bind)
    private val viewModel: AddEventVM by viewModels()

    private var date = 0L

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareEventData()
        clickListeners()
        observe()
    }

    private fun prepareEventData() {
        date = Calendar.getInstance().timeInMillis
        binding.inputDate.setText(date.dateFormat("d MMMM YYYY"))
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun clickListeners() {
        binding.inputDate.setOnDrawableEndClick {
            startDatePickerDialog(date) { timestamp ->
                date = timestamp
                binding.inputDate.setText(timestamp.dateFormat("d MMMM YYYY"))
            }
        }
        binding.btnSave.setOnClickListener {
            if (validateFields()) {
                viewModel.addNewBirthday(
                    binding.inputName.text.toString(),
                    binding.inputLastname.text.toString(),
                    binding.inputMiddleName.text.toString(),
                    date,
                    false
                )
            } else {
                toast(R.string.fields_empty)
            }
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
}