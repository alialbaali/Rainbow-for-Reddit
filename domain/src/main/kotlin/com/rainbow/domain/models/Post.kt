package com.rainbow.domain.models

import com.rainbow.domain.utils.asSubredditDisplayName
import com.rainbow.domain.utils.asUserDisplayName
import kotlinx.datetime.LocalDateTime

data class Post(
    val id: String,
    val userId: String,
    val userName: String,
    val subredditId: String,
    val subredditName: String,
    val title: String,
    val type: Type = Type.Default,
    val upvotesCount: ULong,
    val upvotesRatio: Double,
    val commentsCount: ULong,
    val isOC: Boolean = false,
    val isNSFW: Boolean = false,
    val isLocked: Boolean = false,
    val isSpoiler: Boolean = false,
    val isPinned: Boolean = false,
    val isEdited: Boolean = false,
    val isMine: Boolean = false,
    val isSaved: Boolean = false,
    val vote: Vote = Vote.None,
    val awards: List<Award> = emptyList(),
    val flairs: List<Flair> = emptyList(),
    val flairBackgroundColor: Long,
    val flairTextColor: Flair.TextColor,
    val creationDate: LocalDateTime,
) {
    sealed interface Type {
        object None : Type
        data class Text(val body: String) : Type
        data class Link(val url: String) : Type
        data class Gif(val url: String) : Type
        data class Image(val urls: List<String>) : Type
        data class Video(val url: String) : Type

        companion object {
            val Default = None
        }
    }
}

val Post.userDisplayName
    get() = userName.asUserDisplayName()

val Post.subredditDisplayName
    get() = subredditName.asSubredditDisplayName()