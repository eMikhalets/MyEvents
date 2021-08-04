package com.emikhalets.mydates.ui.calendar

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.emikhalets.mydates.R
import com.emikhalets.mydates.data.database.entities.Event
import com.emikhalets.mydates.databinding.FragmentCalendarBinding
import com.emikhalets.mydates.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class CalendarFragment : Fragment(R.layout.fragment_calendar) {

    private val binding by viewBinding(FragmentCalendarBinding::bind)
    private val viewModel: CalendarVM by viewModels()
    private lateinit var eventsAdapter: DayEventsAdapter

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
            R.id.menu_settings -> navigateCalendarToSettings()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun init() {
        binding.calendar.apply {
            val min = Calendar.getInstance()
            val now = Calendar.getInstance()
            val max = Calendar.getInstance()
            min.set(now.year(), Calendar.JANUARY, 0)
            max.set(now.year(), Calendar.DECEMBER, 31)
            setMinimumDate(min)
            setMaximumDate(max)
            setCalendarDayLayout(R.layout.layout_calendar_day)
            setDate(viewModel.selectedDate)
        }
        eventsAdapter = DayEventsAdapter { onEventClick(it) }
        binding.listDates.adapter = eventsAdapter
        binding.listDates.setHasFixedSize(true)
    }

    private fun listeners() {
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
        navigateCalendarToEvent(event)
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