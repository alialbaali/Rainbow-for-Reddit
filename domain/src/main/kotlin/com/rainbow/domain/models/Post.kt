package com.rainbow.domain.models

import kotlinx.datetime.LocalDate

data class Post(
    val id: String,
    val userId: String, // TODO Decide whether to use Id, name or both
    val subredditId: String, // TODO Decide whether to use Id, name or both
    val title: String,
    val type: Type = Type.None,
    val commentsCount: Long,
    val votes: Votes,
    val isOC: Boolean = false,
    val isNSFW: Boolean = false,
    val isLocked: Boolean = false,
    val isSpoiler: Boolean = false,
    val isPinned: Boolean = false,
    val awards: List<Award> = emptyList(),
    val creationDate: LocalDate,
) {
    sealed class Type {
        data class Image(val url: String) : Type()
        data class Text(val body: String) : Type()
        object None : Type()
        // TODO Add Video
    }

    data class Votes(
        val upVotesCount: Long,
        val downVotesCount: Long,
        val voteRatio: Float,
    )
}

val Post.redditId
    get() = "t3_$id"

val Post.upVotesCount
    get() = votes.upVotesCount

val Post.downVotesCount
    get() = votes.downVotesCount

val Post.voteRatio
    get() = votes.voteRatio

val Post.awardsCount
    get() = awards.count()