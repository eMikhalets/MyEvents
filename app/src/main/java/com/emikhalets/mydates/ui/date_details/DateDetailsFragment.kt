package com.emikhalets.mydates.ui.date_details

import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.emikhalets.mydates.R
import com.emikhalets.mydates.databinding.FragmentDateDetailsBinding
import com.emikhalets.mydates.mvi.MviFragment
import com.emikhalets.mydates.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DateDetailsFragment :
    MviFragment<DateDetailsIntent, DateDetailsAction, DateDetailsState, DateDetailsViewModel>(
        R.layout.fragment_date_details
    ) {

    override val binding by viewBinding(FragmentDateDetailsBinding::bind)
    override val viewModel: DateDetailsViewModel by viewModels()
    private val args: DateDetailsFragmentArgs by navArgs()

    override fun initData() {
        val dateItem = args.dateItem
        with(binding) {
            textName.text = dateItem.name
            textInfo.text = getString(
                R.string.date_details_info,
                dateItem.date.dateFormat("d MMMM")
            )
            textDaysLeft.text = resources.getQuantityString(
                R.plurals.date_details_days_left, dateItem.daysLeft, dateItem.daysLeft
            )
            textAge.text = resources.getQuantityString(
                R.plurals.date_details_age, dateItem.age, dateItem.age
            )
            inputName.setText(dateItem.name)
            inputDate.setText(dateItem.date.dateFormat("d MMMM YYYY"))
        }
    }

    override fun initView() {
    }

    override fun initEvent() {
        binding.apply {
            btnSave.setOnClickListener {
                dispatchIntent(DateDetailsIntent.ClickSaveDateItem(args.dateItem))
            }
            btnDelete.setOnClickListener {
                dispatchIntent(DateDetailsIntent.ClickDeleteDateItem(args.dateItem))
            }
            inputName.doAfterTextChanged {
                args.dateItem.name = it.toString()
                btnSave.isEnabled = true
            }
            inputDate.setOnDrawableEndClick {
                DialogHelper().startDatePickerDialog(requireContext(), args.dateItem.date) {
                    args.dateItem.date = it
                    args.dateItem.computeDaysLeftAndAge()
                    inputDate.setText(it.dateFormat("d MMMM YYYY"))
                    textDaysLeft.text = resources.getQuantityString(
                        R.plurals.date_details_days_left,
                        args.dateItem.daysLeft, args.dateItem.daysLeft
                    )
                    textAge.text = resources.getQuantityString(
                        R.plurals.date_details_age, args.dateItem.age, args.dateItem.age
                    )
                    btnSave.isEnabled = true
                }
            }
        }
    }

    override fun render(state: DateDetailsState) {
        when (state) {
            is DateDetailsState.Error -> toast(state.message)
            DateDetailsState.ResultDateDeleted -> findNavController().popBackStack()
            DateDetailsState.ResultDateSaved -> toast("Saved!")
        }
    }
}