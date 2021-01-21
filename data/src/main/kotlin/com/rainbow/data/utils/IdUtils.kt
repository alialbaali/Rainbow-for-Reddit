package com.rainbow.data.utils

private const val CommentId = "t1_"
private const val UserId = "t2_"
private const val PostId = "t3_"
private const val MessageId = "t4_"
private const val SubredditId = "t5_"
private const val AwardId = "t6_"

internal fun String.asCommentIdPrefixed() = CommentId + this

internal fun String.asUserIdPrefixed() = UserId + this

internal fun String.asPostIdPrefixed() = PostId + this

internal fun String.asMessageIdPrefixed() = MessageId + this

internal fun String.asSubredditIdPrefixed() = SubredditId + this

internal fun String.asAwardIdPrefixed() = AwardId + this