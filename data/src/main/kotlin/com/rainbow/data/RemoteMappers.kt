package com.rainbow.data

import com.rainbow.domain.models.Message
import com.rainbow.domain.models.Moderator
import com.rainbow.domain.models.Rule
import com.rainbow.remote.dto.*
import com.rainbow.sql.*
import io.ktor.http.*

internal object RemoteMappers {

    val RainbowDatabase.RemotePostMapper
        get() = Mapper<RemotePost, LocalPost> {
            savePostAwards(it)
            savePostLinks(it)
            savePostFlairs(it.name, it.linkFlairText, it.linkFlairRichtext)
            savePostUserFlairs(it.name, it.authorFullname, it.authorFlairText, it.authorFlairRichtext)
            with(it) {
                LocalPost(
                    id = name!!,
                    user_id = authorFullname ?: "",
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
                    is_nsfw = over18!!,
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
                count = count?.toLong() ?: 0L,
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

    val RainbowDatabase.RemoteCommentMapper
        get() = Mapper<RemoteComment, LocalComment> {
            with(it) {
                replies?.let { replies -> saveCommentReplies(replies) }
                allAwardings?.let { awards ->
                    if (name != null)
                        awards.quickMap(AwardMapper(name!!))
                            .forEach { award -> localAwardQueries.insert(award) }
                }

                LocalComment(
                    id = name ?: "",
                    parent_id = parentId ?: "",
                    post_id = linkId ?: "",
                    user_id = authorFullname ?: "",
                    subreddit_id = subredditId ?: "",
                    user_name = author ?: "",
                    subreddit_name = subreddit ?: "",
                    body = body ?: "",
                    upvotes_count = ups?.toLong() ?: 0L,
                    creation_date = created?.toLong() ?: 0,
                    vote = likes,
                    isEdited = false,
                    isSaved = saved ?: false,
                    more_replies = children.takeIf { !it.isNullOrEmpty() }?.joinToString()
                )
            }
        }

    private fun RainbowDatabase.saveCommentReplies(replies: List<RemoteComment>) {
        replies.forEach {
            if (localCommentQueries.selectById(it.name!!).executeAsOneOrNull() == null) {
                localCommentQueries.insert(RemoteCommentMapper.map(it))
                it.replies?.let { saveCommentReplies(it) }
            }
        }
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
                active_subscribers_count = activeUserCount?.toLong() ?: 0,
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

    private fun RainbowDatabase.savePostAwards(remotePost: RemotePost) {
        remotePost.allAwardings?.quickMap(AwardMapper(remotePost.name!!))?.let {
            it.forEach {
                localAwardQueries.insert(it)
            }
        }
    }

    private fun RainbowDatabase.savePostLinks(remotePost: RemotePost) {
        val id = remotePost.name
        val url = remotePost.url
        if (id != null && localLinkQueries.selectById(id).executeAsOneOrNull() == null)
            if (!url.isNullOrBlank() && (url.endsWith("jpg") || url.endsWith("png") || url.endsWith("gif")))
                localLinkQueries.insert(LocalLink(id, url))
            else if (!remotePost.media?.oembed?.thumbnailUrl.isNullOrBlank())
                localLinkQueries.insert(LocalLink(id, remotePost.media?.oembed?.thumbnailUrl!!))
            else if (!remotePost.media?.oembed?.url.isNullOrBlank())
                localLinkQueries.insert(LocalLink(id, remotePost.media?.oembed?.url!!))
            else if (!remotePost.mediaMetadata.isNullOrEmpty())
                remotePost.mediaMetadata?.values?.map { it.source?.url?.removeAmp() }?.forEach { url ->
                    if (url != null)
                        localLinkQueries.insert(LocalLink(id, url))
                }
            else if (remotePost.isVideo == true && remotePost.media?.redditVideo?.fallbackUrl != null)
                localLinkQueries.insert(LocalLink(id, remotePost.media?.redditVideo?.fallbackUrl!!))
    }

    private fun RainbowDatabase.savePostFlairs(
        id: String?,
        flairText: String?,
        flairRichText: List<FlairRichText>?
    ) {
        if (!flairText.isNullOrBlank() && !flairRichText.isNullOrEmpty())
            flairRichText.forEach { flair ->
                if (localPostFlairQueries.selectById(id!!)
                        .executeAsList()
                        .none { it.text == flair.text && it.url == flair.url }
                )
                    if (flair.text != null)
                        localPostFlairQueries.insert(LocalPostFlair(id, null, url = null, flair.text))
                    else if (flair.url != null)
                        localPostFlairQueries.insert(LocalPostFlair(id, null, flair.url!!, text = null))
            }
        else if (!flairText.isNullOrBlank() && flairRichText.isNullOrEmpty())
            if (localPostFlairQueries.selectById(id!!).executeAsList().none { it.text == flairText })
                localPostFlairQueries.insert(LocalPostFlair(id, null, url = null, flairText))
    }

    private fun RainbowDatabase.savePostUserFlairs(
        id: String?,
        userId: String?,
        flairText: String?,
        flairRichText: List<FlairRichText>?
    ) {
        if (!flairText.isNullOrBlank() && !flairRichText.isNullOrEmpty())
            flairRichText.forEach { flair ->
                if (localPostFlairQueries.selectByIdAndUserId(id!!, userId)
                        .executeAsList()
                        .none { it.text == flair.text && it.url == flair.url }
                )
                    if (flair.text != null)
                        localPostFlairQueries.insert(LocalPostFlair(id, userId, url = null, flair.text))
                    else if (flair.url != null)
                        localPostFlairQueries.insert(LocalPostFlair(id, userId, flair.url!!, text = null))
            }
        else if (!flairText.isNullOrBlank() && flairRichText.isNullOrEmpty())
            if (localPostFlairQueries.selectByIdAndUserId(id!!, userId).executeAsList().none { it.text == flairText })
                localPostFlairQueries.insert(LocalPostFlair(id, userId, url = null, flairText))
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