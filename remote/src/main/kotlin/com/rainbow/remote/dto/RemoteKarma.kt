package com.rainbow.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteKarma(
    @SerialName("sr")
    val sr: String,
    @SerialName("comment_karma")
    val commentKarma: Int,
    @SerialName("link_karma")
    val linkKarma: Int,
)