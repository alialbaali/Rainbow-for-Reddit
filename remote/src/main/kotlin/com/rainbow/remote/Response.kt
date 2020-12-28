package com.rainbow.remote

import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal sealed class Response<T>

internal data class Item<T>(val kind: Kind?, val data: T) : Response<T>()

internal data class Error<T>(val message: String, val error: Int) : Response<T>()

internal data class Listing<T>(
    val dist: Long?,
    val children: List<Item<T>> = emptyList(),
    val before: String?,
    val after: String?,
)

internal data class UserListing<T>(
    val children: List<T> = emptyList(),
)

internal fun <T> Listing<T>.toList() = children.map { it.data }

@Serializable
internal enum class Kind {
    @SerialName("t1")
    @JsonProperty("t1")
    Comment,

    @SerialName("t2")
    @JsonProperty("t2")
    User,

    @SerialName("t3")
    @JsonProperty("t3")
    Post,

    @SerialName("t4")
    @JsonProperty("t4")
    Message,

    @SerialName("t5")
    @JsonProperty("t5")
    Subreddit,

    @SerialName("t6")
    @JsonProperty("t6")
    Award,

    @SerialName("Listing")
    @JsonProperty("Listing")
    Listing,

    @SerialName("UserList")
    @JsonProperty("UserList")
    UserList,
}

