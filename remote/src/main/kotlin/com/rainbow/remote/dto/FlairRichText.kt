package com.rainbow.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FlairRichText(
    @SerialName("a")
    val name: String? = null,
    @SerialName("e")
    val emoji: String? = null,
    @SerialName("u")
    val url: String? = null,
)