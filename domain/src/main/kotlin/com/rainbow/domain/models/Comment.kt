package com.rainbow.domain.models

import com.rainbow.domain.utils.asSubredditDisplayName
import com.rainbow.domain.utils.asUserDisplayName
import kotlinx.datetime.LocalDateTime

data class Comment(
    override val id: String,
    val parentId: String,
    override val postId: String,
    val userId: String,
    val subredditId: String,
    val userName: String,
    val subredditName: String,
    val replies: List<Comment> = emptyList(),
    val type: Type,
    val body: String,
    val votesCount: Int,
    val awards: List<Award> = emptyList(),
    val isEdited: Boolean = false,
    val isSaved: Boolean = false,
    val vote: Vote = Vote.None,
    val flair: Flair, // make it nullable when it's empty?
    val creationDate: LocalDateTime,
    val isContinueThread: Boolean = false,
    val url: String,
) : Item {
    sealed interface Type {
        object None : Type
        class ViewMore(val replies: List<String>) : Type
        class ContinueThread(val parentId: String) : Type
    }
}

val Comment.userDisplayName
    get() = userName.asUserDisplayName()

val Comment.subredditDisplayName
    get() = subredditName.asSubredditDisplayName()
