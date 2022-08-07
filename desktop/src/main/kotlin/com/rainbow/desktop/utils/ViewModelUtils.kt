package com.rainbow.desktop.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.RememberObserver
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import com.rainbow.domain.ViewModel
import com.rainbow.domain.ViewModel.Intent
import com.rainbow.domain.ViewModel.State
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel

@Composable
fun <I : Intent, S : State> rememberViewModel(viewModel: ViewModel<I, S>): ViewModel<I, S> {
    val wrapper = remember {
        CompositionScopedViewModelController(viewModel)
    }
    return wrapper.viewModel
}

private class CompositionScopedViewModelController<I : Intent, S : State>(
    val viewModel: ViewModel<I, S>
) : RememberObserver {
    override fun onRemembered() {
        // Nothing to do
    }

    override fun onForgotten() {
        viewModel.viewModelScope.cancel()
    }

    override fun onAbandoned() {
        viewModel.viewModelScope.cancel()
    }
}

@Composable
@OptIn(ExperimentalCoroutinesApi::class)
@Suppress("NOTHING_TO_INLINE")
inline operator fun <I : Intent, S : State, VM : ViewModel<I, S>> VM.component1(): S = state.collectAsState().value

@Suppress("NOTHING_TO_INLINE")
inline operator fun <I : Intent, S : State, VM : ViewModel<I, S>> VM.component2(): (I) -> Job = this::emitIntent

inline fun <I : Intent, S : State, VM : ViewModel<I, S>> VM.withState(block: S.() -> @UnsafeVariance S): S =
    state.value.block()

fun <T> Result<T>.toUIState(): UIState<T> = fold(
    onSuccess = { UIState.Success(it) },
    onFailure = { UIState.Failure(it) }
)