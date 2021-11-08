package com.rainbow.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RedditVideo(
    @SerialName("scrubber_media_url")
    val scrubberMediaUrl: String? = null
)