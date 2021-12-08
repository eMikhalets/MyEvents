package com.emikhalets.mydates.ui.calendar

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnCalendarPageChangeListener
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.emikhalets.mydates.R
import com.emikhalets.mydates.data.database.entities.Event
import com.emikhalets.mydates.databinding.FragmentCalendarBinding
import com.emikhalets.mydates.ui.base.BaseFragment
import com.emikhalets.mydates.utils.AppNavigationManager
import com.emikhalets.mydates.utils.extentions.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

class CalendarFragment : BaseFragment(R.layout.fragment_calendar) {

    private val binding by viewBinding(FragmentCalendarBinding::bind)
    private val viewModel by viewModels<CalendarVM> { viewModelFactory }
    private lateinit var eventsAdapter: DayEventsAdapter

    private var currentMinDay = Calendar.getInstance()
    private var currentMaxDay = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val now = Calendar.getInstance()
        currentMinDay.set(now.year(), now.month() - 1, 1)
        currentMaxDay.set(now.year(), now.month() + 1, now.nextMonthMaxDay())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        init()
        listeners()
        observe()
        if (savedInstanceState == null) {
            renderState(CalendarState.Init)
            viewModel.loadAllEvents()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_settings -> {
                AppNavigationManager.toSettings(this)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun init() {
        binding.calendar.apply {
            setMinimumDate(currentMinDay)
            setMaximumDate(currentMaxDay)
            setCalendarDayLayout(R.layout.layout_calendar_day)
            setDate(viewModel.selectedDate)
        }
        eventsAdapter = DayEventsAdapter { onEventClick(it) }
        binding.listDates.adapter = eventsAdapter
        binding.listDates.setHasFixedSize(true)
    }

    private fun listeners() {
        binding.calendar.setOnForwardPageChangeListener(object : OnCalendarPageChangeListener {
            override fun onChange() {
                binding.calendar.setMaximumDate(currentMaxDay.plusMonthAndLastDay())
            }
        })
        binding.calendar.setOnPreviousPageChangeListener(object : OnCalendarPageChangeListener {
            override fun onChange() {
                binding.calendar.setMinimumDate(currentMinDay.minusMonthAndFirstDay())
            }
        })
        binding.calendar.setOnDayClickListener(object : OnDayClickListener {
            override fun onDayClick(eventDay: EventDay) {
                viewModel.selectedDate = eventDay.calendar
                viewModel.getDayEvents(eventDay.calendar)
            }
        })
    }

    private fun observe() {
        lifecycleScope.launch {
            viewModel.state.collect { renderState(it) }
        }
    }

    private fun onEventClick(event: Event) {
        AppNavigationManager.toEventDetails(this, event)
    }

    private fun renderState(state: CalendarState) {
        when (state) {
            is CalendarState.Error -> {
                toast(state.message)
            }
            is CalendarState.AllEvents -> {
                setCalendarEvents(state.events)
            }
            is CalendarState.DayEvents -> {
                eventsAdapter.submitList(state.events)
            }
            CalendarState.EmptyAllEvents -> {
            }
            CalendarState.EmptyDayEvents -> {
                eventsAdapter.submitList(null)
            }
            CalendarState.Init -> {
            }
        }
    }

    private fun setCalendarEvents(events: List<Event>) {
        lifecycleScope.launchWhenCreated {
            val list = mutableListOf<EventDay>()
            events.forEach { list.add(EventDay(it.date.toCalendar(), R.drawable.ic_circle)) }
            binding.calendar.setEvents(list)
        }
    }
}