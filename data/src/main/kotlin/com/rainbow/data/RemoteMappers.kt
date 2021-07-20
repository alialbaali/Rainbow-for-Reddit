package com.rainbow.data

import com.rainbow.data.utils.toSystemLocalDateTime
import com.rainbow.domain.models.Award
import com.rainbow.domain.models.Comment
import com.rainbow.domain.models.Post
import com.rainbow.domain.models.Rule
import com.rainbow.remote.dto.*
import com.rainbow.sql.LocalPost
import com.rainbow.sql.LocalSubreddit
import com.rainbow.sql.LocalUser
import io.ktor.http.*

private const val DefaultColor = "000000"

internal object RemoteMappers {

    @OptIn(ExperimentalStdlibApi::class)
    val PostMapper = Mapper<RemotePost, LocalPost> {
        with(it) {
            val postType = when {
                selftext?.isNotBlank() == true -> Post.Type.Text(selftext!!)
                url?.isNotBlank() == true -> Post.Type.Image(url!!)
                else -> Post.Type.None
            }

            LocalPost(
                id = name ?: "",
                user_id = authorFullname?.removeRange(0, 3) ?: "",
                user_name = author ?: "",
                subreddit_id = subredditId?.removeRange(0, 3) ?: "",
                subreddit_name = subreddit ?: "",
                title = title ?: "",
//                type = postType,
                upvotes_count = ups?.toLong() ?: 0L,
                upvotes_ratio = upvoteRatio ?: 0.0,
                is_oc = isOriginalContent ?: false,
                is_spoiler = spoiler ?: false,
                comments_count = numComments?.toLong() ?: 0L,
                is_locked = locked ?: false,
                is_saved = false,
//        isEdited = edited ?: false,
                is_pinned = pinned ?: false,
//                awards = allAwardings
//                    ?.flatMap { remoteAward ->
//                        buildList {
//                            repeat(remoteAward.count.toInt()) {
//                                add(remoteAward)
//                            }
//                        }
//                    }?.quickMap(AwardMapper) ?: emptyList(),
                creation_date = createdUtc.toSystemLocalDateTime(),
                is_mine = isSelf ?: false,
                vote = likes,
                body = null,
                is_nsfw = false,
                is_edited = false,
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

    val UserMapper = Mapper<RemoteUser, LocalUser> {
        with(it) {
            LocalUser(
                id = id!!,
                name = name!!,
                post_karma = linkKarma?.toLong() ?: 0,
                comment_karma = commentKarma?.toLong() ?: 0,
                is_nsfw = subreddit?.over18 ?: false,
                creation_date = createdUtc.toSystemLocalDateTime(),
                image_url = iconImg!!,
                banner_image_url = subreddit?.bannerImg?.removeParameters() ?: ""
            )
        }
    }

    val CommentMapper = Mapper<RemoteComment, Comment> {
        with(it) {
            Comment(
                id = id ?: "",
                postId = linkId ?: "",
                userId = authorFullname ?: "",
                subredditId = subredditId ?: "",
                userName = author ?: "",
                subredditName = subreddit ?: "",
                body = body ?: "",
                upvotesCount = ups?.toULong() ?: 0UL,
                creationDate = createdUtc.toSystemLocalDateTime(),
            )
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
                image_url = communityIcon.removeParameters() ?: iconImg.removeParameters(),
                banner_image_url = bannerBackgroundImage.removeParameters() ?: mobileBannerImage.removeParameters(),
                is_nsfw = over18 ?: false,
                is_favorite = userHasFavorited ?: false,
                is_subscribed = userIsSubscriber ?: false,
                creation_date = createdUtc.toSystemLocalDateTime(),
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
                createdUtc.toSystemLocalDateTime(),
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
        null