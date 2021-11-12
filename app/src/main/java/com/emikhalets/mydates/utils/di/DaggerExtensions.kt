package com.emikhalets.mydates.utils.di

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.emikhalets.mydates.MyDatesApp

val Activity.appComponent: AppComponent
    get() = (applicationContext as MyDatesApp).appComponent

val Fragment.appComponent: AppComponent
    get() = (requireActivity().applicationContext as MyDatesApp).appComponent

inline fun <reified T : ViewModel> Fragment.injectViewModel(factory: ViewModelProvider.Factory): T {
    return ViewModelProvider(this, factory)[T::class.java]
}