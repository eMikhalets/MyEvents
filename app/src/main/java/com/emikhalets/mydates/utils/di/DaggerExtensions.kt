package com.emikhalets.mydates.utils.di

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.emikhalets.mydates.MyDatesApp

val Context.appComponent: AppComponent
    get() = (applicationContext as MyDatesApp).appComponent

val Activity.appComponent: AppComponent
    get() = (applicationContext as MyDatesApp).appComponent

val Fragment.appComponent: AppComponent
    get() = (requireActivity().applicationContext as MyDatesApp).appComponent