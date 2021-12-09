package com.rainbow.domain.models

import com.rainbow.domain.utils.asSubredditDisplayName
import com.rainbow.domain.utils.asUserDisplayName
import kotlinx.datetime.LocalDateTime

data class Comment(
    override val id: String,
    val parentId: String,
    val postId: String,
    val userId: String,
    val subredditId: String,
    val userName: String,
    val subredditName: String,
    val replies: List<Comment> = emptyList(),
    val moreReplies: List<String> = emptyList(),
    val body: String,
    val upvotesCount: ULong,
    val awards: List<Award> = emptyList(),
    val isEdited: Boolean = false,
    val isSaved: Boolean = false,
    val vote: Vote = Vote.None,
    val flair: Flair, // make it nullable when it's empty?
    val creationDate: LocalDateTime,
) : Item

val Comment.userDisplayName
    get() = userName.asUserDisplayName()

val Comment.subredditDisplayName
    get() = subredditName.asSubredditDisplayName()
