package com.rainbow.domain.models

import com.rainbow.domain.*
import kotlinx.datetime.LocalDateTime

data class Post(
    val id: String,
    val userId: String,
    val userName: String,
    val subredditId: String,
    val subredditName: String,
    val title: String,
    val type: Type = Type.None,
    val commentsCount: Long,
    val votes: Votes,
    val isOC: Boolean = false,
    val isNSFW: Boolean = false,
    val isLocked: Boolean = false,
    val isSpoiler: Boolean = false,
    val isPinned: Boolean = false,
    val isEdited: Boolean = false,
    val awards: List<Award> = emptyList(),
    val creationDate: LocalDateTime,
) {
    sealed class Type {
        object None : Type()
        data class Text(val body: String) : Type()
        data class Link(val url: String) : Type()
        data class Gif(val url: String) : Type()
        data class Image(val url: String) : Type()
        data class Video(val url: String) : Type()
    }
}

val Post.idPrefixed
    get() = id.asPostIdPrefixed()

val Post.userIdPrefixed
    get() = userId.asUserIdPrefixed()

val Post.userNamePrefixed
    get() = userName.asUserNamePrefixed()

val Post.subredditIdPrefixed
    get() = subredditId.asSubredditIdPrefixed()

val Post.subredditNamePrefixed
    get() = subredditName.asSubredditNamePrefixed()

val Post.upvotesCount
    get() = votes.upvotesCount

val Post.downvotesCount
    get() = votes.downvotesCount

val Post.voteRatio
    get() = votes.voteRatio