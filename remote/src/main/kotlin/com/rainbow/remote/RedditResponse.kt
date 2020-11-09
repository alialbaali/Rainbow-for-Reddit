package com.rainbow.remote

enum class RedditType {
    t1, t2, t3, t4, t5, t6
}

sealed class RedditResponse<T> {
    data class Success<T>(val kind: RedditType, val data: T) : RedditResponse<T>()
    data class Failure<T>(val message: String, val error: Int) : RedditResponse<T>()
}

