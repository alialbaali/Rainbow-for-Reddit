package com.rainbow.domain

import com.rainbow.domain.ViewModel.Intent
import com.rainbow.domain.ViewModel.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

abstract class ViewModel<in I : Intent, out S : State>(initialState: S) {

    val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private val mutableIntent = MutableSharedFlow<I>(replay = 1)

    private val mutableState = MutableStateFlow(initialState)
    val state = mutableState.asStateFlow()

    init {
        mutableIntent
            .onEach { intent -> mutableState.value = onIntent(intent) }
            .launchIn(viewModelScope)
    }

    fun emitIntent(intent: I) = viewModelScope.launch { mutableIntent.emit(intent) }

    protected abstract suspend fun onIntent(intent: I): S

    interface Intent

    interface State

}