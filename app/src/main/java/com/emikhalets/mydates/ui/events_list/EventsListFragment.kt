package com.emikhalets.mydates.ui.events_list

import android.app.Application
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.emikhalets.mydates.R
import com.emikhalets.mydates.data.database.entities.Event
import com.emikhalets.mydates.databinding.FragmentEventsListBinding
import com.emikhalets.mydates.utils.*
import dagger.hilt.android.AndroidEntryPoint

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
        viewModel.loadAllEvents(
            requireActivity()
                .getSharedPreferences(APP_SHARED_PREFERENCES, Application.MODE_PRIVATE)
                .getLong(APP_SP_UPDATE_EVENTS_TIME, 0)
        )
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
        viewModel.events.observe(viewLifecycleOwner) {
            eventsAdapter.submitList(it)
        }
        viewModel.loading.observe(viewLifecycleOwner) {
            if (it) binding.loader.root.visibility = View.VISIBLE
            else binding.loader.root.visibility = View.GONE
        }
    }

    private fun onDateClick(item: Event) {
        navigateEventsToEventDetails(item)
    }
}