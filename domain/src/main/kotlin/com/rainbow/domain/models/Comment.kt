package com.rainbow.domain.models

import com.rainbow.domain.utils.asSubredditDisplayName
import com.rainbow.domain.utils.asUserDisplayName
import kotlinx.datetime.LocalDateTime

data class Comment(
    val id: String,
    val postId: String,
    val userId: String,
    val subredditId: String,
    val userName: String,
    val subredditName: String,
    val parent: Comment? = null,
    val replies: List<Comment> = emptyList(),
    val body: String,
    val votes: Votes,
    val awards: List<Award> = emptyList(),
    val isEdited: Boolean = false,
    val isSaved: Boolean = false,
    val creationDate: LocalDateTime,
)

val Comment.upvotesCount
    get() = votes.upvotesCount

val Comment.downvotesCount
    get() = votes.downvotesCount

val Comment.voteRatio
    get() = votes.voteRatio

val Comment.userDisplayName
    get() = userName.asUserDisplayName()

val Comment.subredditDisplayName
    get() = subredditName.asSubredditDisplayName()
