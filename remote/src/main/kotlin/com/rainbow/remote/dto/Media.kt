package com.rainbow.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Media(
    @SerialName("oembed")
    val oembed: Oembed? = null,

    @SerialName("reddit_video")
    val redditVideo: RedditVideo? = null,
)