package com.rainbow.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Oembed(
    @SerialName("url")
    val url: String? = null,

    @SerialName("thumbnail_url")
    val thumbnailUrl: String? = null
)