package com.emikhalets.mydates.ui.calendar

import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.emikhalets.mydates.R
import com.emikhalets.mydates.databinding.FragmentCalendarBinding
import com.emikhalets.mydates.mvi.MviFragment
import com.emikhalets.mydates.ui.events_list.DatesAdapter
import com.emikhalets.mydates.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.text.DateFormatSymbols
import java.util.*

@AndroidEntryPoint
class CalendarFragment :
    MviFragment<CalendarIntent, CalendarAction, CalendarState, CalendarViewModel>(
        R.layout.fragment_calendar
    ) {

    override val binding by viewBinding(FragmentCalendarBinding::bind)
    override val viewModel: CalendarViewModel by viewModels()
    private lateinit var datesAdapter: DatesAdapter

    override fun initData() {
        dispatchIntent(CalendarIntent.LoadAllDates)
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
        binding.textDate.text = binding.calendar.date.dateFormat("d MMMM")
        dispatchIntent(CalendarIntent.LoadDatesByDayMonth(now.day(), now.month()))

        binding.calendar.setOnDateChangeListener { _, _, month, day ->
            binding.textDate.text = getString(
                R.string.calendar_date, day, DateFormatSymbols().months[month]
            )
            dispatchIntent(CalendarIntent.LoadDatesByDayMonth(day, month))
        }
    }

    override fun render(state: CalendarState) {
        when (state) {
            CalendarState.ResultEmptyList -> {
                binding.textNoDates.visibility = View.VISIBLE
                binding.textDate.visibility = View.GONE
                binding.listDates.visibility = View.GONE
            }
            is CalendarState.ResultAllDatesList -> {
                // TODO: add dots in calendar, if day has dateItems
            }
            is CalendarState.ResultDatesByDate -> {
                datesAdapter.submitList(groupDateItemList(state.data))
                binding.textNoDates.visibility = View.GONE
                binding.textDate.visibility = View.VISIBLE
                binding.listDates.visibility = View.VISIBLE
            }
            is CalendarState.Error -> toast(state.message)
        }
    }

    private fun onDateClick(item: GroupDateItem) {
        val action = CalendarFragmentDirections.actionCalendarToDateDetails(item)
        findNavController().navigate(action)
    }
}