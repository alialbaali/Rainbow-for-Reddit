package com.rainbow.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PreviewSource(
    @SerialName("url")
    val url: String? = null,
    @SerialName("width")
    val width: Int? = null,
    @SerialName("height")
    val height: Int? = null,
)