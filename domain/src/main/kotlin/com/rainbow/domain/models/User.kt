package com.rainbow.domain.models

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
    get() = "t2_$id"

val User.namePrefixed
    get() = "u/$name"
