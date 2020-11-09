package com.rainbow.remote.dto

import kotlinx.serialization.Serializable

//@Serializable
data class User(
    val id: String?,
    val username: String?,
    val banner: String?,
    val avatar: String?,
    val linkKarma: Int?,
    val commentKarma: Int?,
    val iconImg: String?,
    val totalKarma: String?,
    val verified: Boolean,
    val isGold: Boolean?,
    val isMod: Boolean?,
    val subreddit: Subreddit?,
)