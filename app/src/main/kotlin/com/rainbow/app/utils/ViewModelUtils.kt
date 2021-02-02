package com.rainbow.app.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.RememberObserver
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import com.rainbow.domain.ViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel

@Composable
@OptIn(ExperimentalCoroutinesApi::class)
@Suppress("NOTHING_TO_INLINE")
inline operator fun <I, S, VM : ViewModel<I, S>> VM.component1(): S {
    remember {

        object : RememberObserver {
            override fun onRemembered() {}
            override fun onForgotten() {
                viewModelScope.cancel()
            }
            override fun onAbandoned() {
                viewModelScope.cancel()
            }
        }

    }
    return state.collectAsState().value
}

@Suppress("NOTHING_TO_INLINE")
inline operator fun <I, S, VM : ViewModel<I, S>> VM.component2(): (I) -> Job = this::emitIntent

inline fun <I, S, VM : ViewModel<I, S>> VM.withState(block: S.() -> @UnsafeVariance S): S = state.value.block()

fun <T> Result<T>.toResource(): Resource<T> = fold(
    onSuccess = { Resource.Success(it) },
    onFailure = { Resource.Failure(it) }
)