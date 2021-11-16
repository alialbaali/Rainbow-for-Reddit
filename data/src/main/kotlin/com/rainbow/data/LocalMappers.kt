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
            val imageFlair = localFlairQueries.selectById(it.id).executeAsList().map { it.url }
            Post(
                id = it.id,
                userId = it.user_id,
                userName = it.user_name,
                title = it.title,
                type = it.body?.let { it -> Post.Type.Text(it) } ?: localLinkQueries.selectById(it.id)
                    .executeAsOneOrNull()
                    ?.url
                    ?.removeParameters()
                    .let { link ->
                        when {
                            link == null -> Post.Type.None
                            link.endsWith("jpg") || link.endsWith("png") -> Post.Type.Image(link)
                            link.endsWith("gif") -> Post.Type.Gif(link)
                            link.endsWith("mp4") -> Post.Type.Video(link)
                            else -> Post.Type.Link(link)
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
                flair = if (it.flair_background_color != null && it.flair_text_color != null && it.flair_text != null)
                    Flair.TextFlair(
                        it.flair_text!!,
                        it.flair_background_color!!.toLongColor(),
                        if (it.flair_text_color!!) Flair.TextFlair.TextColor.Dark else Flair.TextFlair.TextColor.Light
                    )
                else if (imageFlair.isNotEmpty())
                    if (it.flair_background_color != null)
                        Flair.ImageFlair(imageFlair, it.flair_background_color!!.toLongColor())
                    else
                        null
                else
                    null
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