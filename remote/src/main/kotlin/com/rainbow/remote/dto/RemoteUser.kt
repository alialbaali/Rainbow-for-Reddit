package com.rainbow.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteUser(
    val id: String,

    val name: String,

    @SerialName("link_karma")
    val linkKarma: Long,

    @SerialName("comment_karma")
    val commentKarma: Long,

    @SerialName("total_karma")
    val totalKarma: Long,

    @SerialName("icon_img")
    val iconImg: String,

    val verified: Boolean,

    @SerialName("is_gold")
    val isGold: Boolean,

    @SerialName("is_mod")
    val isMod: Boolean,

    val subreddit: RemoteSubreddit,

    val created: Double,

    @SerialName("created_utc")
    val createdUtc: Double,
)