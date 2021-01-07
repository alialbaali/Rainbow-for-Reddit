package com.rainbow.domain.models

import com.rainbow.domain.utils.OauthUrl
import com.rainbow.domain.utils.asSubredditDisplayName
import com.rainbow.domain.utils.colorOf
import kotlinx.datetime.LocalDateTime

data class Subreddit(
    val id: String,
    val name: String,
    val title: String,
    val description: String,
    val subscribersCount: Long,
    val activeSubscribersCount: Long,
    val imageUrl: String? = null,
    val bannerImageUrl: String? = null,
    val isNSFW: Boolean = false,
    val colors: Colors = Colors.Default,
    val isSubscribed: Boolean,
    val isFavorite: Boolean,
    val creationDate: LocalDateTime,
) {
    data class Colors(
        val primary: Long,
        val banner: Long,
        val key: Long,
    ) {
        companion object {

            private val DefaultPrimaryColor = colorOf(red = "00", green = "00", blue = "00")

            private val DefaultBackgroundColor = colorOf(red = "FF", green = "FF", blue = "FF")

            private val DefaultKeyColor = colorOf(red = "FF", green = "FF", blue = "FF")

            val Default = Colors(
                DefaultPrimaryColor,
                DefaultBackgroundColor,
                DefaultKeyColor
            )
        }
    }
}

val Subreddit.displayName
    get() = name.asSubredditDisplayName()

val Subreddit.redditUrl
    get() = "/r/$name"

val Subreddit.fullUrl
    get() = OauthUrl + redditUrl

val Subreddit.primaryColor
    get() = colors.primary

val Subreddit.bannerColor
    get() = colors.banner

val Subreddit.keyColor
    get() = colors.key