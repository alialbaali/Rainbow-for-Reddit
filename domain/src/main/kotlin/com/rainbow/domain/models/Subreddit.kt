package com.rainbow.domain.models

import com.rainbow.domain.utils.RedditUrl
import com.rainbow.domain.utils.asSubredditDisplayName
import kotlinx.datetime.Instant

data class Subreddit(
    val id: String,
    val name: String,
    val title: String,
    val shortDescription: String,
    val longDescription: String,
    val subscribersCount: Int,
    val activeSubscribersCount: Int,
    val imageUrl: String?,
    val bannerImageUrl: String?,
    val isNSFW: Boolean,
    val colors: Colors,
    val isSubscribed: Boolean,
    val isFavorite: Boolean,
    val type: Type = Type.Public,
    val creationDate: Instant,
) {
    data class Colors(
        val primary: Long?,
        val banner: Long?,
        val key: Long?,
    )

    enum class Type {
        Public, Private
    }
}

val Subreddit.displayName
    get() = name.asSubredditDisplayName()

val Subreddit.redditUrl
    get() = "/r/$name"

val Subreddit.fullUrl
    get() = RedditUrl + redditUrl

val Subreddit.primaryColor
    get() = colors.primary

val Subreddit.bannerColor
    get() = colors.banner

val Subreddit.keyColor
    get() = colors.key