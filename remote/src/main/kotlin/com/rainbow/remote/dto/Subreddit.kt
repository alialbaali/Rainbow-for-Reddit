package com.rainbow.remote.dto

import kotlinx.serialization.Serializable

//@Serializable
data class Subreddit(
    val name: String?,
    val title: String?,
    val over18: Boolean?,
    val displayName: String?,
    val displayNamePrefixed: String?,
    val publicDescription: String?,
    val submitText: String?,
    val bannerImg: String?,
    val description: String?,
    val subscribers: Int?,
    val url: String?,
    val activeUserCount: Int?,
)