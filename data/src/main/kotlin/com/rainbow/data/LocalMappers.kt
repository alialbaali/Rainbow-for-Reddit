package com.rainbow.data

import com.rainbow.domain.models.Post
import com.rainbow.domain.models.Subreddit
import com.rainbow.domain.models.User
import com.rainbow.domain.models.Vote
import com.rainbow.sql.LocalPost
import com.rainbow.sql.LocalSubreddit
import com.rainbow.sql.LocalUser
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

internal object LocalMappers {

    fun PostMapper(): Mapper<LocalPost, Post> {
        return PostMapper
    }

    val PostMapper = Mapper<LocalPost, Post> {
        Post(
            id = it.id,
            userId = it.user_id,
            userName = it.user_name,
            title = it.title,
            type = it.body?.let { it ->
                Post.Type.Text(it)
            } ?: Post.Type.None,
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
            awards = emptyList()
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