package com.emikhalets.mydates.utils.di

import android.content.Context
import com.emikhalets.mydates.data.repositories.AppPrefsRepository
import com.emikhalets.mydates.ui.MainActivity
import com.emikhalets.mydates.ui.base.BaseFragment
import com.emikhalets.mydates.utils.di.viewmodel.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    ViewModelModule::class,
    DatabaseModule::class,
    RepositoriesModule::class
])
interface AppComponent {

    val applicationContext: AppComponent
    val appPreferences: AppPrefsRepository

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): AppComponent
    }

    fun inject(activity: MainActivity)
    fun inject(fragment: BaseFragment)
}