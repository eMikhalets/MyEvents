package com.emikhalets.mydates.ui.add_event

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.emikhalets.mydates.R
import com.emikhalets.mydates.ShareVM
import com.emikhalets.mydates.databinding.FragmentAddBirthdayBinding
import com.emikhalets.mydates.utils.*
import java.util.*

class AddBirthdayFragment : Fragment(R.layout.fragment_add_birthday) {

    private val binding by viewBinding(FragmentAddBirthdayBinding::bind)
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
            startDatePickerDialog(date) { ts ->
                binding.inputDate.setText(ts.dateFormat("d MMMM YYYY"))
            }
        }
    }

    private fun observe() {
        viewModel.eventAdd.observe(viewLifecycleOwner) { navigateBack() }
        shareVM.bottomBtnClick.observe(viewLifecycleOwner) {
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
    }

    private fun validateFields(): Boolean {
        return binding.inputName.text.toString().isNotEmpty() &&
                binding.inputLastname.text.toString().isNotEmpty()
    }
}