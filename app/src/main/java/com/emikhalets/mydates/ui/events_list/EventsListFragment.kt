package com.emikhalets.mydates.ui.events_list

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.emikhalets.mydates.R
import com.emikhalets.mydates.data.database.entities.Event
import com.emikhalets.mydates.databinding.FragmentEventsListBinding
import com.emikhalets.mydates.utils.Preferences
import com.emikhalets.mydates.utils.navigateEventsToEventDetails
import com.emikhalets.mydates.utils.navigateEventsToSettings
import com.emikhalets.mydates.utils.setTitle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EventsListFragment : Fragment(R.layout.fragment_events_list) {

    private val binding by viewBinding(FragmentEventsListBinding::bind)
    private val viewModel: EventsListVM by viewModels()
    private lateinit var eventsAdapter: EventsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle(R.string.title_home)
        setHasOptionsMenu(true)
        initEventsAdapter()
        observe()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadAllEvents(Preferences.getEventsLastUpdateTime(requireContext()))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_settings -> navigateEventsToSettings()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initEventsAdapter() {
        eventsAdapter = EventsAdapter { onDateClick(it) }
        binding.listDates.apply {
            setHasFixedSize(true)
            adapter = eventsAdapter
        }
    }

    private fun observe() {
        lifecycleScope.launch {
            viewModel.state.collect { renderState(it) }
        }
    }

    private fun renderState(state: EventsListState) {
        when (state) {
            is EventsListState.Error -> {
                binding.loader.root.visibility = View.GONE
            }
            EventsListState.EmptyEvents -> {
                binding.loader.root.visibility = View.GONE
            }
            is EventsListState.Events -> {
                binding.loader.root.visibility = View.GONE
                eventsAdapter.submitList(state.events)
            }
            EventsListState.Loading -> {
                binding.loader.root.visibility = View.VISIBLE
            }
            EventsListState.Init -> {
            }
        }
    }

    private fun onDateClick(item: Event) {
        navigateEventsToEventDetails(item)
    }
}