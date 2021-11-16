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
                else if (!it.mediaMetadata.isNullOrEmpty())
                    it.mediaMetadata?.values?.map { it.source?.url?.removeAmp() }?.forEach { url ->
                        if (url != null)
                            localLinkQueries.insert(LocalLink(id, url))
                    }
                else if (it.isVideo == true && it.media?.redditVideo?.fallbackUrl != null)
                    localLinkQueries.insert(LocalLink(id, it.media?.redditVideo?.fallbackUrl!!))

            it.allAwardings?.quickMap(AwardMapper(it.name!!))?.let {
                it.forEach {
                    localAwardQueries.insert(it)
                }
            }
            if (!it.linkFlairText.isNullOrBlank() && !it.linkFlairRichtext.isNullOrEmpty())
                it.linkFlairRichtext!!.forEach { flair ->
                    if (flair.text != null)
                        localPostFlairQueries.insert(LocalPostFlair(it.name!!, url = null, flair.text))
                    else if (flair.url != null)
                        localPostFlairQueries.insert(LocalPostFlair(it.name!!, flair.url!!, text = null))
                }
            else if (!it.linkFlairText.isNullOrBlank() && it.linkFlairRichtext.isNullOrEmpty())
                localPostFlairQueries.insert(LocalPostFlair(it.name!!, url = null, it.linkFlairText))

            if (!it.authorFlairText.isNullOrBlank() && !it.authorFlairRichtext.isNullOrEmpty())
                it.authorFlairRichtext!!.forEach { flair ->
                    if (flair.text != null)
                        if (flair.text != null)
                            localPostFlairQueries.insert(LocalPostFlair(it.authorFullname!!, url = null, flair.text))
                        else if (flair.url != null)
                            localPostFlairQueries.insert(LocalPostFlair(it.authorFullname!!, flair.url!!, text = null))
                }
            else if (!it.authorFlairText.isNullOrBlank() && it.authorFlairRichtext.isNullOrEmpty())
                localPostFlairQueries.insert(LocalPostFlair(it.authorFullname!!, url = null, it.authorFlairText))

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
                    is_hidden = hidden!!,
                    vote = likes,
                    is_nsfw = false,
                    is_edited = false,
                    flair_background_color = linkFlairBackgroundColor.takeIf { !it.isNullOrBlank() } ?: "#FFF5F5F5",
                    flair_text_color = when (linkFlairTextColor) {
                        "light" -> false
                        else -> true
                    },
                    user_flair_background_color = authorFlairBackgroundColor.takeIf { !it.isNullOrBlank() }
                        ?: "#FFF5F5F5",
                    user_flair_text_color = when (authorFlairTextColor) {
                        "light" -> false
                        else -> true
                    },
                    url = permalink!!.toRedditUrl(),
                    is_read = localPostQueries.selectById(it.name!!)
                        .executeAsOneOrNull()
                        ?.is_read ?: false,
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

    private fun String.toRedditUrl() = "https://www.reddit.com$this"
    private fun String.removeAmp() = replace("amp;", "&")
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