package com.rainbow.data

import com.rainbow.domain.models.Comment
import com.rainbow.domain.models.Message
import com.rainbow.domain.models.Moderator
import com.rainbow.domain.models.Rule
import com.rainbow.remote.dto.*
import com.rainbow.sql.*
import io.ktor.http.*

internal object RemoteMappers {

    val RainbowDatabase.RemotePostMapper
        get() = Mapper<RemotePost, LocalPost> {
            val id = it.name
            val url = it.url
            if (id != null && localLinkQueries.selectById(id).executeAsOneOrNull() == null)
                if (!url.isNullOrBlank() && (url.endsWith("jpg") || url.endsWith("png") || url.endsWith("gif")))
                    localLinkQueries.insert(LocalLink(id, url))
                else if (!it.media?.oembed?.thumbnailUrl.isNullOrBlank())
                    localLinkQueries.insert(LocalLink(id, it.media?.oembed?.thumbnailUrl!!))
                else if (!it.media?.oembed?.url.isNullOrBlank())
                    localLinkQueries.insert(LocalLink(id, it.media?.oembed?.url!!))
            it.allAwardings?.quickMap(AwardMapper(it.name!!))?.let {
                it.forEach {
                    localAwardQueries.insert(it)
                }
            }
            with(it) {
                LocalPost(
                    id = name!!,
                    user_id = authorFullname!!,
                    user_name = author!!,
                    subreddit_id = subredditId!!,
                    subreddit_name = subreddit!!,
                    title = title!!,
                    body = selftext.takeIf { !it.isNullOrBlank() },
                    upvotes_count = ups!!.toLong(),
                    upvotes_ratio = upvoteRatio!!,
                    is_oc = isOriginalContent!!,
                    is_spoiler = spoiler!!,
                    comments_count = numComments!!.toLong(),
                    is_locked = locked!!,
                    is_saved = false,
                    is_pinned = pinned!!,
                    creation_date = created!!.toLong(),
                    is_mine = isSelf!!,
                    vote = likes,
                    is_nsfw = false,
                    is_edited = false,
                    flair_text = linkFlairText.takeIf { !it.isNullOrBlank() },
                    flair_background_color = linkFlairBackgroundColor.takeIf { !it.isNullOrBlank() }
                        ?: if (linkFlairText.isNullOrBlank()) null else "#FFF5F5F5",
                    flair_text_color = when (linkFlairTextColor) {
                        "light" -> false
                        else -> true
                    },
                )
            }
        }

    fun AwardMapper(itemId: String) = Mapper<RemoteAward, LocalAward> {
        with(it) {
            LocalAward(
                id = id!!,
                item_id = itemId,
                name = name!!,
                description = description!!,
                url = staticIconUrl!!,
                price = coinPrice!!.toLong()
            )
        }
    }

    val UserMapper = Mapper<RemoteUser, LocalUser> {
        with(it) {
            LocalUser(
                id = id!!,
                name = name!!,
                description = it.subreddit?.publicDescription?.takeIf { it.isNotBlank() },
                post_karma = linkKarma?.toLong() ?: 0,
                comment_karma = commentKarma?.toLong() ?: 0,
                awardee_karma = awardeeKarma?.toLong() ?: 0,
                awarder_karma = awarderKarma?.toLong() ?: 0,
                is_nsfw = subreddit?.over18 ?: false,
                creation_date = created!!.toLong(),
                image_url = iconImg!!,
                banner_image_url = subreddit?.bannerImg?.removeParameters() ?: ""
            )
        }
    }

    val CommentMapper = Mapper<RemoteComment, Comment> {
        with(it) {
            toComment()
        }
    }

    private fun RemoteComment.toComment(): Comment {
        return Comment(
            id = name ?: "",
            postId = linkId ?: "",
            userId = authorFullname ?: "",
            subredditId = subredditId ?: "",
            userName = author ?: "",
            subredditName = subreddit ?: "",
            body = body ?: "",
            upvotesCount = ups?.toULong() ?: 0UL,
            creationDate = (created?.toLong() ?: 0).toLocalDateTime(),
            awards = emptyList(),
            replies = replies?.quickMap(CommentMapper) ?: emptyList()
        )
    }

    val SubredditMapper = Mapper<RemoteSubreddit, LocalSubreddit> {
        with(it) {
//            val colors = Subreddit.Colors(
//                colorOf(primaryColor?.removeHashtagPrefix() ?: DefaultColor),
//                colorOf(bannerBackgroundColor?.removeHashtagPrefix() ?: DefaultColor),
//                colorOf(keyColor?.removeHashtagPrefix() ?: DefaultColor)
//            )
            LocalSubreddit(
                id = id ?: "",
                name = displayName,
                title = title,
                description = publicDescription,
                subscribers_count = subscribers?.toLong() ?: 0,
                image_url = communityIcon?.removeParameters() ?: iconImg?.removeParameters(),
                banner_image_url = bannerBackgroundImage?.removeParameters() ?: mobileBannerImage?.removeParameters(),
                is_nsfw = over18 ?: false,
                is_favorite = userHasFavorited ?: false,
                is_subscribed = userIsSubscriber ?: false,
                creation_date = created!!.toLong(),
            )
        }
    }

    val RuleMapper = Mapper<RemoteRule, Rule> {
        with(it) {
            Rule(
                shortName!!,
                description!!,
                priority!!.toInt(),
                violationReason!!,
                createdUtc!!.toLong().toLocalDateTime(),
            )
        }
    }

    val ModeratorMapper = Mapper<RemoteModerator, Moderator> {
        with(it) {
            Moderator(
                id!!,
                name!!,
                modPermissions?.map { Moderator.Permission.valueOf(it.replaceFirstChar { it.uppercase() }) }!!,
                date!!.toLong().toLocalDateTime()
            )
        }
    }

    val MessageMapper = Mapper<RemoteMessage, Message> {
        with(it) {
            Message(
                name!!,
                authorFullname ?: "",
                author!!,
                subject!!,
                body!!,
                new!!,
                when (type) {
                    "unknown" -> Message.Type.Message
                    "post_reply" -> Message.Type.PostReply
                    "comment_reply" -> Message.Type.CommentReply
                    "username_mention" -> Message.Type.Mention
                    else -> Message.Type.Message
                },
                created!!.toLong().toLocalDateTime(),
            )
        }
    }
}

fun String.removeHashtagPrefix() = removePrefix("#")

fun String.removeParameters(): String? {
    return try {
        Url(this).run {

            "${protocol.name}://$host$encodedPath"
        }
    } catch (exception: Throwable) {
        null
    }
}