package com.rainbow.domain.models

import com.rainbow.domain.utils.asUserDisplayName

data class Message(
    val id: String,
    val userId: String,
    val userName: String,
    val subject: String,
    val body: String,
    val toUserId: String,
)

val Message.userDisplayName
    get() = userName.asUserDisplayName()