package com.emikhalets.mydates.ui.add_event

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.emikhalets.mydates.R
import com.emikhalets.mydates.ShareVM
import com.emikhalets.mydates.databinding.FragmentAddAnniversaryBinding
import com.emikhalets.mydates.utils.*
import java.util.*

class AddAnniversaryFragment : Fragment(R.layout.fragment_add_anniversary) {

    private val binding by viewBinding(FragmentAddAnniversaryBinding::bind)
    private val viewModel: AddEventVM by viewModels()
    private val shareVM: ShareVM by activityViewModels()

    private var date = 0L

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shareVM.setBottomBtnIcon(BottomBtnIcon.DONE)
        prepareEventData()
        clickListeners()
        observe()
    }

    private fun prepareEventData() {
        date = Calendar.getInstance().timeInMillis
        binding.inputDate.setText(date.dateFormat("d MMMM YYYY"))
    }

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
    }

    private fun observe() {
        viewModel.eventAdd.observe(viewLifecycleOwner) { navigateBack() }
        shareVM.bottomBtnClick.observe(viewLifecycleOwner) {
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
    }

    private fun validateFields(): Boolean {
        return binding.inputName.text.toString().isNotEmpty()
    }

    private fun EditText.setDate(withoutYear: Boolean) {
        if (withoutYear) this.setText(date.dateFormat("d MMMM"))
        else this.setText(date.dateFormat("d MMMM YYYY"))
    }
}