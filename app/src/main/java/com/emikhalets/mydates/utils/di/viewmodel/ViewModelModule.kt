package com.emikhalets.mydates.utils.di.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.emikhalets.mydates.ui.add_event.AddEventVM
import com.emikhalets.mydates.ui.calendar.CalendarVM
import com.emikhalets.mydates.ui.event_details.EventDetailsVM
import com.emikhalets.mydates.ui.events_list.EventsListVM
import com.emikhalets.mydates.ui.settings.SettingsVM
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(AddEventVM::class)
    abstract fun bindAddEventVM(viewModel: AddEventVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CalendarVM::class)
    abstract fun bindCalendarVM(viewModel: CalendarVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EventDetailsVM::class)
    abstract fun bindEventDetailsVM(viewModel: EventDetailsVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EventsListVM::class)
    abstract fun bindEventsListVM(viewModel: EventsListVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingsVM::class)
    abstract fun bindSettingsVM(viewModel: SettingsVM): ViewModel
}