package com.rainbow.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PreviewResponse(
    @SerialName("images")
    val images: List<PreviewItem>? = null,
    @SerialName("enabld")
    val enabled: Boolean? = null,
)