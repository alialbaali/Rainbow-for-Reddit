package com.rainbow.domain.models

import com.rainbow.domain.utils.asSubredditDisplayName
import com.rainbow.domain.utils.asUserDisplayName
import kotlinx.datetime.LocalDateTime

@OptIn(ExperimentalUnsignedTypes::class)
data class Post(
    val id: String,
    val userId: String,
    val userName: String,
    val subredditId: String,
    val subredditName: String,
    val title: String,
    val type: Type = Type.Default,
    val commentsCount: ULong,
    val votes: Votes,
    val isOC: Boolean = false,
    val isNSFW: Boolean = false,
    val isLocked: Boolean = false,
    val isSpoiler: Boolean = false,
    val isPinned: Boolean = false,
    val isEdited: Boolean = false,
    val awards: List<Award> = emptyList(),
    val isSaved: Boolean,
    val creationDate: LocalDateTime,
) {
    sealed class Type {
        object None : Type()
        data class Text(val body: String) : Type()
        data class Link(val url: String) : Type()
        data class Gif(val url: String) : Type()
        data class Image(val url: String) : Type()
        data class Video(val url: String) : Type()

        companion object {
            val Default = None
        }
    }
}

val Post.userDisplayName
    get() = userName.asUserDisplayName()

val Post.subredditDisplayName
    get() = subredditName.asSubredditDisplayName()

val Post.upvotesCount
    get() = votes.upvotesCount

val Post.downvotesCount
    get() = votes.downvotesCount

val Post.voteRatio
    get() = votes.voteRatio