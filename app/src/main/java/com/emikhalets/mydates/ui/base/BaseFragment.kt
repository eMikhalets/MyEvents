package com.emikhalets.mydates.ui.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.emikhalets.mydates.utils.di.appComponent
import com.emikhalets.mydates.utils.di.viewmodel.ViewModelFactory
import javax.inject.Inject

open class BaseFragment(@LayoutRes layoutRes: Int) : Fragment(layoutRes) {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }
}