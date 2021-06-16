package com.emikhalets.mydates.ui.events_list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.emikhalets.mydates.R
import com.emikhalets.mydates.ShareVM
import com.emikhalets.mydates.databinding.FragmentEventsListBinding
import com.emikhalets.mydates.utils.day
import com.emikhalets.mydates.utils.month
import com.emikhalets.mydates.utils.startAddEventDialog
import com.emikhalets.mydates.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class EventsListFragment : Fragment(R.layout.fragment_events_list) {

    private val binding by viewBinding(FragmentEventsListBinding::bind)
    private val viewModel: EventsListViewModel by viewModels()
    private val shareVM: ShareVM by activityViewModels()
    private lateinit var datesAdapter: DatesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadAllEvents()
        observe()
    }

    private fun observe() {
        viewModel.events.observe(viewLifecycleOwner) {
            datesAdapter.submitList(it)
        }
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
            startAddEventDialog { dispatchIntent(DatesListIntent.ClickAddDateItem(it)) }
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