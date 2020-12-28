package com.rainbow.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class RemoteSubreddit(
    val id: String,
    val name: String,
    val title: String,
    val over18: Boolean?,
    val displayName: String,
    val displayNamePrefixed: String?,
    val publicDescription: String,
    val submitText: String?,
    val description: String?,
    val subscribers: Long?,
    val url: String?,
    val primaryColor: String?,
    val keyColor: String?,
    val bannerBackgroundColor: String?,
    val communityIcon: String?,
    val bannerBackgroundImage: String?,
    val lang: String?,
    val activeUserCount: Long?,
    val userHasFavorited: Boolean?,
    val userIsContributor: Boolean?,
    val userIsSubscriber: Boolean?,
    val userIsModerator: Boolean?,
    val created: Long?,
    val createdUtc: Long?,
)