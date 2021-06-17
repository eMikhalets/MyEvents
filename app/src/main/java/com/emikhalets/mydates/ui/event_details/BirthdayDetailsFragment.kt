package com.emikhalets.mydates.ui.event_details

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.emikhalets.mydates.R
import com.emikhalets.mydates.ShareVM
import com.emikhalets.mydates.data.database.entities.Event
import com.emikhalets.mydates.databinding.FragmentAddBirthdayBinding
import com.emikhalets.mydates.utils.dateFormat
import com.emikhalets.mydates.utils.startDatePickerDialog
import java.util.*

class BirthdayDetailsFragment : Fragment(R.layout.fragment_add_birthday) {

    private val binding by viewBinding(FragmentAddBirthdayBinding::bind)
    private val viewModel: AddBirthdayVM by viewModels()
    private val shareVM: ShareVM by activityViewModels()

    var name = ""
    var date = Calendar.getInstance().timeInMillis
    val dateItem = Event(name)

    inputDate.setText(date.dateFormat("d MMMM YYYY"))
    inputDate.setOnDrawableEndClick
    {
        startDatePickerDialog(requireContext(), date) {
            date = it
            inputDate.setText(it.dateFormat("d MMMM YYYY"))
        }

        btnAdd.setOnClickListener {
            name = inputName.text.toString()
            if (name.isNotEmpty()) {
                dateItem.name = name
                dateItem.date = date
                callback.invoke(dateItem)
                dialog.dismiss()
            }
        }
    }
}