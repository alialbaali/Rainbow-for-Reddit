package com.rainbow.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteKarma(
    @SerialName("comment_karma")
    val commentKarma: Int? = null, // 39
    @SerialName("link_karma")
    val linkKarma: Int? = null, // 1809
    @SerialName("sr")
    val sr: String? = null // me_irl
)