package com.emikhalets.mydates.ui.add_event

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.emikhalets.mydates.R
import com.emikhalets.mydates.databinding.FragmentAddAnniversaryBinding
import com.emikhalets.mydates.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AddAnniversaryFragment : Fragment(R.layout.fragment_add_anniversary) {

    private val binding by viewBinding(FragmentAddAnniversaryBinding::bind)
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
                binding.inputDate.setDate(binding.checkYear.isChecked)
            }
        }
        binding.checkYear.setOnCheckedChangeListener { _, isChecked ->
            binding.inputDate.setDate(isChecked)
        }
        binding.btnSave.setOnClickListener {
            if (validateFields()) {
                viewModel.addNewAnniversary(
                    binding.inputName.text.toString(),
                    date,
                    binding.checkYear.isChecked
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

    private fun EditText.setDate(withoutYear: Boolean) {
        if (withoutYear) this.setText(date.dateFormat("d MMMM"))
        else this.setText(date.dateFormat("d MMMM YYYY"))
    }

    private fun clearFocus() {
        binding.apply {
            inputName.clearFocus()
        }
    }
}