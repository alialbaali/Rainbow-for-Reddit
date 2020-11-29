package com.rainbow.domain.models

import com.rainbow.domain.asMessageIdPrefixed
import com.rainbow.domain.asUserIdPrefixed
import com.rainbow.domain.asUserNamePrefixed

data class Message(
    val id: String,
    val userId: String,
    val userName: String,
    val subject: String,
    val body: String,
    val toUser: String,
)

val Message.idPrefixed
    get() = id.asMessageIdPrefixed()

val Message.userIdPrefixed
    get() = userId.asUserIdPrefixed()

val Message.userNamePrefixed
    get() = userName.asUserNamePrefixed()