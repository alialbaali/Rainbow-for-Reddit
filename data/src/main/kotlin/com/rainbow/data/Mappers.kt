package com.rainbow.data

import com.rainbow.data.utils.toLongColor
import com.rainbow.domain.models.*
import com.rainbow.domain.models.Rule
import com.rainbow.remote.dto.*
import io.ktor.http.*
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

internal object Mappers {

    val ItemMapper = Mapper<RemoteItem, Item> {
        when (it) {
            is RemoteComment -> CommentMapper.map(it)
            is RemotePost -> PostMapper.map(it)
        }
    }

    val PostMapper = Mapper<RemotePost, Post> {
        with(it) {
            val previewUrl = preview?.images?.getOrNull(0)?.source?.url?.removeAmp()
            val validUrl = url?.removeParameters()
            val type = when {
                !selftext.isNullOrBlank() -> Post.Type.Text(selftext!!.trim())
                !url.isNullOrBlank() && url!!.isImage -> Post.Type.Image(listOf(url!!))
                !validUrl.isNullOrBlank() && !previewUrl.isNullOrBlank() -> Post.Type.Link(
                    validUrl,
                    previewUrl,
                    Url(validUrl).fullHost
                )

                mediaMetadata != null -> {
                    val urls = mediaMetadata!!.values.mapNotNull { it.source?.url?.removeAmp() }
                    Post.Type.Image(urls)
                }

                isVideo == true && media?.redditVideo?.fallbackUrl != null -> Post.Type.Video(media?.redditVideo?.fallbackUrl!!)
                else -> Post.Type.None
            }
            //    private fun RainbowDatabase.savePostLinks(remotePost: RemotePost) {
//        val id = remotePost.name
//        val url = remotePost.url
//        if (id != null && localLinkQueries.selectById(id).executeAsOneOrNull() == null)
//            if (!url.isNullOrBlank() && (url.endsWith("jpg") || url.endsWith("png") || url.endsWith("gif")))
//                localLinkQueries.insert(LocalLink(id, url))
//            else if (!remotePost.media?.oembed?.thumbnailUrl.isNullOrBlank())
//                localLinkQueries.insert(LocalLink(id, remotePost.media?.oembed?.thumbnailUrl!!))
//            else if (!remotePost.media?.oembed?.url.isNullOrBlank())
//                localLinkQueries.insert(LocalLink(id, remotePost.media?.oembed?.url!!))
//            else if (!remotePost.mediaMetadata.isNullOrEmpty())
//                remotePost.mediaMetadata?.values?.map { it.source?.url?.removeAmp() }?.forEach { url ->
//                    if (url != null)
//                        localLinkQueries.insert(LocalLink(id, url))
//                }
//            else if (remotePost.isVideo == true && remotePost.media?.redditVideo?.fallbackUrl != null)
//                localLinkQueries.insert(LocalLink(id, remotePost.media?.redditVideo?.fallbackUrl!!))
//    }
            Post(
                id = name!!,
                userId = authorFullname ?: "",
                userName = author!!,
                subredditId = subredditId!!,
                subredditName = subreddit!!,
                title = title!!,
                type = type,
                votesCount = ups!!,
                upvotesRatio = upvoteRatio!!,
                isOC = isOriginalContent!!,
                isSpoiler = spoiler!!,
                commentsCount = numComments!!.toUInt(),
                isLocked = locked!!,
                isSaved = saved ?: false,
                isPinned = pinned!!,
                creationDate = created!!.toLong().toLocalDateTime(),
                isMine = isSelf!!,
                isHidden = hidden!!,
                vote = likes.toVote(),
                isNSFW = over18!!,
                isEdited = false,
                flair = FlairMapper.map(
                    RemoteFlair(
                        id = linkFlairTemplateId,
                        backgroundColor = linkFlairBackgroundColor,
                        textColor = linkFlairTextColor,
                        richtext = linkFlairRichtext,
                        text = linkFlairText,
                    )
                ),
                userFlair = FlairMapper.map(
                    RemoteFlair(
                        id = authorFlairTemplateId,
                        backgroundColor = authorFlairBackgroundColor,
                        textColor = authorFlairTextColor,
                        richtext = authorFlairRichtext,
                        text = authorFlairText,
                    )
                ),
                url = permalink!!.toRedditUrl(),
                awards = allAwardings?.quickMap(AwardMapper) ?: emptyList()
            )
        }
    }

