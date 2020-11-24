package com.rainbow.domain.models

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
        data class Image(val url: String) : Type()
        data class Video(val url: String) : Type()
    }
}

val Post.idPrefixed
    get() = "t3_$id"

val Post.userIdPrefixed
    get() = "t2_$userId"

val Post.userNamePrefixed
    get() = "u/$userName"

val Post.subredditIdPrefixed
    get() = "t5_$subredditId"

val Post.subredditNamePrefixed
    get() = "r/$subredditName"

val Post.upvotesCount
    get() = votes.upvotesCount

val Post.downvotesCount
    get() = votes.downvotesCount

val Post.voteRatio
    get() = votes.voteRatio

val Post.awardsCount
    get() = awards.count()