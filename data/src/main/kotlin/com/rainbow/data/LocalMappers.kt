package com.rainbow.data

import com.rainbow.data.utils.toLongColor
import com.rainbow.domain.models.*
import com.rainbow.sql.*
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

internal object LocalMappers {

    val AwardMapper = Mapper<LocalAward, Award> {
        Award(
            it.id,
            it.name,
            it.description,
            it.url,
            it.price
        )
    }

    val RainbowDatabase.LocalPostMapper: Mapper<LocalPost, Post>
        get() = Mapper {
            val flairs = localPostFlairQueries.selectById(it.id).executeAsList()
                .mapNotNull { localFlair ->
                    if (localFlair.text != null)
                        Flair.TextFlair(localFlair.text!!)
                    else if (localFlair.url != null)
                        Flair.ImageFlair(localFlair.url!!)
                    else
                        null
                }
            Post(
                id = it.id,
                userId = it.user_id,
                userName = it.user_name,
                title = it.title,
                type = it.body?.let { it -> Post.Type.Text(it) } ?: localLinkQueries.selectById(it.id)
                    .executeAsList()
                    .map { it.url }
                    .let { links ->
                        val validLinks = links.mapNotNull { it.removeParameters() }
                        when {
                            links.isEmpty() -> Post.Type.None
                            links.any { it.contains("jpg") } || links.any { it.contains("png") } -> {
                                Post.Type.Image(links)
                            }
                            validLinks.any { it.endsWith("gif") } -> Post.Type.Gif(validLinks.first { it.endsWith("gif") })
                            validLinks.any { it.endsWith("mp4") } -> Post.Type.Video(validLinks.first { it.endsWith("mp4") })
                            else -> Post.Type.Link(links.first())
                        }
                    },
                subredditId = it.subreddit_id,
                subredditName = it.subreddit_name,
                upvotesCount = it.upvotes_count.toULong(),
                upvotesRatio = it.upvotes_ratio,
                commentsCount = it.comments_count.toULong(),
                isOC = it.is_oc,
                isNSFW = it.is_nsfw,
                isLocked = it.is_locked,
                isSpoiler = it.is_spoiler,
                isEdited = it.is_edited,
                isMine = it.is_mine,
                isSaved = it.is_saved,
                isPinned = it.is_pinned,
                creationDate = it.creation_date.toLocalDateTime(),
                vote = it.vote?.let {
                    if (it)
                        Vote.Up
                    else
                        Vote.Down
                } ?: Vote.None,
                awards = localAwardQueries.selectById(it.id)
                    .executeAsList()
                    .quickMap(AwardMapper),
                flairs = flairs,
                flairBackgroundColor = it.flair_background_color.toLongColor(),
                flairTextColor = if (it.flair_text_color) Flair.TextColor.Dark else Flair.TextColor.Light
            )
        }

    val SubredditMapper = Mapper<LocalSubreddit, Subreddit> {
        Subreddit(
            id = it.id,
            name = it.name,
            title = it.title,
            description = it.description,
            subscribersCount = it.subscribers_count,
            activeSubscribersCount = 0L,
            imageUrl = it.image_url,
            bannerImageUrl = it.banner_image_url,
            isNSFW = it.is_nsfw,
            isSubscribed = it.is_subscribed,
            isFavorite = it.is_favorite,
            creationDate = it.creation_date.toLocalDateTime(),
            colors = Subreddit.Colors.Default,
        )
    }


    val UserMapper = Mapper<LocalUser, User> {
        User(
            it.id,
            it.name,
            it.description,
            it.post_karma,
            it.comment_karma,
            it.awardee_karma,
            it.awarder_karma,
            it.image_url,
            it.banner_image_url,
            it.is_nsfw,
            it.creation_date.toLocalDateTime(),
        )
    }

}

fun Long.toLocalDateTime() = Instant
    .fromEpochSeconds(this)
    .toLocalDateTime(TimeZone.currentSystemDefault())