    val AwardMapper = Mapper<RemoteAward, Award> {
        with(it) {
            Award(
                id!!,
                name!!,
                description!!,
                staticIconUrl!!,
                count!!,
                coinPrice!!.toLong()
            )
        }
    }

    val UserMapper = Mapper<RemoteUser, User> {
        with(it) {
            User(
                id = subreddit?.name!!,
                name = name!!,
                description = it.subreddit?.publicDescription?.takeIf { it.isNotBlank() },
                postKarma = linkKarma?.toLong() ?: 0,
                commentKarma = commentKarma?.toLong() ?: 0,
                awardeeKarma = awardeeKarma?.toLong() ?: 0,
                awarderKarma = awarderKarma?.toLong() ?: 0,
                isNSFW = subreddit?.over18 ?: false,
                creationDate = created!!.toLong().toLocalDateTime(),
                imageUrl = iconImg!!,
                bannerImageUrl = subreddit?.bannerImg?.removeParameters() ?: ""
            )
        }
    }

    object CommentMapper : Mapper<RemoteComment, Comment> {
        override fun map(input: RemoteComment): Comment {
            return with(input) {
                Comment(
                    id = name ?: "",
                    parentId = parentId ?: "",
                    postId = linkId ?: "",
                    userId = authorFullname ?: "",
                    subredditId = subredditId ?: "",
                    userName = author ?: "",
                    subredditName = subreddit ?: "",
                    body = body ?: "",
                    votesCount = ups ?: 0,
                    creationDate = (created?.toLong() ?: 0L).toLocalDateTime(),
                    vote = likes.toVote(),
                    isEdited = false,
                    isSaved = saved ?: false,
                    replies = replies?.quickMap(CommentMapper) ?: emptyList(),
                    type = when {
                        children == null -> Comment.Type.None
                        children!!.isEmpty() -> Comment.Type.ContinueThread(parentId ?: "")
                        else -> Comment.Type.ViewMore(children!!)
                    },
                    flair = FlairMapper.map(
                        RemoteFlair(
                            id = authorFlairTemplateId,
                            backgroundColor = authorFlairBackgroundColor,
                            textColor = authorFlairTextColor,
                            richtext = authorFlairRichtext,
                            text = authorFlairText,
                        )
                    ),
                    awards = allAwardings?.quickMap(AwardMapper) ?: emptyList(),
                    url = permalink?.toRedditUrl().orEmpty()
                )
            }
        }
    }

//    val CommentMapper = Mapper<RemoteComment, Comment> {
//        with(it) {
////                replies?.let { replies -> saveCommentReplies(replies) }
////                allAwardings?.let { awards ->
////                    if (name != null)
////                        awards.quickMap(AwardMapper(name!!))
////                            .forEach { award -> localAwardQueries.insert(award) }
////
//        }
//    }

//    private fun RainbowDatabase.saveCommentReplies(replies: List<RemoteComment>) {
//        replies.forEach {
//            if (localCommentQueries.selectById(it.name!!).executeAsOneOrNull() == null) {
//                localCommentQueries.insert(CommentMapper.map(it))
//                it.replies?.let { saveCommentReplies(it) }
//            }
//        }
//    }

