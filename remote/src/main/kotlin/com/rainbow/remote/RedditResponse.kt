package com.rainbow.remote

import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

    @Serializable
sealed class RedditResponse<T> {
    @Serializable
    data class Success<T>(@Serializable val kind: RedditKind? = null, @Serializable val data: T) : RedditResponse<T>()
    @Serializable
    data class Failure<T>(val message: String, val error: Int) : RedditResponse<T>()
}

inline fun <R, T> RedditResponse<T>.map(transform: (value: T) -> R): RedditResponse<R> = when (this) {
    is RedditResponse.Success -> RedditResponse.Success(kind, transform(data))
    is RedditResponse.Failure -> RedditResponse.Failure(message, error)
}

//sealed class Response<T> {
//    data class Item<T>(val kind: RedditKind?, val data: T) : Response<T>()
//    data class Error<T>(val message: String, val error: Int) : Response<T>()
//}

data class Listing<T>(
    val modHash: String?,
    val dist: Long?,
    val children: List<RedditResponse.Success<T>> = emptyList(),
    val before: String?,
    val after: String?,
)

data class UserListing<T>(
    val children: List<T> = emptyList(),
)


val <T> Listing<T>.items get() = children.map { it.data }

@Serializable
enum class RedditKind {
    @JsonProperty("t1")
    Comment,

    @JsonProperty("t2")
    @SerialName("t2")
    User,

    @JsonProperty("t3")
    Post,

    @JsonProperty("t4")
    Message,

    @JsonProperty("t5")
    Subreddit,

    @JsonProperty("t6")
    Award,

    @JsonProperty("Listing")
    Listing,

    @JsonProperty("UserList")
    UserList,
}

