package com.rainbow.app.utils

import com.rainbow.app.utils.Resource.*


sealed class Resource<out T> {
    object Loading : Resource<Nothing>()
    data class Success<T>(val value: T) : Resource<T>()
    data class Failure(val exception: Throwable) : Resource<Nothing>()

    val isLoading
        get() = this is Loading

    val isSuccess
        get() = this is Success

    val isFailure
        get() = this is Failure

}

fun <T> Resource<T>.getOrNull(): T? = when (this) {
    is Loading -> null
    is Success -> value
    is Failure -> null
}

fun <T> Resource<T>.getOrDefault(defaultValue: T): T = when (this) {
    is Loading -> defaultValue
    is Success -> value
    is Failure -> defaultValue
}

inline fun <T, R> Resource<T>.map(transform: (value: T) -> R): Resource<R> = when (this) {
    is Loading -> Loading
    is Success -> Success(transform(value))
    is Failure -> Failure(exception)
}

inline fun <R, T> Resource<T>.fold(
    onLoading: () -> R,
    onSuccess: (value: T) -> R,
    onFailure: (exception: Throwable) -> R,
): R = when (this) {
    is Loading -> onLoading()
    is Success -> onSuccess(value)
    is Failure -> onFailure(exception)
}


inline fun <T> Resource<T>.onSuccess(action: (value: T) -> Unit): Resource<T> {
    if (this is Success) action(value)
    return this
}

inline fun <T> Resource<T>.onFailure(action: (exception: Throwable) -> Unit): Resource<T> {
    if (this is Failure) action(exception)
    return this
}

inline fun <T> Resource<T>.onLoading(action: () -> Unit): Resource<T> {
    if (this is Loading) action()
    return this
}