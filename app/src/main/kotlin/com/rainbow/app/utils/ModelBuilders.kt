package com.rainbow.app.utils

import com.rainbow.data.utils.ZeroDateTime
import com.rainbow.domain.models.*

//private val EmptyVotes = Votes(0, 0, 0F)
private const val EmptyId = ""
private const val ZeroCommentsCount = 0UL
private const val UserId = ""
private const val UserName = ""

//fun postOf(
//    subredditName: String,
//    title: String,
//    type: Post.Type,
//    isNSFW: Boolean,
//    isSpoiler: Boolean,
//): Post {
//    return Post(
//        id = EmptyId,
//        userId = UserId,
//        userName = UserName,
//        EmptyId,
//        subredditName,
//        title,
//        type,
//        commentsCount = ZeroCommentsCount,
//        isNSFW = isNSFW,
//        isSpoiler = isSpoiler,
//        creationDate = ZeroDateTime,
//        isSaved = false,
//    )
//}

//fun commentOf(body: String): Comment {
//    return Comment(
//        EmptyId,
//        UserId,
//        UserName,
//        EmptyId,
//        EmptyId,
//        EmptyId,
//        body = body,
//
//        creationDate = ZeroDateTime,
//    )
//}

fun messageOf(subject: String, body: String, toUser: String): Message {
    return Message(
        EmptyId,
        UserId,
        subject,
        body,
        toUser,
        ""
    )
}

fun upvote() = Vote.Up

fun downvote() = Vote.Down

fun Unvote() = Vote.None
