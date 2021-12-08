package com.rainbow.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PreviewItem(
    @SerialName("source")
    val source: PreviewSource? = null,
    @SerialName("resolutions")
    val resolutions: List<PreviewSource>? = null,
)