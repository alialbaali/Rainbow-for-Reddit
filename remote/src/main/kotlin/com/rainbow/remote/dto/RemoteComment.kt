package com.rainbow.remote.dto

import com.rainbow.remote.Item
import com.rainbow.remote.Listing

data class RemoteComment internal constructor(
    val id: String,
    val author: String,
    val authorFullName: String,
    val linkId: String,
    val subredditId: String,
    val subreddit: String,
    val body: String,
    val edited: String?,
    val ups: Long?,
    val downs: Long?,
    internal val replies: Item<Listing<RemoteComment>>,
    val created: Long?,
    val createdUtc: Long?,
)

val RemoteComment.replies: List<RemoteComment>
    get() = replies.data
        .children
        .map { it.data }

val RemoteComment.validPostId: String
    get() = linkId.substringId()

val RemoteComment.validSubId: String
    get() = subredditId.substringId()

val RemoteComment.validUserId: String
    get() = authorFullName.substringId()


private fun String.substringId() = substringAfter('_')