    val SubredditMapper = Mapper<RemoteSubreddit, Subreddit> {
        with(it) {
//            val colors = Subreddit.Colors(
//                colorOf(primaryColor?.removeHashtagPrefix() ?: DefaultColor),
//                colorOf(bannerBackgroundColor?.removeHashtagPrefix() ?: DefaultColor),
//                colorOf(keyColor?.removeHashtagPrefix() ?: DefaultColor)
//            )
            val validCommunityImage = communityIcon.takeIf { url -> !url.isNullOrBlank() }
                ?.removeParameters()
            val validIconImage = iconImg.takeIf { url -> !url.isNullOrBlank() }
                ?.removeParameters()
            val validBannerBackgroundImage = bannerBackgroundImage.takeIf { url -> !url.isNullOrBlank() }
                ?.removeParameters()
            val validBannerImage = bannerImg.takeIf { url -> !url.isNullOrBlank() }
                ?.removeParameters()
            val validBannerMobileImage = mobileBannerImage.takeIf { url -> !url.isNullOrBlank() }
                ?.removeParameters()

            Subreddit(
                id = name ?: "",
                name = displayName ?: "",
                title = title ?: "",
                shortDescription = publicDescription.takeIf { !it.isNullOrBlank() } ?: "",
                longDescription = description.takeIf { !it.isNullOrBlank() } ?: "",
                subscribersCount = subscribers ?: 0,
                activeSubscribersCount = activeUserCount ?: 0,
                imageUrl = validCommunityImage ?: validIconImage,
                bannerImageUrl = validBannerBackgroundImage ?: validBannerImage ?: validBannerMobileImage,
                isNSFW = over18 ?: false,
                isFavorite = userHasFavorited ?: false,
                isSubscribed = userIsSubscriber ?: false,
                creationDate = Instant.fromEpochSeconds(created!!.toLong()),
                colors = Subreddit.Colors.Default,
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
                FlairMapper.map(RemoteFlair(text = authorFlairText)),
                modPermissions?.map {
                    when (it) {
                        "all" -> Moderator.Permission.All
                        "wiki" -> Moderator.Permission.Wiki
                        "mail" -> Moderator.Permission.Mail
                        "config" -> Moderator.Permission.Config
                        "flair" -> Moderator.Permission.Flair
                        "access" -> Moderator.Permission.Access
                        "chat_operator" -> Moderator.Permission.ChatOperator
                        "posts" -> Moderator.Permission.Posts
                        "chat_config" -> Moderator.Permission.ChatConfig
                        else -> error("Permission isn't supported: $it")
                    }
                } ?: emptyList(),
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
                subreddit,
                when (type) {
                    "unknown" -> subject!!
                    else -> linkTitle!!
                },
                body!!,
                new!!,
                when (type) {
                    "unknown" -> Message.Type.Message
                    "post_reply" -> Message.Type.PostReply(parentId ?: "")
                    "comment_reply" -> Message.Type.CommentReply(context?.getPostId() ?: "")
                    "username_mention" -> Message.Type.Mention(context?.getPostId() ?: "")
                    else -> Message.Type.Message
                },
                created!!.toLong().toLocalDateTime(),
            )
        }
    }

    val FlairMapper = Mapper<RemoteFlair, Flair> {
        with(it) {
            val types = it.richtext.takeIf { !it.isNullOrEmpty() }
                ?.mapNotNull {
                    if (!it.text.isNullOrBlank())
                        Flair.Type.Text(it.text!!)
                    else if (!it.url.isNullOrBlank())
                        Flair.Type.Image(it.url!!)
                    else
                        null
                } ?: if (text.isNullOrBlank()) emptyList() else listOf(Flair.Type.Text(text.orEmpty()))
            val backgroundColor = backgroundColor.takeIf { !it.isNullOrBlank() } ?: "#FFF5F5F5"
            val textColor = when (textColor) {
                "light" -> Flair.TextColor.Light
                else -> Flair.TextColor.Dark
            }
            Flair(id ?: "", types, backgroundColor.toLongColor(), textColor)
        }
    }

    val WikiPageMapper
        get() = Mapper<RemoteWikiPage, WikiPage> {
            with(it) {
                WikiPage(
                    contentMd ?: "",
                    UserMapper.map(revisionBy!!),
                    revisionDate!!.toLong().toLocalDateTime(),
                )
            }
        }

    private fun String.toRedditUrl() = "https://www.reddit.com$this"
    private fun String.removeAmp() = replace("amp;", "&")

    private val String.isImage
        get() = endsWith("jpg") || endsWith("png")

    private fun String.removeHashtagPrefix() = removePrefix("#")

    private fun String.removeParameters(): String? {
        return try {
            Url(this).run {

                "${protocol.name}://$host$encodedPath"
            }
        } catch (exception: Throwable) {
            null
        }
    }

    private fun Boolean?.toVote() = when (this) {
        true -> Vote.Up
        false -> Vote.Down
        null -> Vote.None
    }

    fun Long.toLocalDateTime() = Instant
        .fromEpochSeconds(this)
        .toLocalDateTime(TimeZone.currentSystemDefault())

    private val Url.fullHost
        get() = "${protocol.name}://$host"

    private fun String.getPostId() = "t3_" + substringAfter("comments/").substringBefore("/")
}