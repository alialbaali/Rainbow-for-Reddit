package com.rainbow.domain.models

import com.rainbow.domain.asUserIdPrefixed
import com.rainbow.domain.asUserNamePrefixed
import kotlinx.datetime.LocalDateTime

private const val DefaultUserImageUrl = ""

data class User(
    val id: String,
    val name: String,
    val postKarma: Long,
    val commentKarma: Long,
    val imageUrl: String = DefaultUserImageUrl,
    val isNSFW: Boolean = false,
    val creationDate: LocalDateTime,
)

val User.totalKarma
    get() = postKarma + commentKarma

val User.redditUrl
    get() = "/user/$name"

val User.idPrefixed
    get() = id.asUserIdPrefixed()

val User.namePrefixed
    get() = name.asUserNamePrefixed()
