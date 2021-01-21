package com.rainbow.data

import com.rainbow.data.utils.toSystemLocalDateTime
import com.rainbow.data.utils.validPostId
import com.rainbow.data.utils.validSubId
import com.rainbow.data.utils.validUserId
import com.rainbow.domain.models.*
import com.rainbow.remote.dto.*
import io.ktor.http.*

private const val DefaultColor = "000000"

internal object Mappers {

    @OptIn(ExperimentalStdlibApi::class)
    val PostMapper = Mapper<RemotePost, Post> {
        with(it) {
            val postType = when {
                selftext.isNotBlank() -> Post.Type.Text(selftext)
                url.isNotBlank() -> Post.Type.Image(url)
                else -> Post.Type.None
            }

            Post(
                id = id,
                userId = authorFullname.removeRange(0, 3),
                userName = author,
                subredditId = subredditId.removeRange(0, 3),
                subredditName = subreddit,
                title = title,
                type = postType,
                votes = Votes(score ?: 0, downs ?: 0, upvoteRatio ?: 0F),
                isOC = isOriginalContent ?: false,
                isSpoiler = spoiler ?: false,
                commentsCount = (numComments ?: 0).toULong(),
                isLocked = locked ?: false,
                isSaved = false,
//        isEdited = edited ?: false,
                isPinned = pinned ?: false,
                awards = allAwards
                    .flatMap { remoteAward ->
                        buildList {
                            repeat(remoteAward.count.toInt()) {
                                add(remoteAward)
                            }
                        }
                    }
                    .quickMap(AwardMapper),
                creationDate = createdUtc.toLong().toSystemLocalDateTime(),
            )
        }
    }

    val AwardMapper = Mapper<RemoteAward, Award> {
        with(it) {
            Award(
                id = id,
                name = name,
                description = description,
                imageUrl = staticIconUrl,
                price = coinPrice ?: 0L
            )
        }
    }

    val UserMapper = Mapper<RemoteUser, User> {
        with(it) {
            User(
                id = id,
                name = name,
                postKarma = linkKarma ?: 0,
                commentKarma = commentKarma ?: 0,
//                isNSFW = subreddit?.over18 ?: false,
                creationDate = createdUtc?.toLong().toSystemLocalDateTime(),
            ).let { it.copy(iconImg ?: it.imageUrl) }
        }
    }

    val CommentMapper = Mapper<RemoteComment, Comment> {
        with(it) {
            Comment(
                id = id,
                postId = validPostId,
                userId = validUserId,
                subredditId = validSubId,
                userName = author,
                subredditName = subreddit,
                body = body,
                votes = Votes(ups ?: 0,
                    0,
                    0F), // TODO API Doesn't return [voteRatio] and it always returns downvotes as 0
                creationDate = createdUtc?.toLong().toSystemLocalDateTime(),
            )
        }
    }

    val SubredditMapper = Mapper<RemoteSubreddit, Subreddit> {
        with(it) {
//            val colors = Subreddit.Colors(
//                colorOf(primaryColor?.removeHashtagPrefix() ?: DefaultColor),
//                colorOf(bannerBackgroundColor?.removeHashtagPrefix() ?: DefaultColor),
//                colorOf(keyColor?.removeHashtagPrefix() ?: DefaultColor)
//            )
            Subreddit(
                id = id,
                name = displayName,
                title = title,
                description = publicDescription,
                subscribersCount = subscribers,
                activeSubscribersCount = activeUserCount ?: 0,
                imageUrl = communityIcon.removeParameters(),
                bannerImageUrl = bannerBackgroundImage.removeParameters(),
                colors = Subreddit.Colors.Default,
                isNSFW = over18 ?: false,
                isFavorite = userHasFavorited ?: false,
                isSubscribed = userIsSubscriber ?: false,
                creationDate = createdUtc?.toLong().toSystemLocalDateTime(),
            )
        }
    }

    val RuleMapper = Mapper<RemoteRule, Rule> {
        with(it) {
            Rule(
                shortName!!,
                description!!,
                priority?.toUInt()!!,
                violationReason!!,
                createdUtc?.toLong().toSystemLocalDateTime(),
            )
        }
    }

}

fun String.removeHashtagPrefix() = removePrefix("#")

fun String?.removeParameters(): String? =
    if (!isNullOrBlank())
        Url(this).run {
            "${protocol.name}://$host$encodedPath"
        }
    else
        this