package com.emikhalets.mydates.mvi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

abstract class MviViewModel<I : MviIntent, A : MviAction, S : MviState>
    : ViewModel(), IModel<S, I> {

    protected val state = MutableLiveData<S>()
    override val stateImmutable: LiveData<S> get() = state

    fun launch(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch { block() }
    }

    final override fun dispatchIntent(intent: I) {
        handleAction(intentToAction(intent))
    }

    abstract fun intentToAction(intent: I): A
    abstract fun handleAction(action: A)
}