package com.rainbow.remote.dto

import com.rainbow.remote.Listing
import com.rainbow.remote.RedditResponse

data class RemoteComment(
    val id: String?,
    val author: String?,
    val edited: String?,
    val ups: Long?,
    val downs: Long?,
    val replies: RedditResponse<Listing<RemoteComment>>,
)
