package com.rainbow.remote.dto

import com.rainbow.remote.Item
import com.rainbow.remote.Listing
import com.rainbow.remote.toList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteComment internal constructor(
    val id: String,

    val author: String,

    @SerialName("author_fullname")
    val authorFullName: String,

    @SerialName("link_id")
    val linkId: String,

    @SerialName("subreddit_id")
    val subredditId: String,

    val subreddit: String,

    val body: String,

//    val edited: String?,

    val ups: Long,

    val downs: Long,

    internal val replies: Item<Listing<RemoteComment>>,

    val created: Double,

    @SerialName("created_utc")
    val createdUtc: Double,
)

val RemoteComment.replies: List<RemoteComment>
    get() = replies.data.toList()