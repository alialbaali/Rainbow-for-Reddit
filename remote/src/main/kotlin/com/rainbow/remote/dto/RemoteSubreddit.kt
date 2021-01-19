package com.rainbow.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteSubreddit(
    val id: String,

    val name: String,

    val title: String,

    val over18: Boolean,

    @SerialName("display_name")
    val displayName: String,

    @SerialName("public_description")
    val publicDescription: String,

    // A message when writing a post configured by each subreddit
    @CouldBeEmpty
    @SerialName("submit_text")
    val submitText: String,

    val description: String,

    val subscribers: Long,

    // Subreddit url such as "/r/Kotlin/"
    val url: String,

    @CouldBeEmpty
    @SerialName("primary_color")
    val primaryColor: String,

    @CouldBeEmpty
    @SerialName("key_color")
    val keyColor: String,

    @CouldBeEmpty
    @SerialName("banner_background_color")
    val bannerBackgroundColor: String,

    @CouldBeEmpty
    @SerialName("community_icon")
    val communityIcon: String,

    @CouldBeEmpty
    @SerialName("banner_background_image")
    val bannerBackgroundImage: String,

    val lang: String,

    // Nullable when fetching user's subreddits list
    @SerialName("active_user_count")
    val activeUserCount: Long?,

    // Nullable for not logged-in users
    @SerialName("user_has_favorited")
    val userHasFavorited: Boolean?,

    // Nullable for not logged-in users
    @SerialName("user_is_contributor")
    val userIsContributor: Boolean?,

    // Nullable for not logged-in users
    @SerialName("user_is_subscriber")
    val userIsSubscriber: Boolean?,

    // Nullable for not logged-in users
    @SerialName("user_is_moderator")
    val userIsModerator: Boolean?,

    val created: Double?,

    @SerialName("created_utc")
    val createdUtc: Double?,
)