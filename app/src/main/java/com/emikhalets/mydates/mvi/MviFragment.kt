package com.emikhalets.mydates.mvi

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class MviFragment<I : MviIntent, A : MviAction, S : MviState, VM : MviViewModel<I, A, S>>(
    @LayoutRes val layoutId: Int
) : Fragment(layoutId), IViewRenderer<S> {

    abstract val binding: ViewBinding
    abstract val viewModel: VM

    private lateinit var viewState: S
    val state get() = viewState

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initView()
        initEvent()

        viewModel.stateImmutable.observe(viewLifecycleOwner, { state ->
            viewState = state
            render(state)
        })
    }

    abstract fun initData()
    abstract fun initView()
    abstract fun initEvent()

    fun dispatchIntent(intent: I) {
        viewModel.dispatchIntent(intent)
    }
}