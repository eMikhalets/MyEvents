package com.emikhalets.mydates.ui.common

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

abstract class BaseFragment<STATE : AppState>(@LayoutRes res: Int) : Fragment(res) {

    abstract fun renderState(state: STATE)
}