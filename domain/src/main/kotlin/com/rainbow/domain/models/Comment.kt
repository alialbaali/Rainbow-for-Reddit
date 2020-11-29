package com.rainbow.domain.models

import com.rainbow.domain.*
import com.rainbow.domain.asCommentIdPrefixed
import com.rainbow.domain.asPostIdPrefixed
import com.rainbow.domain.asSubredditIdPrefixed
import com.rainbow.domain.asUserIdPrefixed

data class Comment(
    val id: String,
    val postId: String,
    val userId: String,
    val userName: String,
    val subredditId: String,
    val subredditName: String,
    val parent: Comment? = null,
    val replies: List<Comment> = emptyList(),
    val body: String,
    val votes: Votes,
    val awards: List<Award> = emptyList(),
    val isEdited: Boolean = false,
)

val Comment.upvotesCount
    get() = votes.upvotesCount

val Comment.downvotesCount
    get() = votes.downvotesCount

val Comment.voteRatio
    get() = votes.voteRatio

val Comment.idPrefixed
    get() = id.asCommentIdPrefixed()

val Comment.postIdPrefixed
    get() = postId.asPostIdPrefixed()

val Comment.userIdPrefixed
    get() = userId.asUserIdPrefixed()

val Comment.subredditIdPrefixed
    get() = subredditId.asSubredditIdPrefixed()

val Comment.userNamePrefixed
    get() = userName.asUserNamePrefixed()

val Comment.subredditNamePrefixed
    get() = subredditName.asSubredditNamePrefixed()
