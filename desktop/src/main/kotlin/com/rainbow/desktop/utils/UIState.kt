package com.rainbow.desktop.utils

import com.rainbow.desktop.utils.UIState.*

sealed interface UIState<out T> {
    object Empty : UIState<Nothing>

    data class Loading<T>(val data: T? = null) : UIState<T>

    data class Success<T>(val data: T) : UIState<T>

    data class Failure<T>(val data: T? = null, val exception: Throwable) : UIState<T>

    val isEmpty
        get() = this is Empty

    val isLoading
        get() = this is Loading

    val isSuccess
        get() = this is Success

    val isFailure
        get() = this is Failure

}

fun <T> UIState<T>.getOrNull(): T? = when (this) {
    is Empty -> null
    is Loading -> data
    is Success -> data
    is Failure -> data
}

fun <T> UIState<T>.getOrDefault(defaultValue: T): T = when (this) {
    is Empty -> defaultValue
    is Loading -> data ?: defaultValue
    is Success -> data
    is Failure -> data ?: defaultValue
}

inline fun <T, R> UIState<T>.map(transform: (value: T) -> R): UIState<R> = when (this) {
    is Empty -> Empty
    is Loading -> if (data != null) Loading(transform(data)) else Loading(null)
    is Success -> Success(transform(data))
    is Failure -> if (data != null) Failure(transform(data), exception) else Failure(null, exception)
}

inline fun <R, T> UIState<T>.fold(
    onEmpty: () -> R,
    onLoading: (value: T?) -> R,
    onSuccess: (value: T) -> R,
    onFailure: (value: T?, exception: Throwable) -> R,
): R = when (this) {
    is Empty -> onEmpty()
    is Loading -> onLoading(data)
    is Success -> onSuccess(data)
    is Failure -> onFailure(data, exception)
}


inline fun <T> UIState<T>.onSuccess(action: (value: T) -> Unit): UIState<T> {
    if (this is Success) action(data)
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