package com.emikhalets.mydates.ui.dates_list

import android.widget.Toast
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.emikhalets.mydates.R
import com.emikhalets.mydates.data.database.entities.DateItem
import com.emikhalets.mydates.databinding.FragmentDatesListBinding
import com.emikhalets.mydates.mvi.MviFragment
import com.emikhalets.mydates.utils.DialogHelper
import com.emikhalets.mydates.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DatesListFragment :
    MviFragment<DatesListIntent, DatesListAction, DatesListState, DatesListViewModel>(
        R.layout.fragment_dates_list
    ) {

    private val binding by viewBinding(FragmentDatesListBinding::bind)
    override val viewModel: DatesListViewModel by viewModels()
    private lateinit var datesAdapter: DatesAdapter

    override fun initData() {
        dispatchIntent(DatesListIntent.LoadDatesList)
    }

    override fun initView() {
        datesAdapter = DatesAdapter { onDateClick(it) }
        binding.listDates.apply {
            setHasFixedSize(true)
            adapter = datesAdapter
        }
    }

    override fun initEvent() {
        binding.btnAddDate.setOnClickListener {
            DialogHelper().startAddDateDialog(requireContext()) {
                dispatchIntent(DatesListIntent.ClickAddDateItem(it))
            }
        }
    }

    override fun render(state: DatesListState) {
        when (state) {
            DatesListState.ResultEmptyList -> {
            }
            is DatesListState.ResultDatesList -> {
                datesAdapter.submitList(state.data)
            }
            is DatesListState.Error -> {
                toast(state.message)
            }
            DatesListState.ResultDateAdded -> {
                dispatchIntent(DatesListIntent.LoadDatesList)
            }
        }
    }

    private fun onDateClick(item: DateItem) {
        Toast.makeText(requireContext(), "clicked on ${item.name}", Toast.LENGTH_SHORT).show()
//        val action = DatesListFragmentDirections.actionDatesListToDateItem(item)
//        findNavController().navigate(action)
    }
}