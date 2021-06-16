package com.emikhalets.mydates.ui.dates_list

import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.emikhalets.mydates.R
import com.emikhalets.mydates.data.GroupDateItem
import com.emikhalets.mydates.databinding.FragmentDatesListBinding
import com.emikhalets.mydates.mvi.MviFragment
import com.emikhalets.mydates.ui.DatesAdapter
import com.emikhalets.mydates.utils.day
import com.emikhalets.mydates.utils.month
import com.emikhalets.mydates.utils.startAddDateDialog
import com.emikhalets.mydates.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class DatesListFragment :
    MviFragment<DatesListIntent, DatesListAction, DatesListState, DatesListViewModel>(
        R.layout.fragment_dates_list
    ) {

    override val binding by viewBinding(FragmentDatesListBinding::bind)
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
        val now = Calendar.getInstance()
        val lastUpdateTime = requireActivity().getSharedPreferences("MyDates", 0)
            .getLong("last_update", now.timeInMillis)
        val last = Calendar.getInstance()
        last.timeInMillis = lastUpdateTime
        if ((last.month() < now.month()) ||
            (last.month() == now.month() && last.day() < now.day())
        ) {
            requireActivity().getSharedPreferences("MyDates", 0).edit()
                .putLong("last_update", now.timeInMillis).apply()
            dispatchIntent(DatesListIntent.UpdateDatesList)
        }

        binding.btnAddDate.setOnClickListener {
            startAddDateDialog { dispatchIntent(DatesListIntent.ClickAddDateItem(it)) }
        }
    }

    override fun render(state: DatesListState) {
        binding.loader.root.visibility = View.GONE
        when (state) {
            is DatesListState.Error -> toast(state.message)
            is DatesListState.ResultDatesList -> datesAdapter.submitList(state.data)
            is DatesListState.ResultDateUpdated -> datesAdapter.submitList(state.data)
            DatesListState.ResultDateAdded -> dispatchIntent(DatesListIntent.LoadDatesList)
            DatesListState.Loading -> binding.loader.root.visibility = View.VISIBLE
            DatesListState.ResultEmptyList -> {
            }
        }
    }

    private fun onDateClick(item: GroupDateItem) {
        val action = DatesListFragmentDirections.actionHomeToDateDetails(item)
        findNavController().navigate(action)
    }
}