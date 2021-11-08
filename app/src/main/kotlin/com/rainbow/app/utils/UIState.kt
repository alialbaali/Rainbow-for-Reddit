package com.rainbow.app.utils

import com.rainbow.app.utils.UIState.*


sealed interface UIState<out T> {
    object Empty : UIState<Nothing>
    object Loading : UIState<Nothing>
    data class Success<T>(val value: T) : UIState<T>
    data class Failure(val exception: Throwable) : UIState<Nothing>

    val isLoading
        get() = this is Loading

    val isSuccess
        get() = this is Success

    val isFailure
        get() = this is Failure

}

fun <T> UIState<T>.getOrNull(): T? = when (this) {
    is Empty -> null
    is Loading -> null
    is Success -> value
    is Failure -> null
}

fun <T> UIState<T>.getOrDefault(defaultValue: T): T = when (this) {
    is Empty -> defaultValue
    is Loading -> defaultValue
    is Success -> value
    is Failure -> defaultValue
}

inline fun <T, R> UIState<T>.map(transform: (value: T) -> R): UIState<R> = when (this) {
    is Empty -> Empty
    is Loading -> Loading
    is Success -> Success(transform(value))
    is Failure -> Failure(exception)
}

inline fun <R, T> UIState<T>.fold(
    onEmpty: () -> R,
    onLoading: () -> R,
    onSuccess: (value: T) -> R,
    onFailure: (exception: Throwable) -> R,
): R = when (this) {
    is Empty -> onEmpty()
    is Loading -> onLoading()
    is Success -> onSuccess(value)
    is Failure -> onFailure(exception)
}


inline fun <T> UIState<T>.onSuccess(action: (value: T) -> Unit): UIState<T> {
    if (this is Success) action(value)
    return this
}

inline fun <T> UIState<T>.onFailure(action: (exception: Throwable) -> Unit): UIState<T> {
    if (this is Failure) action(exception)
    return this
}

inline fun <T> UIState<T>.onLoading(action: () -> Unit): UIState<T> {
    if (this is Loading) action()
    return this
}

fun <T> UIState<T>.asSuccess() = this as Success