package com.rainbow.domain.models

import com.rainbow.domain.utils.asUserDisplayName
import kotlinx.datetime.Instant

data class Message(
    val id: String,
    val userId: String,
    val userName: String,
    val subredditName: String?,
    val subject: String,
    val body: String,
    val isNew: Boolean,
    val type: Type,
    val creationDate: Instant,
) {
    sealed interface Type {
        object Message : Type
        class Mention(val postId: String) : Type
        class PostReply(val postId: String) : Type
        class CommentReply(val postId: String) : Type
    }
}

val Message.userDisplayName
    get() = userName.asUserDisplayName()