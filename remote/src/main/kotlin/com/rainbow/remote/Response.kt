package com.rainbow.remote

import com.rainbow.remote.dto.CurrentFlair
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal sealed class Response<out T>

@Serializable
internal data class Item<T>(val kind: Kind? = null, val data: T) : Response<T>()

@Serializable
internal data class Error(val message: String? = null, val error: Int? = null) : Response<Nothing>()

@Serializable
internal data class Listing<T>(val children: List<Item<T>> = emptyList())

@Serializable
internal data class UserListing<T>(val children: List<T> = emptyList())

@Serializable
internal data class FlairSelectorResponse(@SerialName("current") val current: CurrentFlair)

@Serializable
internal enum class Kind {
    @SerialName("t1")
    Comment,

    @SerialName("t2")
    User,

    @SerialName("t3")
    Post,

    @SerialName("t4")
    Message,

    @SerialName("t5")
    Subreddit,

    @SerialName("t6")
    Award,

    @SerialName("Listing")
    Listing,

    @SerialName("UserList")
    UserList,

    @SerialName("TrophyList")
    TrophyList,

    @SerialName("KarmaList")
    KarmaList,
}

internal fun <T> Listing<T>.toList() = children.map { it.data }
