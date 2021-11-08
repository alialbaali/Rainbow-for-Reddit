package com.rainbow.data

import com.rainbow.domain.models.Award
import com.rainbow.domain.models.Comment
import com.rainbow.domain.models.Rule
import com.rainbow.remote.dto.*
import com.rainbow.sql.LocalPost
import com.rainbow.sql.LocalSubreddit
import com.rainbow.sql.LocalUser
import io.ktor.http.*

internal object RemoteMappers {
    
    val PostMapper = Mapper<RemotePost, LocalPost> { remotePost ->
        with(remotePost) {
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
            Comment(
                id = id ?: "",
                postId = linkId ?: "",
                userId = authorFullname ?: "",
                subredditId = subredditId ?: "",
                userName = author ?: "",
                subredditName = subreddit ?: "",
                body = body ?: "",
                upvotesCount = ups?.toULong() ?: 0UL,
                creationDate = (created?.toLong() ?: 0).toLocalDateTime(),
                awards = allAwardings?.quickMap(AwardMapper) ?: emptyList()
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
        TODO()
//        with(it) {
//            Rule(
//                shortName!!,
//                description!!,
//                priority!!.toInt(),
//                violationReason!!,
//                created!!.toLong(),
//            )
//        }
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