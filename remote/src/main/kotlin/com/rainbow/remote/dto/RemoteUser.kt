package com.rainbow.remote.dto

data class RemoteUser(
    val id: String?,
    val name: String?,
    val banner: String?,
    val avatar: String?,
    val linkKarma: Int?,
    val commentKarma: Int?,
    val iconImg: String?,
    val totalKarma: Int?,
    val verified: Boolean,
    val isGold: Boolean?,
    val isMod: Boolean?,
    val subreddit: RemoteSubreddit?,
    val created: Long?,
    val createdUtc: Long?,
)