package com.rainbow.remote.dto

data class RemoteSubreddit(
    val id: String,
    val name: String,
    val title: String,
    val over18: Boolean?,
    val displayName: String,
    val displayNamePrefixed: String?,
    val publicDescription: String?,
    val submitText: String?,
    val bannerImg: String?,
    val description: String?,
    val subscribers: Long?,
    val url: String?,
    val primaryColor: String?,
    val keyColor: String?,
    val bannerBackgroundColor: String?,
    val lang: String?,
    val activeUserCount: Long?,
    val created: Long?,
    val createdUtc: Long?,
)