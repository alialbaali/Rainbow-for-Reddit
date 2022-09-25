package com.rainbow.domain.models

import com.rainbow.domain.utils.asSubredditDisplayName
import com.rainbow.domain.utils.asUserDisplayName
import kotlinx.datetime.Instant

data class Post(
    override val id: String,
    val userId: String,
    val userName: String,
    val subredditId: String,
    val subredditName: String,
    val title: String,
    val type: Type = Type.Default,
    val votesCount: Int,
    val upvotesRatio: Double,
    val commentsCount: UInt,
    val isOC: Boolean = false,
    val isNSFW: Boolean = false,
    val isLocked: Boolean = false,
    val isSpoiler: Boolean = false,
    val isPinned: Boolean = false,
    val isEdited: Boolean = false,
    val isMine: Boolean = false,
    val isSaved: Boolean = false,
    val isHidden: Boolean = false,
    val isRead: Boolean = false,
    val vote: Vote = Vote.None,
    val awards: List<Award> = emptyList(),
    val flair: Flair,
    val userFlair: Flair,
    val url: String,
    val creationDate: Instant,
    val subredditImageUrl: String? = null,
) : Item {
    sealed interface Type {
        object None : Type
        data class Text(val body: String) : Type
        data class Link(val url: String, val previewUrl: String, val host: String) : Type
        data class Gif(val url: String) : Type
        data class Image(val urls: List<String>) : Type
        data class Video(val url: String) : Type

        companion object {
            val Default = None
        }
    }

    override val postId: String = id
}

val Post.userDisplayName
    get() = userName.asUserDisplayName()

val Post.subredditDisplayName
    get() = subredditName.asSubredditDisplayName()