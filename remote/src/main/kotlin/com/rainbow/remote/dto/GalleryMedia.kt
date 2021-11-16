package com.rainbow.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GalleryMedia(
    @SerialName("e")
    val e: String? = null, // Image
    @SerialName("id")
    val id: String? = null, // qssw9v6f5uz71
    @SerialName("m")
    val m: String? = null, // image/jpg
    @SerialName("p")
    val preview: List<Preview>? = null,
    @SerialName("s")
    val source: Source? = null,
    @SerialName("status")
    val status: String? = null // valid
)