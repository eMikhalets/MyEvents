package com.emikhalets.mydates.ui.events_list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.emikhalets.mydates.R
import com.emikhalets.mydates.ShareVM
import com.emikhalets.mydates.data.database.entities.Event
import com.emikhalets.mydates.databinding.FragmentEventsListBinding
import com.emikhalets.mydates.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventsListFragment : Fragment(R.layout.fragment_events_list) {

    private val binding by viewBinding(FragmentEventsListBinding::bind)
    private val viewModel: EventsListViewModel by viewModels()
    private val shareVM: ShareVM by activityViewModels()
    private lateinit var eventsAdapter: EventsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEventsAdapter()
        observe()
    }

    override fun onStart() {
        super.onStart()
        viewModel.loadAllEvents()
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
            binding.loader.root.visibility = View.VISIBLE
        }
        shareVM.bottomBtnClick.observe(viewLifecycleOwner) {
            startAddEventDialog { eventType ->
                when (eventType) {
                    EventType.ANNIVERSARY -> navigateToAddAnniversary()
                    EventType.BIRTHDAY -> navigateToAddBirthday()
                }
            }
        }
    }

    private fun onDateClick(item: Event) {
        when (item.eventType) {
            EventType.ANNIVERSARY.value -> navigateToAnniversaryDetails()
            EventType.BIRTHDAY.value -> navigateToBirthdayDetails()
        }
    }
}