package com.emikhalets.mydates.mvi

import androidx.lifecycle.LiveData

abstract class MviState

abstract class MviAction

abstract class MviIntent

interface IModel<STATE, INTENT> {
    val stateImmutable: LiveData<STATE>
    fun dispatchIntent(intent: INTENT)
}

interface IViewRenderer<STATE> {
    fun render(state: STATE)
}