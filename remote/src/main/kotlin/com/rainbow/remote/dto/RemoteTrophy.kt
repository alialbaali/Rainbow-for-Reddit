package com.rainbow.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteTrophy(
    val id: String,

    val name: String,

    @CouldBeEmpty
    val description: String?,

    @SerialName("image_url")
    val imageUrl: String,
)