package com.emikhalets.mydates.ui.date_details

import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.emikhalets.mydates.R
import com.emikhalets.mydates.data.database.entities.DateItem
import com.emikhalets.mydates.databinding.FragmentDateDetailsBinding
import com.emikhalets.mydates.mvi.MviFragment
import com.emikhalets.mydates.utils.dateFormat
import com.emikhalets.mydates.utils.toast
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
                R.string.date_details_text_info,
                dateItem.date.dateFormat("d MMMM")
            )
            textAge.text = resources.getQuantityString(
                R.plurals.date_details_age, dateItem.age, dateItem.age
            )
        }
    }

    override fun initView() {
    }

    override fun initEvent() {
        binding.textSave.setOnClickListener {
            toast("Click blocked!")
//            dispatchIntent(DateDetailsIntent.ClickSaveDateItem(args.dateItem))
        }
        binding.textDelete.setOnClickListener {
            toast("Click blocked!")
//            dispatchIntent(DateDetailsIntent.ClickDeleteDateItem(args.dateItem))
        }
    }

    override fun render(state: DateDetailsState) {
        when (state) {
            is DateDetailsState.Error -> toast(state.message)
            DateDetailsState.ResultDateDeleted -> findNavController().popBackStack()
            DateDetailsState.ResultDateSaved -> toast("Saved!")
        }
    }

    private fun onDateClick(item: DateItem) {
        Toast.makeText(requireContext(), "clicked on ${item.name}", Toast.LENGTH_SHORT).show()
//        val action = DatesListFragmentDirections.actionDatesListToDateItem(item)
//        findNavController().navigate(action)
    }
}