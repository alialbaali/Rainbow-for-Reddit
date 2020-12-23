package com.rainbow.remote.impl

import com.rainbow.remote.RedditResponse
import io.kotest.matchers.types.shouldBeInstanceOf

internal inline fun <reified T> RedditResponse<T>.shouldBeSuccess(): RedditResponse.Success<T> {
    shouldBeInstanceOf<RedditResponse.Success<T>>()
    return asSuccess()
}

internal inline fun <reified T> RedditResponse<T>.shouldBeFailure(): RedditResponse.Failure<T> {
    shouldBeInstanceOf<RedditResponse.Failure<T>>()
    return asFailure()
}

internal inline fun <reified T> RedditResponse<T>.asSuccess() = this as RedditResponse.Success<T>

internal inline fun <reified T> RedditResponse<T>.asFailure() = this as RedditResponse.Failure<T>