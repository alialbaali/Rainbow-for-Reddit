package com.rainbow.remote.dto

data class RemoteUser(
    val id: String,
    val name: String,
    val authorFlairText: String?,
    val banner: String?,
    val avatar: String?,
    val linkKarma: Long?,
    val commentKarma: Long?,
    val iconImg: String?,
    val totalKarma: Long?,
    val verified: Boolean,
    val isGold: Boolean?,
    val isMod: Boolean?,
    val subreddit: RemoteSubreddit?,
    val created: Long?,
    val createdUtc: Long?,
)