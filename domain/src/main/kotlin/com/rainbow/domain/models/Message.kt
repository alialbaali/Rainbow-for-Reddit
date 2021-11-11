package com.rainbow.domain.models

import com.rainbow.domain.utils.asUserDisplayName
import kotlinx.datetime.LocalDateTime

data class Message(
    val id: String,
    val userId: String,
    val userName: String,
    val subject: String,
    val body: String,
    val isNew: Boolean,
    val type: Type,
    val creationDate: LocalDateTime,
) {
    enum class Type {
        Mention,
        Message,
        PostReply,
        CommentReply,
    }
}

val Message.userDisplayName
    get() = userName.asUserDisplayName()